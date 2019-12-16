package com.linzx.framework.es.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: mapping注解对应的数据载体类
 */
@Setter
@Getter
public class MappingData {

    private String fieldName;
    /**
     * 数据类型（包含 关键字类型）
     * @return
     */
    private String dataType;
    /**
     * 间接关键字
     * @return
     */
    private boolean keyword;

    /**
     * 关键字忽略字数
     * @return
     */
    private int ignoreAbove;
    /**
     * 是否支持autocomplete，高效全文搜索提示
     * @return
     */
    private boolean autocomplete;
    /**
     * 是否支持suggest，高效前缀搜索提示
     * @return
     */
    private boolean suggest;
    /**
     * 索引分词器设置
     * @return
     */
    private String analyzer;
    /**
     * 搜索内容分词器设置
     * @return
     */
    private String searchAnalyzer;

    private boolean allowSearch;

    private String copyTo;

}
