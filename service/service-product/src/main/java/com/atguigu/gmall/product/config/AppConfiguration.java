package com.atguigu.gmall.product.config;


import com.atguigu.gmall.annotation.EnableMinio;
import com.atguigu.gmall.config.AppMybatisPlusConfiguration;
import com.atguigu.gmall.product.annotation.ProductConfig;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 当前应用的配置
 */

//@ProductConfig
@MapperScan(basePackages = {"com.atguigu.gmall.product.dao","com.atguigu.gmall.product.mapper"})
@Import(AppMybatisPlusConfiguration.class)
@EnableMinio
@Configuration
public class AppConfiguration {

    //额外配置


}
