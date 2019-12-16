package com.linzx.framework.es.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: 分页+高亮对象封装
 */
@Getter
@Setter
public class PageSortHighLight {

    private int currentPage;
    private int pageSize;
    private Sort sort = new Sort();
    private HighLight highLight = new HighLight();

    public PageSortHighLight(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public PageSortHighLight(int currentPage, int pageSize, Sort sort) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.sort = sort;
    }

}
