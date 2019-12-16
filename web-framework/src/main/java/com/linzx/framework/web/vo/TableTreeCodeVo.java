package com.linzx.framework.web.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 树形表结构
 */
public class TableTreeCodeVo implements Serializable {

    private String id;

    private String name;

    private String pid;

    private List<TableTreeCodeVo> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<TableTreeCodeVo> getChildren() {
        return children;
    }

    public void setChildren(List<TableTreeCodeVo> children) {
        this.children = children;
    }
}
