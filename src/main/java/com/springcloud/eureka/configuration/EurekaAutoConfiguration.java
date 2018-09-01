package com.springcloud.eureka.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 李雷 on 2018/8/30.
 */
@Configuration
@RibbonClients(defaultConfiguration = {PluginLoadBalanceConfiguration.class,EurekaLoadBalanceConfiguration.class})
public class EurekaAutoConfiguration {
}
