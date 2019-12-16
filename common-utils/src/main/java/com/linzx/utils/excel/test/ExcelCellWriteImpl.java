package com.linzx.utils.excel.test;

import com.linzx.utils.Convert;
import com.linzx.utils.excel.ExcelCellWriteListener;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class ExcelCellWriteImpl implements ExcelCellWriteListener {

    @Override
    public String onCellWrite(int sheetNo, int rowNum, int cellNum, Object cellValue) {
        System.out.println("sheetNo：" + sheetNo + ",rowNum：" + rowNum + ",cellNum：" + cellNum + ",cellValue：" + cellValue);
        return Convert.toStr(cellValue);
    }

}
