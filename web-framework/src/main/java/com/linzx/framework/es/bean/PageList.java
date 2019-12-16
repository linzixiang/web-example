package com.linzx.framework.es.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 分页对象封装
 */
@Setter
@Getter
public class PageList<T> {

    private List<T> list;

    private int totalPages = 0;

    private long totalElements = 0;

}
