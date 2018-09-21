package com.springcloud.eureka.context;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 李雷 on 2018/9/21.
 */
@Configuration
@RibbonClients(defaultConfiguration = {EurekaLoadBalanceConfiguration.class})
public class EurekaAutoConfiguration {
}
