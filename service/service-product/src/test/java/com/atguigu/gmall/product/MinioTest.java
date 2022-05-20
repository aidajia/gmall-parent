package com.atguigu.gmall.product;


import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

public class MinioTest {

    @Test
    public void uploadTest(){

        try {
            // 1、使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient =
                    new MinioClient("http://192.168.136.130:9000",
                            "admin",
                            "admin123456");

            // 2、检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("gmall");
            if(isExist) {
                System.out.println("Bucket 已存在");
            } else {
                // 3、创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket("gmall");
            }

            // 4、使用putObject上传一个文件到存储桶中。
            /**
             * String bucketName, 桶名
             * String objectName, 文件名
             * InputStream stream, 文件流
             * PutObjectOptions options 上传设置的参数项
             */
            FileInputStream stream = new FileInputStream("D:\\girl\\girl2.jpg");
            //上传设置项  public PutObjectOptions(long objectSize[对象大小], long partSize)
            //断点续传、多线程上传
            PutObjectOptions options = new PutObjectOptions(stream.available(),-1);
            options.setContentType("image/png");
            minioClient.putObject("gmall","777777.png",stream,options);
            System.out.println("上传结束");
        } catch(Exception e) {
            //RequestTimeTooSkewed：倾斜; 时间倾斜问题需要修改服务器时间
            System.err.println("Error occurred: " + e);
        }


    }

}
