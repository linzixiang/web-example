package com.linzx.utils.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.event.WriteHandler;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linzx.utils.MapUtils;
import com.linzx.utils.exception.ExcelWriteException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * easyexcel工具操作
 */
public class ExcelUtils {

    /**
     * 读取Excel数据，返回 StringList 的列表
     *
     * @param inputStream Excel文件输入流
     * @param sheetNo     从那张表开始读取，1开始计数。例如2表示读取sheet1和sheet2的数据
     * @param headLineMun 表头占了多少行（从第几行开始读取）
     * @return
     */
    public static List<List<String>> readExcelWithStringList(InputStream inputStream, int sheetNo, int headLineMun, ExcelCellReadListener readCellListener) {
        // 解析每行结果在listener中处理
        List<List<String>> datas = new ArrayList<>();
        EasyExcelFactory.readBySax(inputStream, new Sheet(sheetNo, headLineMun), new AnalysisEventListener() {

            @Override
            public void invoke(Object object, AnalysisContext analysisContext) {
                List<String> stringList = (List<String>) object;
                if (readCellListener != null) {
                    for (int index = 0; index < stringList.size(); index++) {
                        String value = stringList.get(index);
                        value = readCellListener.onStringListCellRead(index, value);
                        stringList.set(index, value);
                    }
                }
                datas.add(stringList);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            }
        });
        return datas;
    }

    /**
     * 使用 StringList 来写入Excel，表头只有一行
     *
     * @param outputStream Excel的输出流
     * @param data         要写入的以StringList为单位的数据
     * @param sheetName    sheet表名称
     * @param headerSimple
     */
    public static void writeExcelWithObjListBySimplyHeader(OutputStream outputStream, List<List<Object>> data, String sheetName, List<String> headerSimple, ExcelCellWriteListener handler) throws IOException {
        List<List<String>> headerList = new ArrayList<>();
        for (String header : headerSimple) {
            List<String> headerCol = new ArrayList<>();
            headerCol.add(header);
            headerList.add(headerCol);
        }
        writeExcelWithObjList(outputStream, data, sheetName, headerList, handler);
    }

    /**
     * 使用 StringList 来写入Excel
     *
     * @param outputStream Excel的输出流
     * @param data         要写入的以StringList为单位的数据
     * @param sheetName    sheet表名称
     * @param header       表头
     */
    public static void writeExcelWithObjList(OutputStream outputStream, List<List<Object>> data, String sheetName, List<List<String>> header, ExcelCellWriteListener handler) throws IOException {
        ExcelWriter writer = EasyExcelFactory.getWriterWithTempAndHandler(null, outputStream, ExcelTypeEnum.XLSX, true, new WriteHandler() {

            private int sheetIndex;

            private int rowIndex;

            @Override
            public void sheet(int i, org.apache.poi.ss.usermodel.Sheet sheet) {
                this.sheetIndex = i;
            }

            @Override
            public void row(int i, Row row) {
                this.rowIndex = i;
            }

            @Override
            public void cell(int i, Cell cell) {
                if (handler != null) {
                    setCellValue(sheetIndex, rowIndex, i, cell, handler);
                }
            }

        });
        try {
            Sheet sheet1 = new Sheet(1, 0);
            sheet1.setSheetName(sheetName);
            Table table = new Table(1);
            table.setHead(header);
            writer.write1(data, sheet1, table);
        } finally {
            writer.finish();
            outputStream.close();
        }
    }

    /**
     * 读取Excel数据，返回 BaseRowMode List 的列表
     *
     * @param inputStream Excel文件输入流
     * @param clazz       返回的class模型
     * @param sheetNo     从那张表读取，1开始计数
     * @param headLineMun 表头占了多少行（从第几行开始读取）
     * @return
     */
    public static List<JSONObject> readExcelWithModel(InputStream inputStream, Class<? extends BaseRowModel> clazz, int sheetNo, int headLineMun, ExcelCellReadListener readCellListener) {
        // 解析每行结果在listener中处理
        List<JSONObject> datas = new ArrayList<>();
        EasyExcelFactory.readBySax(inputStream, new Sheet(sheetNo, headLineMun, clazz), new AnalysisEventListener() {

            @Override
            public void invoke(Object object, AnalysisContext analysisContext) {
                String json = JSONObject.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
                JSONObject jsonObject = JSON.parseObject(json);
                if (readCellListener != null) {
                    Set<String> keySet = jsonObject.keySet();
                    for (String key : keySet) {
                        Object value = MapUtils.getObject(jsonObject, key);
                        value = readCellListener.onRowModelCellRead(key, value);
                        jsonObject.put(key, value);
                    }
                }
                datas.add(jsonObject);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            }
        });
        return datas;
    }

    /**
     * 继承BaseRowModel生成Excel
     *
     * @param outputStream
     * @param data
     * @param sheetName
     * @param handler
     * @throws IOException
     */
    public static void writeExcelWithModelList(OutputStream outputStream,
                                               List<? extends BaseRowModel> data, Class<? extends BaseRowModel> clazz,
                                               String sheetName, ExcelCellWriteListener handler) throws IOException {
        ExcelWriter writer = EasyExcelFactory.getWriterWithTempAndHandler(null, outputStream, ExcelTypeEnum.XLSX, true, new WriteHandler() {

            private int sheetNo;

            private int rowNum;

            @Override
            public void sheet(int sheetNo, org.apache.poi.ss.usermodel.Sheet sheet) {
                this.sheetNo = sheetNo;
            }

            @Override
            public void row(int rowNum, Row row) {
               this.rowNum = rowNum;
            }

            @Override
            public void cell(int cellNum, Cell cell) {
                if (handler != null) {
                    setCellValue(sheetNo, rowNum, cellNum, cell, handler);
                }
            }
        });
        try {
            Sheet sheet1 = new Sheet(1, 0, clazz);
            sheet1.setSheetName(sheetName);
            writer.write(data, sheet1);
        } finally {
            writer.finish();
            outputStream.close();
        }
    }

    /**
     * 设置excel单元格的值
     */
    private static void setCellValue(int sheetNo, int rowNum, int cellNum, Cell cell, ExcelCellWriteListener handler) {
        CellType cellTypeEnum = cell.getCellTypeEnum();
        String cellValueRet = "";
        if (cellTypeEnum.equals(CellType.NUMERIC)) {
            double cellValue = cell.getNumericCellValue();
            cellValueRet = handler.onCellWrite(sheetNo, rowNum, cellNum, cellValue);
        } else if (cellTypeEnum.equals(CellType.STRING)) {
            String cellValue = cell.getStringCellValue();
            cellValueRet = handler.onCellWrite(sheetNo, rowNum, cellNum, cellValue);
        } else {
            throw new ExcelWriteException("excel type not support error");
        }
        cell.setCellType(CellType.STRING);
        cell.setCellValue(cellValueRet);
    }

}
