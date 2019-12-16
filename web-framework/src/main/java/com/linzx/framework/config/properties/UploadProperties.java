package com.linzx.framework.config.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * 上传配置
 */
@Configuration
@DependsOn({"globalProperties"})
public class UploadProperties implements InitializingBean {

    /**图片上传之后的保存路径**/
    public static String uploadImageBasePath;

    public static String uploadAliyunOSSBucketname;

    public static String uploadAliyunOSSEndpoint;

    public static String uploadAliyunOSSKeyid;

    public static String uploadAliyunOSSKeysecret;

    @Override
    public void afterPropertiesSet() {
        uploadImageBasePath = GlobalProperties.getConfigStr("upload.base.path", "");
        uploadAliyunOSSBucketname = GlobalProperties.getConfigStr("upload.aliyun.oss.bucketname", "");
        uploadAliyunOSSEndpoint = GlobalProperties.getConfigStr("upload.aliyun.oss.endpoint", "");
        uploadAliyunOSSKeyid = GlobalProperties.getConfigStr("upload.aliyun.oss.keyid", "");
        uploadAliyunOSSKeysecret = GlobalProperties.getConfigStr("upload.aliyun.oss.keysecret", "");
    }

}
