package com.linzx.framework.bean;

/**
 * 字典bean
 */
public class DictBean {

    public static final String UNIQUE_KEYNAME = "queryCode";

    /** 字典唯一编码*/
    private String dictCode;

    /** 字典where条件扩展 **/
    private String whereExt;

    /** 字典是否包含停用的选项**/
    private String excludeStop;

    /** 字典是否包含删除的选项 **/
    private String excludeDelete;

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getWhereExt() {
        return whereExt;
    }

    public void setWhereExt(String whereExt) {
        this.whereExt = whereExt;
    }

    public String getExcludeStop() {
        return excludeStop;
    }

    public void setExcludeStop(String excludeStop) {
        this.excludeStop = excludeStop;
    }

    public String getExcludeDelete() {
        return excludeDelete;
    }

    public void setExcludeDelete(String excludeDelete) {
        this.excludeDelete = excludeDelete;
    }
}
