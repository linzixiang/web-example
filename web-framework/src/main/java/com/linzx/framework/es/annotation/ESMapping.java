package com.linzx.framework.es.annotation;

import com.linzx.framework.es.bean.Analyzer;
import com.linzx.framework.es.bean.DataType;

import java.lang.annotation.*;

/**
 * @description: 对应索引结构mapping的注解，在es entity field上添加
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ESMapping {
    /**
     * 数据类型（包含 关键字类型）
     */
    DataType datatype() default DataType.text_type;
    /**
     * 间接关键字
     */
    boolean keyword() default true;
    /**
     * 关键字忽略字数
     */
    int ignoreAbove() default 256;
    /**
     * 是否支持autocomplete，高效全文搜索提示
     */
    boolean autocomplete() default false;
    /**
     * 是否支持suggest，高效前缀搜索提示
     */
    boolean suggest() default false;
    /**
     * 索引分词器设置（研究类型）
     */
    Analyzer analyzer() default Analyzer.standard;
    /**
     * 搜索内容分词器设置
     */
    Analyzer searchAnalyzer() default Analyzer.standard;

    /**
     * 是否允许被搜索
     */
    boolean allowSearch() default true;

    /**
     * 拷贝到哪个字段，代替_all
     */
    String copyTo() default "";
}
