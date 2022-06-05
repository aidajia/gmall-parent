package com.atguigu.gmall.model.vo;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.list.SearchParam;
import lombok.Data;

import java.util.List;

@Data
public class GoodsSearchResultVo {

    private SearchParam searchParam; //当时检索用的所有参数
    private String trademarkParam; //品牌面包屑: 自己拼成 品牌: VIVO 的效果
    private String urlParam; //url参数, 需要记录url?k=v&k=v&k=v
    private List<SearchAttr> propsParamList; //平台属性面包屑
    private List<SearchTrademarkVo> trademarkList; //检索区: 品牌列表信息
    private List<SearchAttrListVo> attrList; //检索区: 平台属性列表信息
    private SearchOrderMapVo orderMap; //排序条件
    private List<Goods> goodsList;  //检索到的所有商品集合
    private Integer pageNo;//当前页
    private Integer totalPages;//总页码

}
