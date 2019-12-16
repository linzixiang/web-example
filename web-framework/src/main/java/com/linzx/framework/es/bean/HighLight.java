package com.linzx.framework.es.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 高亮对象封装
 */
@Setter
@Getter
public class HighLight {

    private String preTag;
    private String postTag;
    private List<String> highLightList = new ArrayList<>();;



}
