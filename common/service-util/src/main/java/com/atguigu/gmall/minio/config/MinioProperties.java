package com.atguigu.gmall.minio.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 封装minio的配置属性
 * @ConfigurationProperties 告诉SpringBoot
 * 未来我这个JavaBean的所有属性和配置文件 app.minio 下的属性一一对应
 */
@ConfigurationProperties(prefix = "app.minio")
@Data
public class MinioProperties {

    String endpoint;

    String accessKey;

    String secretKey;

    String bucket;

}
