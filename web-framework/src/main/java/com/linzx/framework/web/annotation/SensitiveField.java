package com.linzx.framework.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
public @interface SensitiveField {

    /**
     * 敏感字段配置文件中的code字段
     */
    String code() default "";

    /**
     * 当前响应json需要处理的敏感字段名称
     */
    String[] colNames() default {};

}
