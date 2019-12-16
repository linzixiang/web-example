package com.linzx.framework.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * excel导出
 */
public class ExcelExportBean {

    /** 是否显示序列号 **/
    private boolean showSerialNumber;

    /** 导出表名称 **/
    private String titleName;

    /** 表内容 **/
    private List<ExcelExportContent> content;

    /** 表所有列属性 **/
    private Set<String> fieldSet = new HashSet<>();

    public boolean isShowSerialNumber() {
        return showSerialNumber;
    }

    public void setShowSerialNumber(boolean showSerialNumber) {
        this.showSerialNumber = showSerialNumber;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public List<ExcelExportContent> getContent() {
        return content;
    }

    public void setContent(List<ExcelExportContent> content) {
        this.content = content;
    }

    public void addField(String field) {
        fieldSet.add(field);
    }

    /**
     * 判断表是否包含属性
     */
    public boolean isIncludeField(String fieldName) {
        return fieldSet.contains(fieldName);
    }
}
