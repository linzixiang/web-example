package com.linzx.framework.mapper;

import java.util.Map;

/**
 * 单表的简单统计
 */
public interface SimpleCountMapper {

    Long countSimple(Map<String, Object> params);

}
