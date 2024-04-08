package com.chenyue.eurekaserver.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Author chenyue
 * @Date 2023/8/1
 */
@Slf4j
@Component
public class EurekaStateChangeListener {

    /**
     * 服务下线事件
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        log.info("appName:{}, serverId:{} 服务下线", event.getAppName(), event.getServerId());
    }

    /**
     * 服务注册事件
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        log.info("服务注册：{}",event.getInstanceInfo());
    }

    /**
     * 服务续约事件
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info("{} \t {} 服务续约", event.getServerId(), event.getAppName());
    }
}
