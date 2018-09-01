package com.springcloud.eureka.decorator;

import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.List;

/**
 * Created by 李雷 on 2018/8/29.
 */
public class DiscoveryClientDecorator implements DiscoveryClient {
    public static final String DESCRIPTION = "Spring Cloud Eureka Discovery Client";

    private Logger logger = LoggerFactory.getLogger(DiscoveryClientDecorator.class);
    private DiscoveryClient discoveryClient;
    private ConfigurableApplicationContext applicationContext;
    private ConfigurableEnvironment environment;


    public DiscoveryClientDecorator(DiscoveryClient discoveryClient, ConfigurableApplicationContext applicationContext) {
        this.discoveryClient = discoveryClient;
        this.applicationContext = applicationContext;
        this.environment = applicationContext.getEnvironment();
    }


    @Override
    public String description() {
        return DESCRIPTION;
    }


    @Override
    public ServiceInstance getLocalServiceInstance() {
        ServiceInstance instance = discoveryClient.getLocalServiceInstance();
        logger.debug("============>DiscoveryClientDecorator.getLocalServiceInstance==================="+ JSONObject.toJSONString(instance));
        return instance;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {

        List<ServiceInstance> list =  discoveryClient.getInstances(serviceId);
        logger.debug("============>DiscoveryClientDecorator.getInstances==================="+ JSONObject.toJSONString(list));

        return list;
    }

    @Override
    public List<String> getServices() {
        List<String> services =  discoveryClient.getServices();
        logger.debug("============>DiscoveryClientDecorator.getServices==================="+ JSONObject.toJSONString(services));
        return services;
    }
}
