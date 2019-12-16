package com.linzx.utils;

import java.text.DecimalFormat;

/**
 * 单位转换工具
 */
public class UnitConvertUtils {

    /**
     * 文件字节单位(byte)转Kb，Mb
     * @return
     */
    public static String byteToOther(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "KB";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "GB";
        }
        return fileSizeString;
    }

}
