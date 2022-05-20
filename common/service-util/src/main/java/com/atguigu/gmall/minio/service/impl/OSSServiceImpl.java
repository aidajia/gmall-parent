package com.atguigu.gmall.minio.service.impl;

import com.atguigu.gmall.minio.config.MinioProperties;
import com.atguigu.gmall.minio.service.OSSService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OSSServiceImpl implements OSSService {

    @Autowired
    MinioClient minioClient;

    @Autowired
    MinioProperties minioProperties;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {

        //1.准备一个唯一的文件名
        String filename = UUID.randomUUID().toString().replace("_","") + "_" +file.getOriginalFilename();
        //2.文件流
        InputStream inputStream = file.getInputStream();

        //3.上传参数设置项
        PutObjectOptions options = new PutObjectOptions(inputStream.available(), -1);

        //4.设置此次上传的文件的内容类型
        String contentType = file.getContentType();
        options.setContentType(contentType);

        //5.上传
        minioClient.putObject(minioProperties.getBucket(),filename,inputStream,options);

        //6.访问路径
        String path = minioProperties.getEndpoint()+"/"+minioProperties.getBucket()+"/"+filename;

        return path;
    }
}
