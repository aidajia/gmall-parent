package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.list.SearchFeignClient;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 检索功能控制器
 */
@Controller
public class ListController {

    @Autowired
    SearchFeignClient searchFeignClient;


    /**
     * 检索
     * 1、按照分类检索
     * @RequestParam(value = "category1Id",required = false) Long category1Id,
     * @RequestParam(value = "category2Id",required = false) Long category2Id,
     * @RequestParam(value = "category3Id",required = false) Long category3Id)
     *
     * 2、按照品牌检索
     * @RequestParam(value = "trademark",required = false) String trademark
     *  trademark=1:小米     分隔字符串
     *
     * 3、按照平台属性检索
     * props=23:8G:运行内存&props=24:128G:机身内存：  [23:8G:运行内存,24:128G:机身内存]
     * @RequestParam(value = "props",required = false) String props[]
     *
     * 4、进行排序
     *  综合排序（热度分）
     *      order=1:desc
     *      order=1:asc
     *  价格排序
     *      order=2:desc
     *      order=2:asc
     *
     * 5、分页
     *  pageNo=2
     *
     *
     *  接参集合
     *  @RequestParam(value = "category1Id",required = false) Long category1Id,
     *  @RequestParam(value = "category2Id",required = false) Long category2Id,
     *  @RequestParam(value = "category3Id",required = false) Long category3Id,
     *  @RequestParam(value = "trademark",required = false) String trademark,
     *  @RequestParam(value = "props",required = false) String props[],
     *  @RequestParam(value = "order",required = false) String order,
     *  @RequestParam(value = "pageNo",required = false) Long pageNo,
     *  @RequestParam(value = "keyword",required = false) String keyword
     *
     *  推荐用JavaBean封装以上参数；SearchParam
     *
     * @return
     */
    @GetMapping("/list.html")
    public String searchPage(SearchParam param, Model model, HttpServletRequest request){



        //TODO 远程调用检索服务去检索
        Result<GoodsSearchResultVo> searchGoods = searchFeignClient.searchGoods(param);

        if(searchGoods.isOk()){
            GoodsSearchResultVo data = searchGoods.getData();
            //展示数据到页面
            //1. 原来参数原封不动给页面
            model.addAttribute("searchParam",param);

            //2. 品牌面包屑: 例如: 品牌: VIVO
            model.addAttribute("trademarkParam",data.getTrademarkParam());

            //3. url参数
            model.addAttribute("urlParam",data.getUrlParam());

            //4. 平台属性面包屑: propsParamList: 集合里面每个元素(attrName/attrValue)
            model.addAttribute("propsParamList", data.getPropsParamList());

            //5. 检索条件区: 品牌列表: 集合里面的每个元素(tmId, tmLogoUrl, tmName)
            model.addAttribute("trademarkList",data.getTrademarkList());

            //6. 检索条件区: 平台属性列表: attrsList每个元素(attrId, attrName, attrValueList)
            model.addAttribute("attrsList",data.getAttrList());

            //7. 排序信息: Bean (type, sort)
            model.addAttribute("orderMap",data.getOrderMap());

            //8. 查到的商品列表: goodsList 集合中每一个元素(id, defaultImg, price,title)
            model.addAttribute("goodsList",data.getGoodsList());

            //9. 分页信息: 当前页,总页数
            model.addAttribute("pageNo",data.getPageNo());
            model.addAttribute("totalPages",data.getTotalPages());
        }

        //来到检索页
        return "list/index";
    }

}
