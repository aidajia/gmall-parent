package com.atguigu.gmall.list.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

//shards指定数据分片【类似mysql的分库分表】，分成几堆进行存储
//replicas 指定副本； 类似mysql的主从复制，主从同步
@AllArgsConstructor
@NoArgsConstructor
@Document(shards=1,replicas = 1,indexName="person",createIndex = true) //文档
@Data
@ToString
public class Person {

    @Id
    private Long id;


    private String name;
    private String email;
    private String address;

    //    @Field(format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm")
    private Date birthday;

}
