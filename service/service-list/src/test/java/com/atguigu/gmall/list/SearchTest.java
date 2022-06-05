package com.atguigu.gmall.list;

import com.atguigu.gmall.list.service.GoodsSearchService;
import com.atguigu.gmall.model.list.SearchParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SearchTest {


    @Autowired
    GoodsSearchService searchService;
    @Test
    public void searchTest(){
        SearchParam param = new SearchParam();
        //传递的参数
        param.setCategory3Id(61L);

//        param.setTrademark("4:小米");
//        param.setKeyword("手机");
//        String[] props = new String[]{"4:64GB:机身存储"};
//        param.setProps(props);
//        param.setOrder("2:desc");
//        param.setPageNo(1);

        searchService.search(param);
    }
}
