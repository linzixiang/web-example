package com.linzx.framework.bean;

import java.util.HashMap;
import java.util.Map;

public class CodeBean {

    public static final String UNIQUE_KEYNAME = "keyName";

    /** 返回结果的name属性**/
    private String nameField;

    /** 返回结果的id属性**/
    private String valueField;

    /** 查询表**/
    private String tableName;

    /** where查询条件的固定扩展*/
    private String whereExt;

    /** 搜索关键字的数据库属性 **/
    private String likeField;

    /** 排序，例如 name desc **/
    private String orderBy;

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public String getWhereExt() {
        return whereExt;
    }

    public void setWhereExt(String whereExt) {
        this.whereExt = whereExt;
    }

    public String getLikeField() {
        return likeField;
    }

    public void setLikeField(String likeField) {
        this.likeField = likeField;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
