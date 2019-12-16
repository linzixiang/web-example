package com.linzx.framework.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "cache.manager.ehcache")
public class EhCacheProperties {

    /** Cache 的唯一标识名称 **/
    public String name = "unknow";

    /** 磁盘缓存位置 **/
    public String diskPath = "java.io.tmpdir";

    /** 是否永久有效， 如果为true则timeout失效 **/
    public Boolean eternal = true;

    /** 堆内存中最大缓存对象数 **/
    public Long maxEntriesLocalHeap = new Long(1000);

    /*** 磁盘中的最大对象数，默认为 0 不限制 **/
    public Long maxEntriesLocalDisk = new Long(10000);

}
