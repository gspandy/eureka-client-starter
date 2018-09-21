package com.springcloud.eureka.context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Iterator;
import java.util.List;

/**
 * Created by 李雷 on 2018/9/21.
 */
public class EurekaServerListDecorator extends DomainExtractingServerList {



    private Logger logger = LoggerFactory.getLogger(EurekaServerListDecorator.class);
    private String serviceId;
    private ConfigurableEnvironment environment;
    private StringRedisTemplate stringRedisTemplate;

    public EurekaServerListDecorator(ServerList<DiscoveryEnabledServer> list, IClientConfig clientConfig, boolean approximateZoneFromHostname, StringRedisTemplate stringRedisTemplate)
    {
        super(list, clientConfig, approximateZoneFromHostname);
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public List<DiscoveryEnabledServer> getInitialListOfServers() {
        List<DiscoveryEnabledServer> list = super.getInitialListOfServers();
        this.logger.debug("============>EurekaServerListDecorator.getInitialListOfServers===================" + JSONObject.toJSONString(list));
        return list;
    }


    @Override
    public List<DiscoveryEnabledServer> getUpdatedListOfServers() {
        List<DiscoveryEnabledServer> discoveryEnabledServers = super.getUpdatedListOfServers();

        Iterator<DiscoveryEnabledServer> iterator = discoveryEnabledServers.iterator();
        while (iterator.hasNext())
        {
            DiscoveryEnabledServer discoveryEnabledServer = iterator.next();
            String host = discoveryEnabledServer.getHost();
            String ipConfig = this.stringRedisTemplate.opsForValue().get("spring_cloud_register_white_list_of_" + this.serviceId);
            JSONObject ipConfigJson = JSONObject.parseObject(ipConfig);
            JSONArray whiteList = ipConfigJson.getJSONArray("whiteList");
            if (!whiteList.contains(host))
            {
                iterator.remove();
                this.logger.debug("current server can not send request to server:" + this.serviceId + " ip：" + host);
            }
        }
        this.logger.debug("============>EurekaServerListDecorator.getUpdatedListOfServers===================" + JSONObject.toJSONString(discoveryEnabledServers));
        return discoveryEnabledServers;

    }


    public void setEnvironment(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
