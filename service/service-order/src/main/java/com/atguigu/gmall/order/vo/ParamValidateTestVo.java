package com.atguigu.gmall.order.vo;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class ParamValidateTestVo {


    @Length(max = 256,min = 3,message = "名字长度必须在3-256之间")
    private String name; //256



    @Min(value = 0,message = "年龄不能小于0")
    @Max(value = 150,message = "年龄不能超过150")
    @NotNull(message = "年龄是必填字段")
    private Integer age;


//    @NotBlank // "    "，不能是空白符
//    @NotEmpty //不是null，不是空串  ""
//    @NotNull //不能null，
//    @Negative
    @Email(message = "邮箱格式必须满足")
    private String email;




//    @Past //这个必须是一个过去时间
//    @Future //这个必须是一个未来时间
//    private Date birth;
//
//    @Pattern(regexp = "") //自定义规则
//    private String msg;




}
