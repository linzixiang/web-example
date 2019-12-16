package com.linzx.framework.bean;

public class ExcelExportContent implements Comparable<ExcelExportContent> {

    public static final String  FORMAT_TYPE_CONVERT = "convert";
    public static final String  FORMAT_TYPE_DICT = "dict";

    public static final String  SERIAL_NUMBER = "serialNumber";

    /** 列顺序，越小越靠前 */
    private Integer index;

    /** 列名称 **/
    private String name;

    /** 属性 **/
    private String field;

    /** 对属性值进行格式化，可以是:convert，dict **/
    private String formatType;

    /** formatType值为dict，翻译字典 **/
    private String dictCode;

    /** formatType值为convert，格式化  **/
    private String formatter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public int compareTo(ExcelExportContent content) {
        if (this.getIndex() != null && content.getIndex() != null) {
            return this.getIndex().compareTo(content.getIndex());
        }
        return 0;
    }
}
