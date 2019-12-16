package com.linzx.framework.config.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn({"globalProperties"})
public class MybatisProperties implements InitializingBean {

    /** 乐观锁数据库的列名 **/
    public static String versionColumn;

    /*** 乐观锁java字段名 **/
    public static String versionField;

    /** pagehelp分页器 **/
    public static String helperDialect;
    public static String helperReasonable;
    public static String helperSupportMethodsArguments;
    public static String helperParams;
    public static String helperPageSizeZero;

    @Override
    public void afterPropertiesSet() throws Exception {
        versionColumn = GlobalProperties.getConfigStr("mybatis.version-locker.column", "revision");
        versionField = GlobalProperties.getConfigStr("mybatis.version-locker.field", "revision");

        helperDialect = GlobalProperties.getConfigStr("pagehelper.helper-dialect", "mysql");
        helperReasonable = GlobalProperties.getConfigStr("pagehelper.reasonable", "false");
        helperSupportMethodsArguments = GlobalProperties.getConfigStr("pagehelper.support-methods-arguments", "false");
        helperParams = GlobalProperties.getConfigStr("pagehelper.params", "count=countSql");
        helperPageSizeZero = GlobalProperties.getConfigStr("pagehelper.page-size-zero", "count=countSql");
    }
}
