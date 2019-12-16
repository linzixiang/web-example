package com.linzx.generator.util;

import com.linzx.common.constant.Constants;
import com.linzx.generator.config.GenConfig;
import com.linzx.generator.domain.ColumnInfo;
import com.linzx.generator.domain.TableInfo;
import com.linzx.utils.DateUtils;
import com.linzx.utils.StringUtils;
import org.apache.velocity.VelocityContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器 工具类
 */
public class GenUtils {
    /**
     * 项目空间路径
     */
    private static final String PROJECT_PATH = getProjectPath();

    /**
     * mybatis空间路径
     */
    private static final String MYBATIS_PATH = "main/resources/mapper";

    /**
     * html空间路径
     */
    private static final String TEMPLATES_PATH = "main/resources/templates";

    /**
     * 类型转换
     */
    public static Map<String, String> javaTypeMap = new HashMap<String, String>();

    /**
     * 设置列信息
     */
    public static List<ColumnInfo> transColums(List<ColumnInfo> columns) {
        // 列信息
        List<ColumnInfo> columsList = new ArrayList<>();
        for (ColumnInfo column : columns) {
            // 列名转换成Java属性名
            String attrName = StringUtils.convertToCamelCase(column.getColumnName(), true);
            column.setAttrName(attrName);
            column.setAttrname(StringUtils.uncapitalize(attrName));
            column.setExtra(column.getExtra());

            // 列的数据类型，转换成Java类型
            String attrType = javaTypeMap.get(column.getDataType());
            column.setAttrType(attrType);

            columsList.add(column);
        }
        return columsList;
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static VelocityContext getVelocityContext(TableInfo table) {
        // java对象数据传递到模板文件vm
        VelocityContext velocityContext = new VelocityContext();
        String packageName = GenConfig.getPackageName();
        velocityContext.put("tableName", table.getTableName());
        velocityContext.put("tableComment", replaceKeyword(table.getTableComment()));
        velocityContext.put("primaryKey", table.getPrimaryKey());
        velocityContext.put("className", table.getClassName());
        velocityContext.put("classname", table.getClassname());
        velocityContext.put("moduleName", getModuleName(packageName));
        velocityContext.put("columns", table.getColumns());
        velocityContext.put("basePackage", getBasePackage(packageName));
        velocityContext.put("package", packageName);
        velocityContext.put("author", GenConfig.getAuthor());
        velocityContext.put("datetime", DateUtils.getDate());
        return velocityContext;
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("vm/java/domain.java.vm");
        templates.add("vm/java/Mapper.java.vm");
        templates.add("vm/java/Service.java.vm");
        templates.add("vm/java/ServiceImpl.java.vm");
        templates.add("vm/java/Controller.java.vm");
        templates.add("vm/xml/Mapper.xml.vm");
        templates.add("vm/html/list.html.vm");
        templates.add("vm/html/add.html.vm");
        templates.add("vm/html/edit.html.vm");
        templates.add("vm/sql/sql.vm");
        return templates;
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName) {
        String autoRemovePre = GenConfig.getAutoRemovePre();
        String tablePrefix = GenConfig.getTablePrefix();
        if (Constants.BOOLEAN_TRUE.equals(autoRemovePre) && StringUtils.isNotEmpty(tablePrefix)) {
            tableName = tableName.replaceFirst(tablePrefix, "");
        }
        return StringUtils.convertToCamelCase(tableName, true);
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, TableInfo table, String moduleName) {
        // 小写类名
        String classname = table.getClassname();
        // 大写类名
        String className = table.getClassName();
        String javaPath = PROJECT_PATH;
        String mybatisPath = MYBATIS_PATH + "/" + moduleName + "/" + className;
        String htmlPath = TEMPLATES_PATH + "/" + moduleName + "/" + classname;

        if (template.contains("domain.java.vm")) {
            return javaPath + "domain" + "/" + className + ".java";
        }

        if (template.contains("Mapper.java.vm")) {
            return javaPath + "mapper" + "/" + className + "Mapper.java";
        }

        if (template.contains("Service.java.vm")) {
            return javaPath + "service" + "/I" + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return javaPath + "service" + "/impl/" + className + "Service.java";
        }

        if (template.contains("Controller.java.vm")) {
            return javaPath + "controller" + "/" + className + "Controller.java";
        }

        if (template.contains("Mapper.xml.vm")) {
            return mybatisPath + "Mapper.xml";
        }

        if (template.contains("list.html.vm")) {
            return htmlPath + "/" + "list.html";
        }
        if (template.contains("add.html.vm")) {
            return htmlPath + "/" + "add.html";
        }
        if (template.contains("edit.html.vm")) {
            return htmlPath + "/" + "edit.html";
        }
        if (template.contains("sql.vm")) {
            return classname + "Menu.sql";
        }
        return null;
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        int nameLength = packageName.length();
        String moduleName = StringUtils.substring(packageName, lastIndex + 1, nameLength);
        return moduleName;
    }

    public static String getBasePackage(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        String basePackage = StringUtils.substring(packageName, 0, lastIndex);
        return basePackage;
    }

    public static String getProjectPath() {
        String packageName = GenConfig.getPackageName();
        StringBuffer projectPath = new StringBuffer();
        projectPath.append("main/java/");
        projectPath.append(packageName.replace(".", "/"));
        projectPath.append("/");
        return projectPath.toString();
    }

    public static String replaceKeyword(String keyword) {
        String keyName = keyword.replaceAll("(?:表|信息|管理)", "");
        return keyName;
    }

    static {
        javaTypeMap.put("tinyint", "java.lang.Integer");
        javaTypeMap.put("smallint", "java.lang.Integer");
        javaTypeMap.put("mediumint", "java.lang.Integer");
        javaTypeMap.put("int", "java.lang.Integer");
        javaTypeMap.put("number", "java.lang.Integer");
        javaTypeMap.put("integer", "java.lang.Integer");
        javaTypeMap.put("bigint", "java.lang.Long");
        javaTypeMap.put("float", "java.lang.Float");
        javaTypeMap.put("double", "java.lang.Double");
        javaTypeMap.put("decimal", "java.math.BigDecimal");
        javaTypeMap.put("bit", "java.lang.Boolean");
        javaTypeMap.put("char", "java.lang.String");
        javaTypeMap.put("varchar", "java.lang.String");
        javaTypeMap.put("varchar2", "java.lang.String");
        javaTypeMap.put("tinytext", "java.lang.String");
        javaTypeMap.put("text", "java.lang.String");
        javaTypeMap.put("mediumtext", "java.lang.String");
        javaTypeMap.put("longtext", "java.lang.String");
        javaTypeMap.put("time", "java.util.Date");
        javaTypeMap.put("date", "java.util.Date");
        javaTypeMap.put("datetime", "java.util.Date");
        javaTypeMap.put("timestamp", "java.util.Date");
    }
}
