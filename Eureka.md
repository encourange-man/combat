# Eureka

Spring Cloud Eureka是Spring Cloud Netflix微服务套件的一部分，基于Netflix Eureka做了二次封装，主要负责完成微服务实例的自动注册与发现，这也是微服务架构中的核心和基础功能。
Eureka所治理的每一个微服务实例被称为Provider Instance（提供者实例）。每一个Provider Instance包含一个Eureka Client组件（相当于注册中心客户端组件），
它的主要工作如下：
- 向Eureka Server完成Provider Instance的注册、续约和下线等操作，主要的注册信息包括服务名、机器IP、端口号、域名等。
- 向Eureka Server获取Provider Instance清单，并且缓存在本地。

实际上一个Eureka Server实例身兼三个角色：注册中心、服务提供者、注册中心客户端组件。主要原因如下：
- 对于所有Provider Instance而言，Eureka Server的角色是注册中心。
- 对于Eureka Server集群中其他的Eureka Server而言，Eureka Server的角色是注册中心客户端组件。
- Eureka Server对外提供REST接口的服务，当然也是服务提供者。


## Eureka server注册中心

### Eureka Server作为注册中心的配置项
Eureka Server作为注册中心的配置项以eureka.server.*作为前缀，大致如下：

（1）**eureka.server.enable-self-preservation**： 此配置项用于设置是否关闭注册中心的保护机制。什么是保护机制呢？Eureka Server会定时统计15分钟之内心跳成功的Provider实例的比例，
如果低于85%就会触发保护机制，处于保护状态的Eureka Server不剔除失效的服务提供者。enable-self-preservation的默认值为true表示开启自我保护机制。如果15分钟之内心跳成功的Provider实例的比例高于85%，那么Eureka Server仍然会处于正常状态。

（2）**eureka.server.eviction-interval-timer-in-ms**：配置Eureka Server清理无效节点的时间间隔，默认为60 000毫秒（60秒）。但是，如果Eureka Server处于保护状态，此配置就无效。

### Eureka Server作为服务提供者的配置项
Eureka Server自身是一种特殊的服务提供者，对外提供REST服务，所以需要配置一些Provider实例专属的配置项，这些配置项以eureka.instance.*作为前缀，大致如下：

（1）**eureka.instance.hostname**：设置当前实例的主机名称。

（2）**eureka.instance.appname**：设置当前实例的服务名称。默认值取自spring.application.name配置项的值，如果该选项没有值，eureka.instance.appname的值就为unknown。在Eureka服务器上，服务提供者的名称不区分字母大小写。

（3）**eureka.instance.ip-address**：设置当前实例的IP地址。

（4）**eureka.instance.prefer-ip-address**： 如果配置为true，就使用IP地址的形式来定义Provider实例的访问地址，而不是使用主机名来定义Provider实例的地址。如果同时设置了eureka.instance.ip-address选项，就使用该选项所配置的IP，否则自动获取网卡的IP地址作为Provider实例的访问地址。默认情况下，此配置项的值为false，即使用主机名来定义Provider实例的访问地址。

（5）**eureka.instance.lease-renewal-interval-in-seconds**：定义Provider实例到注册中心续约（心跳）的时间间隔，单位为秒，默认值为30秒。

（6）**eureka.instance.lease-expiration-duration-in-seconds**：定义Provider实例失效的时间，单位为秒，默认值为90秒。

（7）**eureka.instance.status-page-url-path**：定义Provider实例状态页面的URL，此选项配置的是相对路径，默认使用HTTP访问，如果需要使用HTTPS，就使用绝对路径配置。默认的相对路径为/info。

（8）**eureka.instance.status-page-url**：定义Provider实例状态页面的URL，此选项配置的是绝对路径。

（9）**eureka.instance.health-check-url-path**：定义Provider实例健康检查页面的URL，此选项配置的是相对路径，默认使用HTTP访问，如果需要使用HTTPS，就使用绝对路径配置。默认的相对路径为/health。

（10）**eureka.instance.health-check-url**：定义Provider实例健康检查页面的URL，此选项配置的是绝对路径。

### Eureka Server作为注册中心客户端组件的配置项
如果集群中配置了多个Eureka Server，那么节点和节点之间是对等的，在角色上一个Eureka Server同时也是其他Eureka Server实例的客户端，它的注册中心客户端组件角色的相关配置项以eureka.client.*作为前缀，大致如下:

（1）**eureka.client.register-with-eureka**：作为Eureka Client，eureka.client.register-with-eureka表示是否将自己注册到其他的Eureka Server上，默认为true。因为当前集群只有一个Eureka Server，所以需要设置成false。

（2）**eureka.client.fetch-registry**：作为Eureka Client，是否从Eureka Server获取注册信息，默认为true。因为本例是一个单点的Eureka Server，不需要同步其他Eureka Server节点数据，所以设置为false。

（3）**eureka.client.registery-fetch-interval-seconds**：作为Eureka Client，从Eureka Server获取注册信息的间隔时间，单位为秒，默认为30秒。

（4）**eureka.client.eureka-server-connect-timeout-seconds**：Eureka Client组件连接到Eureka Server的超时时间，单位为秒，默认值为5。

（5）**eureka.client.eureka-server-read-timeout-seconds**：Eureka Client组件读取Eureka Server信息的超时时间，单位为秒，默认值为8。

（6）**eureka.client.eureka-connection-idle-timeout-seconds**：Eureka Client组件到Eureka Server连接的空闲超时的时间，单位为秒，默认值为30。

（7）**eureka.client.filter-only-up-instances**：从Eureka Server获取Provider实例清单时是否进行过滤，只保留UP状态的实例，默认值为true。

（8）**eureka.client.service-url.defaultZone**：作为Eureka Client，需要向远程的Eureka Server自我注册，发现其他的Provider实例。此配置项用于设置Eureka Server的交互地址，在具有注册中心集群的情况下，多个Eureka Server的交互地址之间可以使用英文逗号分隔开。

Region和Zone可以理解为服务器的位置，Region可以理解为服务器所在的地域，Zone可以理解为服务器所处的机房。配置Region与Zone的主要目的是，在网络环境复杂的情况下帮助客户端就近访问需要的Provider实例。负载均衡组件Spring Cloud Ribbon的默认策略是优先访问与客户端处于同一个Zone中的服务端实例，只有当同一个Zone中没有可用服务端实例时，才会访问其他Zone中的实例。


> **服务提供者的创建和配置**

注册中心Eureka Server创建并启动之后，接下来介绍如何创建一个Provider并且注册到Eureka Server中，再提供一个REST接口给其他服务调用？

首先一个Provider要引入Eureka Client组件包的依赖
```xml
<dependencies>
    <!--Eureka Client组件 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>
```
备注：启动类上不需要配置`@EnableEurekaClinet`

Spring Cloud中的一个Provider实例身兼两个角色：服务提供者和注册中心客户端。所以，在Provider的配置文件中包含两类配置：Provider实例角色的相关配置和Eureka Client角色的相关配置。

Provider实例角色的相关配置:
```properties
# 配置Provider实例ID，默认值的格式：${spring.cloud.client.hostname}:${spring.application.name}:${server.port} （主机名：服务名称：服务端口）
eureka.instance.instance-id= ${spring.cloud.client.ip-address}:${server.port}

#设置当前实例的IP地址
eureka.instance.ip-address=

#配置为true，就使用IP地址的形式来定义Provider实例的地址，而不是使用主机名来定义Provider实例的地址
eureka.instance.prefer-ip-address=

#定义Provider实例状态页面的URL，此选项配置的是相对路径，默认使用HTTP访问，如果需要使用HTTPS，就使用绝对路径配置。默认的相对路径为/info
eureka.instance.status-page-url-path=

#定义Provider实例健康检查页面的URL，此选项配置的是相对路径，默认使用HTTP访问，如果需要使用HTTPS，就使用绝对路径配置。默认的相对路径为/health
eureka.instance.health-check-url-path=
```

Eureka Client角色的相关配置:
```properties
#这里设置为true，表示需要将Provider实例注册到Eureka Server
eureka.client.register-with-eureka=

#是否从Eureka Server获取注册信息，这里设置为true，表示需要从Eureka Server定期获取注册了的Provider实例清单
eureka.client.fetch-registry=

#作为Eureka Client，需要向远程的Eureka Server自我注册，查询其他的提供者。此配置项用于设置此客户端默认Zone（类似于默认机房）的Eureka Server的交互地址
eureka.client.service-url.defaultZone=
```

> **服务提供者续约（心跳）**

服务提供者的续约（心跳）保活由Provider Instance主动定期执行来实现，默认是开启的，默认每隔30秒一段时间就调用Eureka Server提供的REST保活接口，发送Provider Instance的状态信息给注册中心，告诉注册中心注册者还在正常运行。
```properties
#心跳时间，即服务续约间隔时间
eureka.instance.lease-renewal-interval-in-seconds=30

#租约有效期，如果Eureka Client未续约（心跳），Eureka Server将剔除该服务。默认90s，也就说instance实例有3次心跳重试的机会
eureka.instance.lease-expiration-duration-in-seconds=90
```

备注：

Eureka Server提供了多个和Provider Instance相关的Spring上下文ApplicationEvent，以方便应用程序进行监听：
- EurekaInstanceRenewedEvent：服务续约事件
- EurekaInstanceRegisteredEvent：服务注册事件
- EurekaInstanceCanceledEvent：服务下线事件
- EurekaRegistryAvailableEvent：Eureka注册中心启动事件
- EurekaServerStartedEvent：Eureka Server启动事件

> **服务提供者的健康状态**

Eureka Server并不记录 服务提供者Provider 的所有健康状况信息，仅仅维护了一个Provider清单。Eureka Client组件查询的Provider注册清单中，包含每一个Provider的健康状况的检查地址。通过该健康状况的地址可以查询Provider的健康状况。

通过 Eureka Server的 `/eureka/apps/{application}` 接口地址获取某个Provider实例的详细信息，

```xml
<application>
    <name>COMBAT-WEB</name>
    <instance>
        <instanceId>30.240.115.115:combat-web:8089</instanceId>
        <hostName>30.240.115.115</hostName>
        <app>COMBAT-WEB</app>
        <ipAddr>30.240.115.115</ipAddr>
        <!-- Provider实例本身发布的健康状态, UP-健康， DOWN, OUT_OF_SERVER, UNKONWN -->
        <status>UP</status>
        <overriddenstatus>UNKNOWN</overriddenstatus>
        <port enabled="true">8089</port>
        <securePort enabled="false">443</securePort>
        <countryId>1</countryId>
        
        <dataCenterInfo class="com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo">
            <name>MyOwn</name>
        </dataCenterInfo>
        
        .....
    
        <homePageUrl>http://30.240.115.115:8089/</homePageUrl>
        <!-- provider实例的状态URL地址       -->
        <statusPageUrl>http://30.240.115.115:8089/actuator/info</statusPageUrl>
        <!-- provider实例的健康信息URL       -->
        <healthCheckUrl>http://30.240.115.115:8089/actuator/health</healthCheckUrl>
        .....
    </instance>
</application>
```