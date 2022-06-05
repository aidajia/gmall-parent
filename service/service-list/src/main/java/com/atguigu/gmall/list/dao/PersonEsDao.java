package com.atguigu.gmall.list.dao;

import com.atguigu.gmall.list.bean.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

//方法如何起名： https://docs.spring.io/spring-data/elasticsearch/docs/4.1.15/reference/html/#repository-query-keywords
@Repository //操作数据层的一个组件
public interface PersonEsDao extends CrudRepository<Person,Long> {

    List<Person> findAllByAddressLike(String address);

    //where id>=2 and birthday > 5/27 or address "南大街"
    List<Person> findAllByIdGreaterThanEqualOrAddressLike(Long id, String address);


    long countAllByIdGreaterThan(Long id);


}
