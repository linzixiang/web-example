package com.linzx.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.util.Map;

/**
 * 文件操作工具类
 */
public class FileUtils {

    public static void writeBytes(File sourceFile, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = sourceFile;
            if (!file.exists()) {
                throw new FileNotFoundException(sourceFile.getCanonicalPath());
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(fis);
        }
    }

    /**
     * 保存流到文件中
     **/
    public static void saveUploadFileLocal(String absolutePath, InputStream inputStream) throws Exception {
        File targetFile = new File(absolutePath);
        createParentDirIfNotExist(targetFile);
        if (targetFile.exists()) {
            throw new RuntimeException("文件已存在：" + absolutePath);
        }
        targetFile.createNewFile();
        OutputStream os = new FileOutputStream(targetFile);
        try {
            byte[] b = new byte[1024];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 保存文件到阿里云OSS
     */
    public static void saveUploadFileOSS(String uploadPath, File file, Map<String, Object> config) throws Exception{
        // 创建OSSClient实例。
        String endpoint = MapUtils.getStringValue(config, "endpoint");
        String keyId = MapUtils.getStringValue(config, "keyid");
        String keySecret = MapUtils.getStringValue(config, "keysecret");
        String bucketName = MapUtils.getStringValue(config, "bucketname");
        OSSClient ossClient = new OSSClient(endpoint, keyId, keySecret);
        try {
            // 判断容器是否存在,不存在就创建
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            // 上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, uploadPath, file));
            // 设置权限(公开读)
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 从oss中下载文件到并保存到本地
     * @param localBasePath
     * @param fileOriginPath
     * @param config
     */
    public static void downloadOSSFileToLocal(String localBasePath, String fileOriginPath, Map<String, Object> config) {
        // 创建OSSClient实例。
        String endpoint = MapUtils.getStringValue(config, "endpoint");
        String keyId = MapUtils.getStringValue(config, "keyid");
        String keySecret = MapUtils.getStringValue(config, "keysecret");
        String bucketName = MapUtils.getStringValue(config, "bucketname");
        OSSClient ossClient = new OSSClient(endpoint, keyId, keySecret);
        try {
            // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
            ossClient.getObject(new GetObjectRequest(bucketName, fileOriginPath), new File(localBasePath + File.separator + fileOriginPath));
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

    /**
     * 文件扩展名是否合法
     * @param legalExtension 合法的扩展名集合
     * @param curExtension 当前文件扩展名
     **/
    public static boolean isExtensionLegal(String[] legalExtension, String curExtension) {
        for (String str : legalExtension) {
            if (str.equalsIgnoreCase(curExtension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果不存在目录则创建
     * @param file
     */
    public static void createParentDirIfNotExist(File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("C:/Users/LZX/Desktop/test.pdf");
        saveUploadFileLocal("C:/Users/LZX/Desktop/test.pdf", inputStream);
    }

}
