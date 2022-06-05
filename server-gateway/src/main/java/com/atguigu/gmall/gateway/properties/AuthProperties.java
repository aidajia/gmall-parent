package com.atguigu.gmall.gateway.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {

    List<String> anyoneurls;

    List<String> denyurls;

    List<String> authurls;

    String loginPage;

}
