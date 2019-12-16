package com.linzx.framework.core.mybatis.versionlock.interceptor;

import com.linzx.framework.core.mybatis.versionlock.annotation.VersionLocker;
import com.linzx.framework.core.mybatis.versionlock.cache.Cache;
import com.linzx.framework.core.mybatis.versionlock.cache.LocalVersionLockerCache;
import com.linzx.framework.utils.PluginUtil;
import com.linzx.utils.SqlUtils;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

//@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class OptimisticLocker implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(OptimisticLocker.class);

    private static VersionLocker trueLocker;
    private static VersionLocker falseLocker;

    static {
        try {
            trueLocker = OptimisticLocker.class.getDeclaredMethod("versionValueTrue").getAnnotation(VersionLocker.class);
            falseLocker = OptimisticLocker.class.getDeclaredMethod("versionValueFalse").getAnnotation(VersionLocker.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("The plugin init faild.", e);
        }
    }

    private Properties props = null;
    private LocalVersionLockerCache versionLockerCache = new LocalVersionLockerCache();

    @VersionLocker(true)
    private void versionValueTrue() {
    }

    @VersionLocker(false)
    private void versionValueFalse() {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String versionColumn = props.getProperty("versionColumn", "revision");
        String versionField = props.getProperty("versionField", "revision");
        String interceptMethod = invocation.getMethod().getName();
        if (!"prepare".equals(interceptMethod)) {
            return invocation.proceed();
        }
        StatementHandler handler = (StatementHandler) PluginUtil.processTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(handler);
        MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        SqlCommandType sqlCmdType = ms.getSqlCommandType();
        if (sqlCmdType != SqlCommandType.UPDATE) {
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        VersionLocker vl = getVersionLocker(ms, boundSql);
        // 默认不用乐观锁，只有加了注解才用乐观锁
        if (vl == null || !vl.value()) {
            return invocation.proceed();
        }
        Object originalVersion = metaObject.getValue("delegate.boundSql.parameterObject." + versionField);
        // 如果加了乐观锁注解，更新的sql条件中没有原始版本号，则不启动乐观锁更新机制
        if (originalVersion == null && !vl.originVersionMust()) {
            return invocation.proceed();
        }
        if (originalVersion == null || Long.parseLong(originalVersion.toString()) < 0) {
            throw new BindingException("value of version field[" + versionField + "]can not be empty");
        }
        String originalSql = boundSql.getSql();
        if (logger.isDebugEnabled()) {
            logger.debug("==> originalSql: " + originalSql);
        }
        Statement stmt = CCJSqlParserUtil.parse(originalSql);
        if (!(stmt instanceof Update)) {
            return originalSql;
        }
        originalSql = SqlUtils.addVersionLockerToSql((Update) stmt, versionColumn, originalVersion);
        metaObject.setValue("delegate.boundSql.sql", originalSql);
        if (logger.isDebugEnabled()) {
            logger.debug("==> originalSql after add version: " + originalSql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler || target instanceof ParameterHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    private VersionLocker getVersionLocker(MappedStatement ms, BoundSql boundSql) {

        Class<?>[] paramCls = null;
        Object paramObj = boundSql.getParameterObject();

        /******************下面处理参数只能按照下面3个的顺序***********************/
        /******************Process param must order by below ***********************/
        // 1、处理@Param标记的参数
        // 1、Process @Param param
        if (paramObj instanceof MapperMethod.ParamMap<?>) {
            MapperMethod.ParamMap<?> mmp = (MapperMethod.ParamMap<?>) paramObj;
            if (null != mmp && !mmp.isEmpty()) {
                paramCls = new Class<?>[mmp.size() / 2];
                int mmpLen = mmp.size() / 2;
                for (int i = 0; i < mmpLen; i++) {
                    Object index = mmp.get("param" + (i + 1));
                    paramCls[i] = index.getClass();
                    if (List.class.isAssignableFrom(paramCls[i])) {
                        return falseLocker;
                    }
                }
            }
            // 2、处理Map类型参数
            // 2、Process Map param
        } else if (paramObj instanceof Map) {//不支持批量
            @SuppressWarnings("rawtypes")
            Map map = (Map) paramObj;
            if (map.get("list") != null || map.get("array") != null) {
                return falseLocker;
            } else {
                paramCls = new Class<?>[]{Map.class};
            }
            // 3、处理POJO实体对象类型的参数
            // 3、Process POJO entity param
        } else {
            paramCls = new Class<?>[]{paramObj.getClass()};
        }

        Cache.MethodSignature vm = new Cache.MethodSignature(ms.getId(), paramCls);
        VersionLocker versionLocker = versionLockerCache.getVersionLocker(vm);
        if (null != versionLocker) {
            return versionLocker;
        }

        Class<?> mapper = getMapper(ms);
        if (mapper != null) {
            Method m;
            try {
                m = mapper.getDeclaredMethod(getMapperShortId(ms), paramCls);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException("The Map type param error." + e, e);
            }
            versionLocker = m.getAnnotation(VersionLocker.class);
            if (versionLocker == null) {
                versionLocker = falseLocker;
            }
            if (!versionLockerCache.containMethodSignature(vm)) {
                versionLockerCache.cacheMethod(vm, versionLocker);
            }
            return versionLocker;
        } else {
            throw new RuntimeException("Config info error, maybe you have not config the Mapper interface");
        }
    }

    private Class<?> getMapper(MappedStatement ms) {
        String namespace = getMapperNamespace(ms);
        Collection<Class<?>> mappers = ms.getConfiguration().getMapperRegistry().getMappers();
        for (Class<?> clazz : mappers) {
            if (clazz.getName().equals(namespace)) {
                return clazz;
            }
        }
        return null;
    }

    private String getMapperNamespace(MappedStatement ms) {
        String id = ms.getId();
        int pos = id.lastIndexOf(".");
        return id.substring(0, pos);
    }

    private String getMapperShortId(MappedStatement ms) {
        String id = ms.getId();
        int pos = id.lastIndexOf(".");
        return id.substring(pos + 1);
    }

    @Override
    public void setProperties(Properties properties) {
        if (null != properties && !properties.isEmpty()) {
            props = properties;
        }
    }

}
