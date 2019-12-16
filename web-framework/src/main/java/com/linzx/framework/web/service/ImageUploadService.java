package com.linzx.framework.web.service;

import com.linzx.framework.bean.ImageBean;

import java.util.Map;

public interface ImageUploadService {

    /**
     * 保存原图
     * @param name 名称
     * @param ext 扩展名
     */
     Long addOriginImage(String name, String ext, ImageBean imageBean);

    /**
     * 保存缩略图
     * @param spec 缩略图规格
     */
     void appendThumbnailImage(Long fileId, String spec,ImageBean imageBean);

    /**
     * 获取指定规格的图片信息，当fileId资源不存在时抛出异常
     * @param fileId 图片id
     * @param spec 图片规格
     * @return
     */
    Map<String, Object> getImageInfo(Long fileId, String spec) throws Exception;

}
