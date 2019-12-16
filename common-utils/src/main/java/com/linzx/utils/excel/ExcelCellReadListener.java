package com.linzx.utils.excel;

public interface ExcelCellReadListener {

    /**
     * 读取动态数组时，回调的方法
     * @param index 动态数组下标
     * @param value 下标相应的值
     * @return 返回处理之后的结果值
     */
    String onStringListCellRead(int index, String value);

    /**
     * 读取对象模型时，回调的方法
     * @param key
     * @param value
     * @return
     */
    Object onRowModelCellRead(String key, Object value);

}
