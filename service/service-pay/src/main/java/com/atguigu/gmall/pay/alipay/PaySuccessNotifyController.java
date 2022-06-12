package com.atguigu.gmall.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.atguigu.gmall.common.constants.MqConst;
import com.atguigu.gmall.common.util.JSONS;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.pay.service.AlipayService;
import com.atguigu.gmall.model.mqto.order.PayNotifySuccessVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/payment")
@RestController
public class PaySuccessNotifyController {

    @Autowired
    AlipayService alipayService;

    @Autowired
    OrderFeignClient orderFeignClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 支付宝的异步通知里面改订单状态
     * @param vo
     * @param req Map<String,String>  把当前请求带来的所有参数封装到Map中
     * @return
     * @throws AlipayApiException
     */
    @PostMapping("/notify/success")
    public String paySuccessNotify(PayNotifySuccessVo vo,
                                   @RequestParam Map<String,String> req) throws AlipayApiException {


        System.out.println("支付宝通知到达: "+vo);
        Map<String,String> params = req;
        //验签很重要
        boolean sign = alipayService.checkSign(params);
        if(sign){
            System.out.println("验签通过...");
            //签名验证通过, 修改订单状态信息
//            Result result = orderFeignClient.updateOrderStatusToPAID(vo.getOut_trade_no());
//            if(result.isOk()) {
//                return "success"; //收到success支付宝就不通过
//            }
//            return "error";
            rabbitTemplate.convertAndSend(
                    MqConst.ORDER_EVENT_EXCHANGE,
                    MqConst.RK_ORDER_PAYED,
                    JSONS.toStr(vo));

            return "success";
        }
        System.out.println("验签失败...");
        return "error";
    }

}
