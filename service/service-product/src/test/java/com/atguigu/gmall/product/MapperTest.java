package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {

    @Autowired
    BaseTrademarkMapper mapper;

    @Test
    public void testReadWriteSplit(){
        BaseTrademark trademark = new BaseTrademark();
        trademark.setTmName("呵呵");
        mapper.insert(trademark);
    }

    //读写分离
    @Test
    public void testQuery(){
        BaseTrademark byId1 = mapper.selectById(14L);
        System.out.println("第一次"+byId1);

        BaseTrademark byId2 = mapper.selectById(14L);
        System.out.println("第2次"+byId2);


        BaseTrademark byId3 = mapper.selectById(14L);
        System.out.println("第3次"+byId3);

        BaseTrademark byId4 = mapper.selectById(14L);
        System.out.println("第4次"+byId4);
    }

}
