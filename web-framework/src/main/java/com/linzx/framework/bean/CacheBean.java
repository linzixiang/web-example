package com.linzx.framework.bean;

/**
 * 缓存配置
 */
public class CacheBean {

    public static final String TIME_UNIT_SECONDS = "seconds";
    public static final String TIME_UNIT_MINUTES = "minutes";
    public static final String TIME_UNIT_HOURS = "hours";
    public static final String TIME_UNIT_DAYS = "days";

    /** 属于哪个模块 **/
    private String moduleName;

    /** 缓存命名**/
    private String name;

    /** 存活时间，0表示长期有效 **/
    private int ttl;

    /** 存活时间单位：seconds，minutes，hours，days **/
    private String timeUnit;

    /**  如果内存中数据超过内存限制，是否要缓存到磁盘上 **/
    private boolean overflowToDisk;

    /** 在内存中缓存的element的最大数目 **/
    private int maxElementsInMemory;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public boolean isOverflowToDisk() {
        return overflowToDisk;
    }

    public void setOverflowToDisk(boolean overflowToDisk) {
        this.overflowToDisk = overflowToDisk;
    }

    public int getMaxElementsInMemory() {
        return maxElementsInMemory;
    }

    public void setMaxElementsInMemory(int maxElementsInMemory) {
        this.maxElementsInMemory = maxElementsInMemory;
    }

    public static class CacheKey {

        private String moduleName;

        private String keyName;

        public CacheKey(String moduleName, String keyName) {
            this.moduleName = moduleName;
            this.keyName = keyName;
        }

        public String get() {
            return moduleName + "::" + keyName;
        }
    }
}
