package com.linzx.utils.excel.test;

import com.alibaba.fastjson.JSONObject;
import com.linzx.utils.excel.ExcelUtils;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    @Test
    public void test1(){
        List<WriteModel> modelList = createModelList();
        try {
            OutputStream outputStream = new FileOutputStream("C:/Users/LZX/Desktop/test.xlsx");
            ExcelUtils.writeExcelWithModelList(outputStream, modelList, WriteModel.class, "测试", new ExcelCellWriteImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws Exception {
        List<List<Object>> objectList = createObjectList();
        OutputStream outputStream = new FileOutputStream("C:/Users/LZX/Desktop/test2.xlsx");
        List<List<String>> header = new ArrayList<>();
        List<String> headerCol1 = new ArrayList<>();
        headerCol1.add("年龄");
        header.add(headerCol1);
        List<String> headerCol2 = new ArrayList<>();
        headerCol2.add("姓名");
        header.add(headerCol2);
        List<String> headerCol3 = new ArrayList<>();
        headerCol3.add("密码");
        header.add(headerCol3);
        ExcelUtils.writeExcelWithObjList(outputStream, objectList, "测试", header, new ExcelCellWriteImpl());
    }

    @Test
    public void test3() throws Exception {
        List<List<Object>> objectList = createObjectList();
        OutputStream outputStream = new FileOutputStream("C:/Users/LZX/Desktop/test3.xlsx");
        List<String> header = new ArrayList<>();
        header.add("年龄");
        header.add("姓名");
        header.add("密码");
        ExcelUtils.writeExcelWithObjListBySimplyHeader(outputStream, objectList, "测试", header, new ExcelCellWriteImpl());
    }

    @Test
    public void test4() throws Exception {
        InputStream inputStream = new FileInputStream("C:/Users/LZX/Desktop/test.xlsx");
        List<List<String>> lists = ExcelUtils.readExcelWithStringList(inputStream, 1, 1, new ExcelCellReadImpl());
        int i = 0;
    }

    @Test
    public void test5() throws Exception {
        InputStream inputStream = new FileInputStream("C:/Users/LZX/Desktop/test.xlsx");
        List<JSONObject> lists = ExcelUtils.readExcelWithModel(inputStream, WriteModel.class, 1, 1, new ExcelCellReadImpl());
        int i = 0;
    }

    public List<List<Object>> createObjectList() {
        List<List<Object>> rows = new ArrayList<>();
        for (int i = 0; i < 20; i++ ) {
            List<Object> row = new ArrayList<>();
            row.add(i + 1);
            row.add("小哈学java" + i);
            row.add("123456");
            rows.add(row);
        }
        return rows;
    }

    public List<WriteModel> createModelList() {
        List<WriteModel> writeModels = new ArrayList<>();
        for (int i = 0; i< 20; i ++) {
            WriteModel writeModel = new WriteModel();
            writeModel.setAge(i + 1);
            writeModel.setName("小哈学java" + i);
            writeModel.setPassword("123456");
            writeModel.setDate(new Date());
            writeModel.setBool(true);
            writeModels.add(writeModel);
        }
        return writeModels;
    }

}
