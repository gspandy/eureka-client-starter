package com.springcloud.eureka.configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.ServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import com.springcloud.eureka.decorator.EurekaServerListDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.eureka.EurekaRibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.inject.Provider;


/**
 * Created by 李雷 on 2018/8/30.
 */
@AutoConfigureAfter(EurekaRibbonClientConfiguration.class)
public class EurekaLoadBalanceConfiguration {


    @Value("${ribbon.client.name}")
    private String serviceId = "client";

    @Autowired
    private PropertiesFactory propertiesFactory;

    @Autowired
    private ConfigurableEnvironment environment;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public ServerList<?> ribbonServerList(IClientConfig config, Provider<EurekaClient> eurekaClientProvider){
        if (this.propertiesFactory.isSet(ServerList.class, serviceId)) {
            return this.propertiesFactory.get(ServerList.class, config, serviceId);
        }

        DiscoveryEnabledNIWSServerList discoveryServerList = new DiscoveryEnabledNIWSServerList(config, eurekaClientProvider);
        EurekaServerListDecorator serverList = new EurekaServerListDecorator(discoveryServerList, config, false,stringRedisTemplate);
        serverList.setEnvironment(environment);
        serverList.setServiceId(config.getClientName());

        return serverList;
    }
}
