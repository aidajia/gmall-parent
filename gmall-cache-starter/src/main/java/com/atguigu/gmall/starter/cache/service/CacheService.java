package com.atguigu.gmall.starter.cache.service;


import com.fasterxml.jackson.core.type.TypeReference;

public interface CacheService {

//    List<CategoryAndChildTo> getCategorys();
//
//    void saveCategoryData(List<CategoryAndChildTo> childs);

    /**
     * 从缓存中去一个数据
     * @param cacheKey
     * @param typeReference
     * @param <T>
     * @return
     */
    <T>T getCacheData(String cacheKey, TypeReference<T> typeReference);

    /**
     * 给缓存保存一个数据
     * @param key
     * @param data
     */
    void save(String key,Object data);
}
