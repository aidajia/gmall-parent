package com.atguigu.gmall.starter.utils;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JSONS {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String toStr(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象(Json)转换Sting的异常",e);
        }
        return null;
    }

//    public static List<CategoryAndChildTo> strToCategoryObj(String categorys) {
//        List<CategoryAndChildTo> tos =null;
//        try {
//            tos = objectMapper.readValue(categorys, new TypeReference<List<CategoryAndChildTo>>() {
//            });
//        } catch (JsonProcessingException e) {
//            log.error("菜单(Json)转换对象的异常",categorys);
//        }
//        return tos;
//    }

    public static <T> T strToObj(String json, TypeReference<T> typeReference) {
        T t = null;
        try {
            t = objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T extends Object> T nullInstance(TypeReference<T> typeReference) {
        String json = "[]";
        T t = null;
        try {
            t = objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("准备空实例异常: {}",e);
            try {
                t = objectMapper.readValue("{}", typeReference);
            } catch (JsonProcessingException ex) {
                log.error("你这不是json");
            }
        }
        return t;
    }
}
