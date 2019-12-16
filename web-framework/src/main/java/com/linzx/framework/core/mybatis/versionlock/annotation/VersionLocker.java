package com.linzx.framework.core.mybatis.versionlock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface VersionLocker {

    /** true表示是否开启乐观锁机制 */
    boolean value() default true;

    /** 是否必须原始版本号，false表示允许没有原始版本号（即有原始版本号才启用乐观锁机制） **/
    boolean originVersionMust() default true;

}
