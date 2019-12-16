package com.linzx.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtils {

    /**
     * 创建数据源
     */
    public static DataSource createDataSource(String jdbcUrl, String username, String pwd, Map<String, String> properties) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        if (properties == null ) {
            properties = new HashMap<>();
        }
        properties.put("url", jdbcUrl);
        properties.put("username", username);
        properties.put("password", pwd);
        properties.put("driverClassName", "com.mysql.jdbc.Driver");
        return DruidDataSourceFactory.createDataSource(properties);
    }

    /**
     * 从数据源中获取连接
     */
    public static Connection getConnection(DataSource dataSource) throws SQLException {
        return  dataSource.getConnection();
    }

    /**
     * 开启事务
     */
    public static void beginTransaction(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
    }

    /**
     * 提交事务
     */
    public static void commitTransaction(Connection connection) throws SQLException {
        connection.commit();
        releaseAll(connection);
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction(Connection connection) throws SQLException {
        connection.rollback();
        releaseAll(connection);
    }

    /**
     * 关闭连接
     */
    public static void releaseAll(AutoCloseable... autoCloseableArr) throws SQLException {
        for (AutoCloseable autoCloseable : autoCloseableArr) {
            if (autoCloseable instanceof Connection) {
                Connection connection = (Connection) autoCloseable;
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
            if (autoCloseable instanceof ResultSet) {
                ResultSet resultSet = (ResultSet) autoCloseable;
                if (resultSet != null && !resultSet.isClosed()) {
                    resultSet.close();
                }
            }
            if (autoCloseable instanceof PreparedStatement) {
                PreparedStatement pstmt = (PreparedStatement) autoCloseable;
                if (pstmt != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            }
        }
    }

    /**
     * 查询单条记录
     */
    public static Map<String, Object> get(String sql, List<Object> params, Connection connection) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        int index  = 1;
        if(params != null && !params.isEmpty()){
            for(int i=0; i<params.size(); i++){
                pstmt.setObject(index++, params.get(i));
            }
        }
        ResultSet resultSet = pstmt.executeQuery();//返回查询结果
        ResultSetMetaData metaData = resultSet.getMetaData();
        int col_len = metaData.getColumnCount();
        while(resultSet.next()){
            for(int i=0; i<col_len; i++ ){
                String colName = metaData.getColumnName(i+1);
                Object colValue = resultSet.getObject(colName);
                map.put(colName, colValue);
            }
        }
        releaseAll(pstmt, resultSet);
        return map;
    }

    /**
     * 查询多条记录
     */
    public static List<Map<String, Object>> find(String sql, List<Object> params, Connection connection) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        int index = 1;
        PreparedStatement pstmt = connection.prepareStatement(sql);
        if(params != null && !params.isEmpty()){
            for(int i = 0; i<params.size(); i++){
                pstmt.setObject(index++, params.get(i));
            }
        }
        ResultSet resultSet = pstmt.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols_len = metaData.getColumnCount();
        while(resultSet.next()){
            Map<String, Object> map = new HashMap<String, Object>();
            for(int i=0; i<cols_len; i++){
                String colName = metaData.getColumnName(i+1);
                Object colValue = resultSet.getObject(colName);
                map.put(colName, colValue);
            }
            list.add(map);
        }
        releaseAll(pstmt, resultSet);
        return list;
    }

    public static void main(String[] args) {
        try {
            DataSource dataSource = createDataSource("jdbc:mysql://139.196.72.210:3306/web_example?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8",
                    "root", "123456", null);
            Connection connection = getConnection(dataSource);
            String sql = "select * from sys_dict where dict_id = ?";
            List<Object> params = new ArrayList<>();
            params.add("1");
            Map<String, Object> map = get(sql, params, connection);
            int i = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
