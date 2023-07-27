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


## 服务提供者的创建和配置
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