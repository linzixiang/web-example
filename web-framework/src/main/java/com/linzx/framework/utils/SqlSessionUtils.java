package com.linzx.framework.utils;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class SqlSessionUtils {

    private static SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    public SqlSessionUtils(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public static Connection getConnection() {
        return sqlSessionTemplate.getSqlSessionFactory().openSession().getConnection();
    }

}
