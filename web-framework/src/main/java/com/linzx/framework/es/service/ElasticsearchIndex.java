package com.linzx.framework.es.service;

/**
 * @description: 索引结构基础方法接口
 * @param <T>
 */
public interface ElasticsearchIndex<T> {

    /**
     * 创建索引
     * @param clazz
     * @throws Exception
     */
    void createIndex(Class<T> clazz) throws Exception;
    /**
     * 删除索引
     * @param clazz
     * @throws Exception
     */
    void dropIndex(Class<T> clazz) throws Exception;
    /**
     * 索引是否存在
     * @param clazz
     * @throws Exception
     */
    boolean exists(Class<T> clazz) throws Exception;

}
