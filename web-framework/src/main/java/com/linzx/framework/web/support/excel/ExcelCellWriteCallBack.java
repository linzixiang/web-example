package com.linzx.framework.web.support.excel;

import com.linzx.framework.bean.ExcelExportContent;
import com.linzx.framework.utils.SpringUtils;
import com.linzx.framework.web.service.impl.CommonService;
import com.linzx.framework.web.vo.DictOptionVo;
import com.linzx.utils.Convert;
import com.linzx.utils.ReflectionUtils;
import com.linzx.utils.StringUtils;
import com.linzx.utils.excel.ExcelCellWriteListener;

import java.util.List;

public class ExcelCellWriteCallBack implements ExcelCellWriteListener {

    private List<ExcelExportContent> contentList;

    private int headerRowNum;

    private CommonService commonService;

    public ExcelCellWriteCallBack(List<ExcelExportContent> contentList, int headerRowNum) {
        this.contentList = contentList;
        this.headerRowNum = headerRowNum;
        this.commonService = SpringUtils.getBean("commonService");
    }

    @Override
    public String onCellWrite(int sheetNo, int rowNum, int cellNum, Object cellValue) {
        if (rowNum > headerRowNum - 1) { // 跳过表头
            ExcelExportContent excelExportContent = contentList.get(cellNum);
            if (StringUtils.isNotEmpty(excelExportContent.getFormatType())) {
                switch (excelExportContent.getFormatType()) {
                    case ExcelExportContent.FORMAT_TYPE_CONVERT:
                        String[] beanMethodArr = excelExportContent.getFormatter().split("\\.");
                        Object bean =  SpringUtils.getBean(beanMethodArr[0]);
                        cellValue = ReflectionUtils.invokeMethod(bean, beanMethodArr[1], new Object[]{cellValue}, new Class[]{Object.class});
                        break;
                    case ExcelExportContent.FORMAT_TYPE_DICT:
                        DictOptionVo dictOption = commonService.getDictOption(excelExportContent.getDictCode(), Convert.toStr(cellValue));
                        cellValue = dictOption.getLabelName();
                        break;
                }
            }
        }
        return Convert.toStr(cellValue);
    }

}
