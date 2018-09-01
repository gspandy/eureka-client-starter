package com.springcloud.eureka.decorator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by 李雷 on 2018/8/30.
 */


public class EurekaServiceRegistryDecorator extends EurekaServiceRegistry {

    private Logger logger = LoggerFactory.getLogger(EurekaServiceRegistryDecorator.class);

    private EurekaServiceRegistry serviceRegistry;
    private ConfigurableApplicationContext applicationContext;

    public EurekaServiceRegistryDecorator(EurekaServiceRegistry serviceRegistry, ConfigurableApplicationContext applicationContext) {
        this.serviceRegistry = serviceRegistry;
        this.applicationContext = applicationContext;
    }

    /**
     * 注册时过滤逻辑
     */
    @Override
    public void register(EurekaRegistration registration) {
        String serviceId = registration.getServiceId();
        String host = registration.getHost();
        StringRedisTemplate stringRedisTemplate = applicationContext.getBean("stringRedisTemplate",StringRedisTemplate.class);
        String ipConfig = stringRedisTemplate.opsForValue().get("spring_cloud_register_white_list_of_"+serviceId);
        JSONObject ipConfigJson = JSONObject.parseObject(ipConfig);
        JSONArray whiteList = ipConfigJson.getJSONArray("whiteList");
        if(whiteList.contains(host)){
            serviceRegistry.register(registration);
            logger.info("service："+serviceId+" machine ip："+host+ " register eureka success");
        }else{
            logger.info("service："+serviceId+" machine ip："+host+ " register eureka fail");
        }
    }


    @Override
    public void deregister(EurekaRegistration eurekaRegistration) {
        logger.info("====================EurekaServiceRegistryDecorator.deregister====================");
        serviceRegistry.deregister(eurekaRegistration);
    }

    @Override
    public void setStatus(EurekaRegistration registration, String status) {

        logger.info("====================EurekaServiceRegistryDecorator.setStatus====================status:"+status);
        serviceRegistry.setStatus(registration, status);
    }

    @Override
    public Object getStatus(EurekaRegistration registration) {
        Object status = serviceRegistry.getStatus(registration);
        logger.info("====================EurekaServiceRegistryDecorator.getStatus====================status:"+JSONObject.toJSONString(status));
        return status;
    }

    @Override
    public void close() {
        logger.info("====================EurekaServiceRegistryDecorator.close====================");
        serviceRegistry.close();
    }
}
