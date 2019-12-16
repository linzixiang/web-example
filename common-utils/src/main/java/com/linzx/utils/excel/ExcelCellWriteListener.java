package com.linzx.utils.excel;

import org.apache.poi.ss.usermodel.Cell;

public interface ExcelCellWriteListener {

    String onCellWrite(int sheetNo, int rowNum, int cellNum, Object cellValue);
    
}
