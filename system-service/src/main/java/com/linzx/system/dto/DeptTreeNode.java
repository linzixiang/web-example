package com.linzx.system.dto;

import java.util.ArrayList;
import java.util.List;

public class DeptTreeNode {

    private Long checkId; // checked=true时

    private Long id;

    private String name;

    private String url;

    private Boolean checked = false; // 是否为选中状态

    private List<DeptTreeNode> children = new ArrayList<>();

    private Boolean hasChildren;

    public Long getCheckId() {
        return checkId;
    }

    public void setCheckId(Long checkId) {
        this.checkId = checkId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public List<DeptTreeNode> getChildren() {
        if (this.children == null) {
            return new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<DeptTreeNode> children) {
        this.children = children;
    }

    public Boolean getHasChildren() {
        return this.getChildren().size() > 0;
    }

//    public void setHasChildren(Boolean hasChildren) {
//        this.hasChildren = hasChildren;
//    }
}
