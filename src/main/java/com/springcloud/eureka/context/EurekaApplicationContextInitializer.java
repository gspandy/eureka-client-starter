package com.springcloud.eureka.context;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springcloud.eureka.util.IpUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.core.StringRedisTemplate;


public class EurekaApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String SPRING_INETUTILS_PREFERRED_NETWORKS="spring.cloud.inetutils.preferred-networks[0]";
    private static final String SPRING_CLOUD_REGISTER_WHITE_LIST_OF_PREFIX="spring_cloud_register_white_list_of_";
    private static final String SPRING_APPLICATION_NAME="spring.application.name";
    private static final String EUREKA_WHITE_LIST_NAME="whiteList";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if(bean instanceof EurekaClientConfigBean){
                    ConfigurableEnvironment configurableEnvironment = applicationContext.getEnvironment();
                    String network = configurableEnvironment.getProperty(SPRING_INETUTILS_PREFERRED_NETWORKS);
                    String serviceId = configurableEnvironment.getProperty(SPRING_APPLICATION_NAME);
                    StringRedisTemplate stringRedisTemplate  =  applicationContext.getBean(StringRedisTemplate.class);

                    String ipConfig = stringRedisTemplate.opsForValue().get(SPRING_CLOUD_REGISTER_WHITE_LIST_OF_PREFIX+serviceId);
                    JSONObject ipConfigJson = JSONObject.parseObject(ipConfig);
                    JSONArray whiteList = ipConfigJson.getJSONArray(EUREKA_WHITE_LIST_NAME);
                    String host = IpUtil.getCurrentIp(network);
                    EurekaClientConfigBean eurekaClientConfigBean = (EurekaClientConfigBean) bean;
                    if(!whiteList.contains(host)){
                        eurekaClientConfigBean.setRegisterWithEureka(false);
                    }
                    return eurekaClientConfigBean;
                }else{
                    return bean;
                }

            }
        });
    }
}