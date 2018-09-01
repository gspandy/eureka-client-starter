package com.springcloud.eureka.configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Created by 李雷 on 2018/8/30.
 */

@AutoConfigureAfter(RibbonClientConfiguration.class)
public class PluginLoadBalanceConfiguration {
    @Value("${ribbon.client.name}")
    private String serviceId = "client";

    @Autowired
    private PropertiesFactory propertiesFactory;


    @Bean
    @ConditionalOnMissingBean
    public IRule ribbonRule(IClientConfig config) {
        if (this.propertiesFactory.isSet(IRule.class, serviceId)) {
            return this.propertiesFactory.get(IRule.class, config, serviceId);
        }

        ZoneAvoidanceRule rule = new ZoneAvoidanceRule();
        rule.initWithNiwsConfig(config);

        return rule;
    }

    @Bean
    public ILoadBalancer ribbonLoadBalancer(IClientConfig config, ServerList<Server> serverList, ServerListFilter<Server> serverListFilter, IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
        if (this.propertiesFactory.isSet(ILoadBalancer.class, serviceId)) {
            return this.propertiesFactory.get(ILoadBalancer.class, config, serviceId);
        }

        ZoneAwareLoadBalancer<?> loadBalancer = new ZoneAwareLoadBalancer<>(config, rule, ping, serverList, serverListFilter, serverListUpdater);

        return loadBalancer;
    }
}
