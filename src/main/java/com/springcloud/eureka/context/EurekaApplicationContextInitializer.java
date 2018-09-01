package com.springcloud.eureka.context;



import com.springcloud.eureka.decorator.DiscoveryClientDecorator;
import com.springcloud.eureka.decorator.EurekaServiceRegistryDecorator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;


public class EurekaApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof DiscoveryClient) {
                    DiscoveryClient discoveryClient = (DiscoveryClient) bean;
                    return new DiscoveryClientDecorator(discoveryClient, applicationContext);
                } else {

                    if (bean instanceof EurekaServiceRegistry) {
                        EurekaServiceRegistry eurekaServiceRegistry = (EurekaServiceRegistry) bean;
                        return new EurekaServiceRegistryDecorator(eurekaServiceRegistry,applicationContext);
                    }

                    return bean;
                }
            }
        });
    }
}