package com.atguigu.gmall.model.mqto.order;

import lombok.Data;

/**
 * <pre>
 *  PayNotifySuccessVo
 * </pre>
 * @author lfy
 * @verison $Id: PayNotifySuccessVo v 0.1 2022-06-08 14:18:37
 */
@Data
public class PayNotifySuccessVo{

    private String	gmt_create;


    private String	charset;


    private String	gmt_payment;


    private String	notify_time;


    private String	subject;


    private String	sign;

    private String	buyer_id;


    private String	body;


    private String	invoice_amount;


    private String	version;


    private String	notify_id;


    private String	fund_bill_list;


    private String	notify_type;


    private String	out_trade_no; //


    private String	total_amount;


    private String	trade_status;


    private String	trade_no;


    private String	auth_app_id;


    private String	receipt_amount;


    private String	point_amount;


    private String	app_id;


    private String	buyer_pay_amount;


    private String	sign_type;


    private String	seller_id;


}
