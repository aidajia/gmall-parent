package com.atguigu.gmall.minio.config;


import com.atguigu.gmall.minio.service.OSSService;
import com.atguigu.gmall.minio.service.impl.OSSServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(MinioProperties.class)
//1. 开启MinioProperties 和配置文件绑定
//2. MinioProperties 放进容器
@Configuration
public class MinioAutoConfiguration {

    @Autowired
    MinioProperties minioProperties;

    //额外配置写到这里
    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient minioClient = new MinioClient(minioProperties.getEndpoint(),
                minioProperties.getAccessKey(),
                minioProperties.getSecretKey());

        boolean b = minioClient.bucketExists(minioProperties.getBucket());
        if(!b){
            minioClient.makeBucket(minioProperties.getBucket());
        }

        return minioClient;
    }

    @Bean
    public OSSService ossService(){
        OSSServiceImpl service = new OSSServiceImpl();

        //SpringBoot会自动给 OSSServiceImpl注入他想要的东西
        return service;
    }

}
