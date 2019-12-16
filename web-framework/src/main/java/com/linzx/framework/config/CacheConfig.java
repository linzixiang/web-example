package com.linzx.framework.config;
;
import com.linzx.framework.bean.CacheBean;
import com.linzx.framework.config.condition.RedisOnEnableCondition;
import com.linzx.framework.config.properties.EhCacheProperties;
import com.linzx.framework.config.properties.ShiroProperties;
import com.linzx.framework.config.support.RedisSerializerImpl;
import com.linzx.utils.ResourceLoaderUtils;
import com.linzx.utils.StringUtils;
import com.linzx.utils.xml.Dom4jUtils;
import net.sf.ehcache.CacheManager;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Configuration
@DependsOn({"shiroProperties"})
@EnableConfigurationProperties({EhCacheProperties.class})
public class CacheConfig {

    @Value("${cache.manager.config-location}")
    private String configLocation;

    @Bean
    @ConditionalOnProperty(prefix = "cache.manager", name = "name", havingValue = "simple")
    public SimpleCacheManager simpleCacheManager() throws Exception {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CacheBean> cacheBeanList = resolverCacheXml();
        List<Cache> caches = new ArrayList<>();
        for (CacheBean cacheBean : cacheBeanList) {
            ConcurrentMapCache sysDictCache = new ConcurrentMapCache(getCacheName(cacheBean));
            caches.add(sysDictCache);
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    /**
     * 必须开启redis才可以使用redis的缓存配置
     */
    @Bean
    @Conditional(RedisOnEnableCondition.class)
    @ConditionalOnProperty(prefix = "cache.manager", name = "name", havingValue = "redis")
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        //使用fastjson序列化
        RedisSerializerImpl fastJson2RedisSerializer = new RedisSerializerImpl(Object.class);
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(fastJson2RedisSerializer);

        // 默认缓存配置
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(pair);
        //设置默认超过时期
        defaultCacheConfig.entryTtl(Duration.ZERO);
        // allowInFlightCacheCreation: false表示不允许自动创建配置文件中未配置的缓存
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig, false){

            @Override
            protected Collection<RedisCache> loadCaches() {
                Collection<RedisCache> redisCacheList = super.loadCaches();
                try {
                    List<CacheBean> cacheBeanList = resolverCacheXml();
                    for (CacheBean cacheBean : cacheBeanList) {
                        RedisCacheConfiguration cacheConfiguration = defaultCacheConfig;
                        if (StringUtils.isEmpty(cacheBean.getTimeUnit()) || cacheBean.getTtl() <= 0) {
                            cacheConfiguration = defaultCacheConfig.entryTtl(Duration.ZERO);
                        } else if (CacheBean.TIME_UNIT_SECONDS.equals(cacheBean.getTimeUnit())) {
                            cacheConfiguration = defaultCacheConfig.entryTtl(Duration.ofSeconds(cacheBean.getTtl()));
                        } else if (CacheBean.TIME_UNIT_MINUTES.equals(cacheBean.getTimeUnit())) {
                            cacheConfiguration = defaultCacheConfig.entryTtl(Duration.ofMinutes(cacheBean.getTtl()));
                        } else if (CacheBean.TIME_UNIT_HOURS.equals(cacheBean.getTimeUnit())) {
                            cacheConfiguration = defaultCacheConfig.entryTtl(Duration.ofHours(cacheBean.getTtl()));
                        } else if (CacheBean.TIME_UNIT_DAYS.equals(cacheBean.getTimeUnit())) {
                            cacheConfiguration = defaultCacheConfig.entryTtl(Duration.ofDays(cacheBean.getTtl()));
                        }
                        RedisCache redisCache = this.createRedisCache(getCacheName(cacheBean), cacheConfiguration);
                        redisCacheList.add(redisCache);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("缓存配置加载失败：" + e);
                }
                return redisCacheList;
            }
        };
        return cacheManager;
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache.manager", name= "name", havingValue = "ehCache")
    public net.sf.ehcache.CacheManager ehcacheNativeCacheManage(EhCacheProperties ehCacheProperties) {
        net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.create();
        net.sf.ehcache.config.Configuration configuration = cacheManager.getConfiguration();
        configuration.setName(ehCacheProperties.getName());
        // 默认配置
        net.sf.ehcache.config.CacheConfiguration defaultCacheConfiguration = new net.sf.ehcache.config.CacheConfiguration();
        defaultCacheConfiguration.setMaxEntriesLocalHeap(ehCacheProperties.getMaxEntriesLocalHeap());
        defaultCacheConfiguration.setEternal(ehCacheProperties.getEternal());
        defaultCacheConfiguration.setMaxEntriesLocalDisk(ehCacheProperties.getMaxEntriesLocalDisk());
        configuration.defaultCache(defaultCacheConfiguration);
        // 磁盘缓存位置
        net.sf.ehcache.config.DiskStoreConfiguration diskStoreConfiguration = configuration.getDiskStoreConfiguration();
        diskStoreConfiguration.setPath(ehCacheProperties.getDiskPath());
        return cacheManager;
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache.manager", name= "name", havingValue = "ehCache")
    public EhCacheCacheManager ehCacheCacheManager(net.sf.ehcache.CacheManager cacheManager) throws Exception {
        List<CacheBean> cacheBeanList = resolverCacheXml();
        for (CacheBean cacheBean : cacheBeanList) {
            boolean isEternal = cacheBean.getTtl() <= 0;
            int ttl = getRealTtl(cacheBean, isEternal ? 0: cacheBean.getTtl());
            net.sf.ehcache.Cache cache = new net.sf.ehcache.Cache(
                    getCacheName(cacheBean), // 缓存名称
                    cacheBean.getMaxElementsInMemory(), // 缓存最大个数
                    cacheBean.isOverflowToDisk(), // 当内存中对象数量达到maxElementsInMemory时，Ehcache将会对象写到磁盘中
                    isEternal, // 对象是否永久有效，一但设置了timeout将不起作用
                    ttl, // 设置对象在失效前允许存活时间（单位：秒）
                    ttl); // 设置对象在失效前的允许闲置时间（单位：秒）。仅当eternal=false对象不是永久有效时使用，默认值为0
            cacheManager.addCache(cache);
        }
        EhCacheCacheManager cacheCacheManager = new EhCacheCacheManager() {
            @Override
            public CacheManager getCacheManager() {
                return cacheManager;
            }
        };
        return cacheCacheManager;
    }

    /**
     * 获取转换后的时间
     */
    private int getRealTtl(CacheBean cacheBean, int ttl) {
        if (StringUtils.isEmpty(cacheBean.getTimeUnit()) || cacheBean.getTtl() <= 0) {
            ttl = 0;
        } else if (CacheBean.TIME_UNIT_SECONDS.equals(cacheBean.getTimeUnit())) {
            ttl = cacheBean.getTtl();
        } else if (CacheBean.TIME_UNIT_MINUTES.equals(cacheBean.getTimeUnit())) {
            ttl = cacheBean.getTtl() * 60;
        } else if (CacheBean.TIME_UNIT_HOURS.equals(cacheBean.getTimeUnit())) {
            ttl = cacheBean.getTtl() * 60 * 60;
        } else if (CacheBean.TIME_UNIT_DAYS.equals(cacheBean.getTimeUnit())) {
            ttl = cacheBean.getTtl() * 60 * 60 * 24;
        }
        return ttl;
    }

    /**
     * xml配置解析
     * @return
     * @throws Exception
     */
    private List<CacheBean> resolverCacheXml() throws Exception {
        List<CacheBean> cacheBeanList = new ArrayList<>();
        Resource[] resources = ResourceLoaderUtils.getResources(configLocation);
        for(Resource resource : resources) {// 循环所有文件
            Element rootElement = Dom4jUtils.getRootElement(resource.getInputStream());
            List<Element> moduleList = rootElement.elements();
            for (Element element : moduleList) { // 循环所有module节点
                String moduleName = element.attributeValue("name");
                List<Element> cacheElements = element.elements();
                for(Element cacheElement : cacheElements) { // 循环所有module节点下的cache节点
                    CacheBean cacheBean = Dom4jUtils.elementToObject(CacheBean.class, cacheElement);
                    cacheBean.setModuleName(moduleName);
                    cacheBeanList.add(cacheBean);
                }
            }
        }
        addShiroCache(cacheBeanList);
        return cacheBeanList;
    }

    /**
     * 添加shiro配置相关缓存
     * @param cacheBeanList
     */
    private void addShiroCache(List<CacheBean> cacheBeanList) {
        CacheBean cacheBean = new CacheBean();
        cacheBean.setTtl(0);
        cacheBean.setModuleName(ShiroProperties.cacheModuleName);
        cacheBean.setName(ShiroProperties.cacheAuthentication);
        cacheBeanList.add(cacheBean);
        CacheBean shiroSession = new CacheBean();
        shiroSession.setTtl(0);
        shiroSession.setModuleName(ShiroProperties.cacheModuleName);
        shiroSession.setName(ShiroProperties.cacheActiveSession);
        cacheBeanList.add(shiroSession);
    }

    private String getCacheName(CacheBean cacheBean) {
        return new CacheBean.CacheKey(cacheBean.getModuleName(), cacheBean.getName()).get();
    }

}
