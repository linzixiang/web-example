package com.linzx.framework.web;

import com.linzx.framework.bean.ExcelExportBean;
import com.linzx.framework.bean.ExcelExportContent;
import com.linzx.framework.core.context.ContextManager;
import com.linzx.framework.web.support.excel.ExcelCellWriteCallBack;
import com.linzx.utils.*;
import com.linzx.utils.excel.ExcelUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * web层通用数据处理
 */
public abstract class BaseController {

    /**
     * 页面重定向
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }


    /**
     * 导出数据下载excel
     */
    protected <T> void downloadExcel(List<T> objList,
                                     String excelConfigName, String[] exportColumns,
                                     HttpServletResponse response, HttpServletRequest request) throws IOException {
        ExcelExportBean excelExportBean = ContextManager.getExcelExportBean(excelConfigName);
        List<ExcelExportContent> contentList = excelExportBean.getContent();
        List<String> header = new ArrayList<>(); // excel表头名称
        Set<String> headerFields = new LinkedHashSet<>(); // excel表头属性
        List<List<Object>> data = new ArrayList<>(); // excel导出的数据
        List<ExcelExportContent> contentExportList = new ArrayList<>();
        for (ExcelExportContent content : contentList) {
            // 如果导出的字段不在客户端上传的exportColumns集合中，则不导出该列(不包括序号列)
            if (!ExcelExportContent.SERIAL_NUMBER.equals(content.getField()) && !StringUtils.inStringIgnoreCase(content.getField(), exportColumns)) {
                continue;
            }
            contentExportList.add(content);
            header.add(content.getName());
            headerFields.add(content.getField());
        }
        for (int index = 0; index < objList.size(); index++) {
            List<Object> dataRow = new ArrayList<>();
            T obj = objList.get(index);

            for (String field : headerFields) {
                // 显示序号
                if (ExcelExportContent.SERIAL_NUMBER.equals(field)) {
                    dataRow.add(index + 1);
                } else {
                    Object value = ReflectionUtils.getFieldValue(obj, obj.getClass(), field);
                    if (value instanceof Date) {
                        value = DateUtils.parseDateToStr(DateUtils.DateFormatStrEnum.YYYY_MM_DD_HH_MM_SS, (Date) value);
                    }
                    dataRow.add(value);
                }
            }
            data.add(dataRow);
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String fileName = excelExportBean.getTitleName();
        if (StringUtils.isEmpty(fileName)) {
            fileName = "未知文件";
        }
        String userAgent = request.getHeader("User-Agent");
        // 针对IE或者以IE为内核的浏览器：
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("Content-Disposition","attachment;fileName=" + fileName + ".xlsx");
        ExcelUtils.writeExcelWithObjListBySimplyHeader(response.getOutputStream(), data,
                excelExportBean.getTitleName(), header,
                new ExcelCellWriteCallBack(contentExportList, 1));
    }
}
