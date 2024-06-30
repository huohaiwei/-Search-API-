package com.yellow.usermanager.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.yellow.usermanager.config.OssClientConfig;
import com.yellow.usermanager.exception.BusinessException;
import com.yellow.usermanager.model.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;


/**
 * @author 陈翰垒
 */
@Component
@Slf4j
public class OssUtils {

    @Resource
    private OssClientConfig ossClientConfig;

    @Resource
    private OSS ossClient;

    /**
     * 上传文件
     * @param filePath
     * @param file
     * @return
     */
    public PutObjectResult putObject(String filePath, File file){
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossClientConfig.getBucket(), filePath, file);
        return ossClient.putObject(putObjectRequest);
    }

    /**
     * 下载文件
     * @param fileName 文件名
     * @return
     */
    public OSSObject getObject(String fileName){
        GetObjectRequest getObjectRequest = new GetObjectRequest(ossClientConfig.getBucket(), fileName);
        return ossClient.getObject(getObjectRequest);
    }

    /**
     * 关闭连接
     */
    public void shutdownOssClient() {
        if (ossClient != null) {
            ossClient.shutdown();
            log.info("OSS client shutdown successfully");
        }
    }
}
