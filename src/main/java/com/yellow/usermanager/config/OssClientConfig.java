package com.yellow.usermanager.config;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSBuilder;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS连接
 *
 * @author 陈翰垒
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "oss.client")
public class OssClientConfig {

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 区域
     */
    private String endpoint;

    /**
     * 桶名
     */
    private String bucket;

    @Bean
    public OSS ossClient() {
        //OSSClient 对象
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretKey);
        return ossClient;
    }

}
