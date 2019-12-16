package com.linzx.utils.excel.test;

import com.linzx.utils.Convert;
import com.linzx.utils.excel.ExcelCellReadListener;

public class ExcelCellReadImpl implements ExcelCellReadListener {

    @Override
    public String onStringListCellRead(int index, String value) {
        System.out.println("onStringListCellRead->" + index + "," + value);
        return value;
    }

    @Override
    public Object onRowModelCellRead(String key, Object value) {
        System.out.println("onRowModelCellRead->" + key + "," + Convert.toStr(value));
        return value;
    }
    
}
