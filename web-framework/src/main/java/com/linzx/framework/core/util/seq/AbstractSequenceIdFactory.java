package com.linzx.framework.core.util.seq;

public abstract class AbstractSequenceIdFactory {

    /**
     * 获取redis有序自增id
     * @param sequenceName 序列唯一key
     * @param start 序列开始值，当sequenceName不存在时有效
     * @param step 自增步长，当sequenceName存在时生效
     * @return
     */
    public abstract Long getRedisNextSeqValue(String sequenceName, int start, int step) throws Exception;

    /**
     * 利用数据库主键自增原理，获取有序自增id
     * @param sequenceName 序列唯一key
     * @param start 序列开始值，当sequenceName不存在时有效
     * @return
     */
    public abstract Long getDbNextSeqValue(String sequenceName, int start) throws Exception;

    /**
     * 根据Snowflake规则，获取有序id
     * @param sequenceName 序列唯一key
     * @param workerId 工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     * @return
     */
    public abstract  Long getSnowflakeNextSeqValue(String sequenceName, long workerId, long datacenterId) throws Exception;

    /**
     * 获取UUID无序id
     * @param sequenceName 序列唯一key
     * @return
     */
    public abstract  String getUUIDNextSeqValue(String sequenceName) throws Exception;

}
