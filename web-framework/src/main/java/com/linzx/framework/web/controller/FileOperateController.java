package com.linzx.framework.web.controller;

import com.linzx.common.constant.Constants;
import com.linzx.framework.bean.ImageBean;
import com.linzx.framework.config.properties.UploadProperties;
import com.linzx.framework.exception.BizCommonException;
import com.linzx.framework.utils.SpringUtils;
import com.linzx.framework.web.BaseController;
import com.linzx.framework.web.service.ImageUploadService;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.utils.*;
import com.linzx.utils.imgae.ImageInfo;
import com.linzx.utils.imgae.ImagesUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/fileOperate")
public class FileOperateController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(FileOperateController.class);

    private String uploadImageFold = "images";

    private String uploadBasePath = UploadProperties.uploadImageBasePath + fileSeparator + uploadImageFold;

    private int imageUploadMaxSize = 10 * 1024; // 单位KB

    private static String fileSeparator = "/";

    /** 图片下载地址 **/
    public static final String IMAGE_DOWNLOAD_URL = "/fileOperate/showImage/{}";

    @Autowired(required = false)
    private ImageUploadService imageUploadService;

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/uploadImage")
    @ResponseBody
    public AjaxResult uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        checkImageUploadService();
        String filename = file.getOriginalFilename();
        checkUploadImageSize(file.getSize());
        String size = UnitConvertUtils.byteToOther(file.getSize());
        String extension = getExtension(file);
        if (!FileUtils.isExtensionLegal(MimeTypeUtils.IMAGE_EXTENSION, extension)) {
            String extensionStr = Convert.toStrConcat(MimeTypeUtils.IMAGE_EXTENSION, ",");
            throw new BizCommonException("upload.file.not.image", new String[] {extensionStr});
        }
        String filePath = genOriFilePath(extension);
        ImageBean imageBean = new ImageBean();
        String absolutePath = uploadBasePath + filePath;
        /**本地保存一份**/
        FileUtils.saveUploadFileLocal(absolutePath, file.getInputStream());
        /**阿里云oss云端保存一个备份**/
        Map<String, Object> ossConfigMap = MapUtils.genHashMap(
                "endpoint", UploadProperties.uploadAliyunOSSEndpoint,
                "keyid", UploadProperties.uploadAliyunOSSKeyid,
                "keysecret", UploadProperties.uploadAliyunOSSKeysecret,
                "bucketname", UploadProperties.uploadAliyunOSSBucketname);
        FileUtils.saveUploadFileOSS(uploadImageFold + filePath, new File(absolutePath), ossConfigMap);
        BufferedImage sourceImg = ImageIO.read(new FileInputStream(uploadBasePath + filePath));
        imageBean.setStoreWay(ImageBean.STORE_SERVERFOLD);
        imageBean.setPath(filePath);
        imageBean.setSize(size);
        imageBean.setHeight(sourceImg.getHeight());
        imageBean.setWidth(sourceImg.getHeight());
        Long fileId = imageUploadService.addOriginImage(filename, extension, imageBean);
        return AjaxResult.success(fileId);
    }

    /**
     * 检查上传图片大小
     */
    private void checkUploadImageSize(long size) {
        if (size / 1024 > imageUploadMaxSize) {
            String maxSize = UnitConvertUtils.byteToOther(imageUploadMaxSize * 1024);
            throw new BizCommonException("common.image.size.not.allow", new String[] { maxSize });
        }
    }

    @RequestMapping("/showImage/{fileId}")
    public void downloadImage(
            @PathVariable("fileId") Long fileId,
            @RequestParam(value = "spec", defaultValue = Constants.IMAGE_ORIGINAL) String spec,
            HttpServletResponse response) throws Exception {
        checkImageUploadService();
        File responseFile = null;
        Map<String, Object> imageInfoMap = imageUploadService.getImageInfo(fileId, spec);
        if (StringUtils.equals(spec, Constants.IMAGE_ORIGINAL)) {
            copyFileFromOSSToLocal(imageInfoMap);
        }
        String imagePath = MapUtils.getString(imageInfoMap, "path");
        try {
            if (imageInfoMap == null) {
                // 根据原图生成缩略图
                ImageBean image = new ImageBean();
                Map<String, Object> imageOriginInfoMap = imageUploadService.getImageInfo(fileId, Constants.IMAGE_ORIGINAL);
                if (StringUtils.equals(spec, Constants.IMAGE_ORIGINAL)) {
                    copyFileFromOSSToLocal(imageOriginInfoMap);
                }
                int storeWay = MapUtils.getIntValue(imageOriginInfoMap, "storeWay");
                String imageOriginPath = MapUtils.getString(imageOriginInfoMap, "path");
                if (storeWay == ImageBean.STORE_SERVERFOLD) {
                    File sourceFile = new File(uploadBasePath + imageOriginPath);
                    String yyyyMM = DateFormatUtils.format(new Date(), "yyyyMM");
                    String relativePath = yyyyMM + fileSeparator + spec;
                    String sourceFileName = sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf("."));
                    String sourceFileExt = sourceFile.getName().substring(sourceFile.getName().lastIndexOf(".") + 1);
                    String fileName = sourceFileName + "_" + spec + "." + sourceFileExt;
                    File targetFile = new File(uploadBasePath + fileSeparator + relativePath + fileSeparator + fileName);
                    ImageInfo imageInfo = ImagesUtils.cropCenter(sourceFile, targetFile, 800, 800);
                    image.setStoreWay(ImageBean.STORE_SERVERFOLD);
                    image.setHeight(imageInfo.getHeight());
                    image.setWidth(imageInfo.getWidth());
                    image.setPath(fileSeparator + relativePath + fileSeparator + fileName);
                    image.setSize(imageInfo.getSize());
                    imageUploadService.appendThumbnailImage(fileId, spec, image);
                    imagePath = image.getPath();
                }
            }
            responseFile = new File(uploadBasePath + imagePath);
            // 读取图片资源
            response.setCharacterEncoding(Constants.CHARSET_UTF8);
            response.setContentType("image/jpeg");
            response.addHeader("Content-Length", "" + responseFile.length());
            ServletOutputStream outputStream = response.getOutputStream();
            FileUtils.writeBytes(responseFile, outputStream);
        } catch (Exception e) {
            logger.error("downloadImage error", e);
        }
    }

    public void checkImageUploadService() {
        if (imageUploadService == null) {
            throw new RuntimeException("需要先实现就接口：com.linzx.framework.web.service.ImageUploadService");
        }
    }

    /**
     * 生成原图文件路径
     */
    public static String genOriFilePath(String ext) {
        String yyyyMM = DateFormatUtils.format(new Date(), "yyyyMM");
        String relativePath = yyyyMM + fileSeparator + Constants.IMAGE_ORIGINAL;
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return fileSeparator + relativePath + fileSeparator + uuid + "." + ext;
    }

    /**
     * 获取文件扩展名
     */
    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }

    /**
     * 从云端复制一份图片保存到本地
     */
    private void copyFileFromOSSToLocal(Map<String, Object> imageInfoMap) {
        // 如果原图不存在，则从oss重新加载并保存到本地
        String imageOriginPath = MapUtils.getString(imageInfoMap, "path");
        File sourceFile = new File(uploadBasePath + imageOriginPath);
        if (!sourceFile.exists()) {
            FileUtils.createParentDirIfNotExist(sourceFile);
            Map<String, Object> ossConfigMap = MapUtils.genHashMap(
                    "endpoint", UploadProperties.uploadAliyunOSSEndpoint,
                    "keyid", UploadProperties.uploadAliyunOSSKeyid,
                    "keysecret", UploadProperties.uploadAliyunOSSKeysecret,
                    "bucketname", UploadProperties.uploadAliyunOSSBucketname);
            FileUtils.downloadOSSFileToLocal(UploadProperties.uploadImageBasePath, uploadImageFold + imageOriginPath, ossConfigMap);
        }
    }


}
