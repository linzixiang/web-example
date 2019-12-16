package com.linzx.framework.es.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MetaData {

    private String indexName;

    private String indexType;

    private int numberOfShards;

    private int numberOfReplicas;

    private boolean printLog = false;

}
