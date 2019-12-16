package com.linzx.utils;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * sql脚本工具类
 */
public class SqlUtils {

    /**
     * 仅支持字母、数字、下划线、空格、逗号（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value) {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value)) {
            return StringUtils.EMPTY;
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

    /**
     * 返回order by的sql片段
     *
     * @param map                key-value表示排序字段和排序顺序，例如age:desc
     * @param needConvertToCamel true表示排序属性以驼峰方式命名，false表示排序属性以下划线方式
     * @return
     */
    public static String genSqlOrderBy(LinkedHashMap<String, String> map, boolean needConvertToCamel) {
        if (map.isEmpty()) {
            return "";
        }
        StringBuilder sqlBuilder = new StringBuilder();
        for (String orderField : map.keySet()) {
            String orderDirection = map.get(orderField).toLowerCase();
            if (!"desc".equals(orderDirection) && !"asc".equals(orderDirection)) {
                continue;
            }
            if (needConvertToCamel) { // 下划线转驼峰
                orderField = StringUtils.convertToCamelCase(orderField, false);
            } else { // 驼峰转下划线
                orderField = StringUtils.toUnderScoreCase(orderField);
            }
            sqlBuilder.append(orderField).append(" ").append(orderDirection).append(",");
        }
        String orderSql = sqlBuilder.deleteCharAt(sqlBuilder.length() - 1).toString();
        return escapeOrderBySql(orderSql);
    }

    /**
     * 给更新sql语句增加条件增加版本号（乐观锁）
     * @param originalSqlStat 原始sql
     * @param colName 列名称
     * @param version 列值
     */
    public static <T> String addVersionLockerToSql(Update originalSqlStat, String colName, T version) {
        try{
            // 判断update语句中是否存在版本号列
            boolean existVersionCol = false;
            List<Column> columns = originalSqlStat.getColumns();
            for(Column column : columns){
                if(column.getColumnName().equalsIgnoreCase(colName)){
                    existVersionCol = true;
                    break;
                }
            }
            // 如果不存在版本号列
            if(!existVersionCol){
                Column versionColumn = new Column();
                versionColumn.setColumnName(colName);
                columns.add(versionColumn);
                List<Expression> expressions = originalSqlStat.getExpressions();
                Addition add = new Addition();
                add.setLeftExpression(versionColumn);
                add.setRightExpression(new LongValue(1));
                expressions.add(add);
            }
            Expression where = originalSqlStat.getWhere();
            EqualsTo equal = new EqualsTo();
            Column column = new Column();
            column.setColumnName(colName);
            equal.setLeftExpression(column);
            LongValue val = new LongValue(Convert.toLong(version));
            equal.setRightExpression(val);
            if(where != null){
                AndExpression and = new AndExpression(where, equal);
                originalSqlStat.setWhere(and);
            }else{
                originalSqlStat.setWhere(equal);
            }
            return originalSqlStat.toString();
        }catch(Exception e){
            e.printStackTrace();
            return originalSqlStat.toString();
        }
    }

    public static void main(String[] args) {
        LinkedHashMap<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("field1A", "desc");
        orderMap.put("field2A", "asc");
        System.out.println(genSqlOrderBy(orderMap, false));
    }

}
