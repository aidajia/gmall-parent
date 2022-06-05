package com.atguigu.gmall.list;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 整合ES：
 * 1、导入依赖 spring-boot-starter-data-elasticsearch
 * 2、分析ES的自动配置
 *      1)、ElasticsearchDataAutoConfiguration：ES数据自动配置
 *          @Import({
 *          BaseConfiguration.class,： ElasticsearchConverter 数据转换器
 *          RestClientConfiguration.class： ElasticsearchRestTemplate 封装好了对这个中间件所有的crud功能
 *              xxxTemplate：RedisTemplate、JdbcTemplate、RabbitTemplate、ElasticsearchRestTemplate
 *
 *          ReactiveRestClientConfiguration.class})
 *
 *      2)、ElasticsearchRepositoriesAutoConfiguration: 开启es的自动仓库；
 *          声明式操作es；无需关系api
 *              EsDao:   save();
 *
 *      3)、ElasticsearchRestClientAutoConfiguration: Rest客户端的自动配置
 *          属性绑定： ElasticsearchRestClientProperties
 *          @Import({
 *              RestClientBuilderConfiguration.class,
 *              RestHighLevelClientConfiguration.class,  给容器中自动注入 RestHighLevelClient;
 *              RestClientFallbackConfiguration.class})
 *
 *      总结：自动配置产生如下效应；
 *       1）、配置文件中：spring.elasticsearch  下的所有配置都是配置es的
 *       2）、所有东西自动化
 *          1、如果想要编程式操作es，推荐用 ElasticsearchRestTemplate 操作es
 *          2、如果想要声明式操作es，只需要写好接口
 *
 *
 * spring-data-es怎么使用
 * 1、@EnableElasticsearchRepositories 开启es的自动仓库功能
 * 2、写接口crud-es；
 *      1）、写Bean
 *      2）、写接口，继承 CrudRepository
 *
 *
 */
@EnableElasticsearchRepositories //自动扫描主程序所在的包以及所有子包下的所有 @Repository
@SpringCloudApplication
public class ListApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListApplication.class,args);
    }

}
