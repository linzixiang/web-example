package com.linzx.framework.bean;

public class ImageBean {

    /** fastDFS存储 **/
    public static final int STORE_FASTDFS = 1;
    /** 服务器文件夹存储 **/
    public static final int STORE_SERVERFOLD = 2;

    /** 图片大小 **/
    private String size;
    /** 图片宽度 **/
    private Integer width;
    /** 图片高度 **/
    private Integer height;
    /** 存储方式：1表示fastDFS,2表示服务器文件夹**/
    private Integer storeWay;
    /** 文件路径 **/
    private String path;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getStoreWay() {
        return storeWay;
    }

    public void setStoreWay(Integer storeWay) {
        this.storeWay = storeWay;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
}
