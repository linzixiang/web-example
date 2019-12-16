package com.linzx.framework.utils;

import com.linzx.common.constant.Constants;
import com.linzx.utils.StringUtils;

public class PageHelper {

    public static void startPage() {
        startPage(false);
    }

    /**
     * 开始分页
     * @param isNameByCamel
     */
    public static void startPage(boolean isNameByCamel) {
        Integer pageNum = ServletUtils.getParameterToInt(Constants.PAGE_NUM, 1);
        Integer pageSize = ServletUtils.getParameterToInt(Constants.PAGE_SIZE, Constants.PAGE_SIZE_DEFAULT);
        String paramOrderCol = ServletUtils.getParameter(Constants.ORDER_BY_COLUMN);
        // 如果排序字段不是驼峰命名方式，则需要转为下划线
        if (StringUtils.isNotEmpty(paramOrderCol) && !isNameByCamel) {
            paramOrderCol = StringUtils.toUnderScoreCase(paramOrderCol);
        }
        if (StringUtils.isNotEmpty(paramOrderCol)) {
            String orderDirection = ServletUtils.getParameter(Constants.IS_ASC, Constants.ORDER_DIRECTION_DEFAULT);
            com.linzx.framework.core.mybatis.pagehelper.PageHelper.startPage(pageNum, pageSize, paramOrderCol + " " + orderDirection);
//            com.github.pagehelper.PageHelper.startPage(pageNum, pageSize, paramOrderCol + " " + orderDirection);
        } else {
            com.linzx.framework.core.mybatis.pagehelper.PageHelper.startPage(pageNum, pageSize, null);
        }
    }
}
