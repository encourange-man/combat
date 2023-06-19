# 分布式框架实战练手项目
- [分布式框架实战练手项目](#分布式框架实战练手项目)
  - [项目架构介绍](#项目架构介绍)
    - [集成日志框架](#集成日志框架)
    - [集成mybatis](#集成mybatis)
  - [Springboot集成Redis](#springboot集成redis)
  - [Redis应用场景之抢红包系统](#redis应用场景之抢红包系统)


## 项目架构介绍
- combat-web: web层，启动类所在层
- combat-server: 服务层，

模块的依赖关系：

combat-server -> combat-web

### 集成日志框架
市面上常见的日志框架有很多，它们可以被分为两类：日志门面（日志抽象层）和日志实现

| 日志分类     | 描述     | 举例     |
| -------- | -------- | -------- |
| 日志门面 | 为 Java 日志访问提供一套标准和规范的 API 框架, 其主要意义在于提供接口。 | JCL、SLF4j |
| 日志实现 | 日志门面的具体的实现 | Log4j、JUL、Log4j2、Logback |

目前我们常见的日志框架为**Log4j、Log4j2、Logback**这3种，并且现在很多的工具包里面都会自带日志框架，因此我们使用的使用要格外小心日志框架的冲突。

**三种日志框架的关系**
最先有 `Log4j`，然后因为 `Log4j` 有很大的性能问题因此该作者又重写了一个 `Logback` 并且抽象出一个日志门面 `slf4j` 。由于之前Log4j的问世，Apache公司就借鉴了Log4j的部分原理，自己重新写了一个日志框架 `Log4j2` 并且也实现了slf4j日志门面。

**SprongBoot使用的默认日志框架是 `Logback`**
springboot的启动项 `spring-boot-starter` 依赖 `spring-boot-starter-logging` （是Logback的日志实现）。因此SpringBoot默认集成了Logback。

默认情况下SpringBoot内部使用logback作为系统日志实现的框架，将日志输出到控制台，不会写到日志文件 。如果要编写除控制台输出之外的日志文件，则需在application.properties中设置。
```properties
#配置日志
logging.level.root=WARN
logging.level.org.springframework.web=DEBUG
logging.file=e:\\springboot\\info.log
logging.pattern.console=%d{yyyy/MM/dd-HH:mm:ss} [%thread] %-5level %logger- %msg%n
logging.pattern.file=%d{yyyy/MM/dd-HH:mm} [%thread] %-5level %logger- %msg%n
```

> Spring Boot官方推荐优先使用带有-spring 的文件名作为你的日志配置（如使用logback-spring.xml，而不是logback.xml），命名为logback-spring.xml的日志配置文件，spring boot可以为它添加一些spring boot特有的配置项。

**自定义日志配置**
根据不同的日志系统，你可以按如下规则组织配置文件名，就能被正确加载：
• Logback：logback-spring.xml, logback-spring.groovy, logback.xml, logback.groovy
• Log4j：log4j-spring.properties, log4j-spring.xml, log4j.properties, log4j.xml
• Log4j2：log4j2-spring.xml, log4j2.xml

**logback-spring.xml的配置说明**
```xml
<?xml version="1.0" encoding="utf-8"?>
<configuration>
    <!--
    子节点<property> ：用来定义变量值，它有两个属性name和value，通过<property>定义的值会被插入到logger上下文中，可以使“${}”来使用变量。
    -->
    <property name="APP_NAME" value="jobmd-extra-service"/>
    <property name="LOG_PATH" value="${user.home}/${APP_NAME}/logs" />  
	<property name="LOG_FILE" value="${LOG_PATH}/application.log" />
    <!--
     子节点<timestamp>：获取时间戳字符串，他有两个属性key和datePattern
    　　key: 标识此<timestamp> 的名字；
    　　datePattern: 设置将当前时间（解析配置文件的时间）转换为字符串的模式，遵循java.txt.SimpleDateFormat的格式。
     -->
    <timestamp key="TIMESTAMP" datePattern="yyyyMMddHHmmss"/>
    <!--
     子节点<contextName>：用来设置上下文名称，每个logger都关联到logger上下文，默认上下文名称为default。
     但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
     -->
    <contextName>${APP_NAME}-${TIMESTAMP}</contextName>
    
    <!-- 日志输入到命令行的appender定义
     子节点<appender>：负责写日志的组件，它有两个必要属性name和class; name指定appender名称，class指定appender的全限定名
       ConsoleAppender 把日志输出到控制台，有以下子节点：
    　　  <encoder>：对日志进行格式化。
    　　  <target>：字符串System.out(默认)或者System.err
     -->
    <appender name="CONSOLE-LOG" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>[%d{yyyy-MM-dd' 'HH:mm:ss.sss}] [%C] [%t] [%L] [%-5p] %m%n</pattern>
        </layout>
    </appender>

    <!--获取比info级别高(包括info级别)但除error级别的日志

    RollingFileAppender：滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件。有以下子节点：
      <file>：被写入的文件名，可以是相对目录，也可以是绝对目录，如果上级目录不存在会自动创建，没有默认值。
      <append>：如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true。
      <rollingPolicy>:当发生滚动时，决定RollingFileAppender的行为，涉及文件移动和重命名。属性class定义具体的滚动策略类
        class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy"： 最常用的滚动策略，它根据时间来制定滚动策略，既负责滚动也负责出发滚动。有以下子节点：
        <fileNamePattern>：必要节点，包含文件名及“%d”转换符，“%d”可以包含一个java.text.SimpleDateFormat指定的时间格式，
          如：%d{yyyy-MM}。如果直接使用 %d，默认格式是 yyyy-MM-dd。RollingFileAppender的file字节点可有可无，
          通过设置file，可以为活动文件和归档文件指定不同位置，当前日志总是记录到file指定的文件（活动文件），活动文件的
          名字不会改变；如果没设置file，活动文件的名字会根据fileNamePattern 的值，每隔一段时间改变一次。“/”或者“\”
          会被当做目录分隔符。
        <maxHistory>: 可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件。假设设置每个月滚动，
          且<maxHistory>是6，则只保存最近6个月的文件，删除之前的旧文件。注意，删除旧文件是，那些为了归档而创建的目录也会被删除。
          class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy"： 查看当前活动文件的大小，
          如果超过指定大小会告知RollingFileAppender 触发当前活动文件滚动。只有一个节点:
  　　　 <maxFileSize>:这是活动文件的大小，默认值是10MB。
  　　　 <prudent>：当为true时，不支持FixedWindowRollingPolicy。支持TimeBasedRollingPolicy，但是有两个限制，
          1不支持也不允许文件压缩，2不能设置file属性，必须留空。
        <triggeringPolicy >: 告知 RollingFileAppender 合适激活滚动。
  　　　   class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy" 根据固定窗口算法重命名文件的滚动策略。有以下子节点：
  　　　　　<minIndex>:窗口索引最小值
  　　　　　<maxIndex>:窗口索引最大值，当用户指定的窗口过大时，会自动将窗口设置为12。
  　　　　　<fileNamePattern>:必须包含“%i”例如，假设最小值和最大值分别为1和2，命名模式为 mylog%i.log,会产生
            归档文件mylog1.log和mylog2.log。还可以指定文件压缩选项，例如，mylog%i.log.gz 或者 没有log%i.log.zip
    -->
    <appender name="INFO-LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤掉error级别日志，其他级别的日志策略为ACCEPT -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>DENY</onMatch>
            <onMismatch>ACCEPT</onMismatch>
        </filter>
        <encoder>
            <pattern>[%d{yyyy-MM-dd' 'HH:mm:ss.sss}] [%C] [%t] [%L] [%-5p] %m%n</pattern>
        </encoder>
        <!--滚动策略-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_HOME}//%d-info.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>

    <!-- 输出error级别的日志 -->
    <appender name="ERROR-LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 只接受error级别日志 -->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
        <encoder>
            <pattern>[%d{yyyy-MM-dd' 'HH:mm:ss.sss}] [%C] [%t] [%L] [%-5p] %m%n</pattern>
        </encoder>
        <!--滚动策略-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_HOME}//%d-error.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>

    <!--
      FileAppender：把日志添加到文件，有以下子节点：
    　  <file>：被写入的文件名，可以是相对目录，也可以是绝对目录，如果上级目录不存在会自动创建，没有默认值。
    　  <append>：如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true。
    　　<encoder>：对记录事件进行格式化。（具体参数稍后讲解 ）
    　　<prudent>：如果是 true，日志会被安全的写入文件，即使其他的FileAppender也在向此文件做写入操作，效率低，默认是 false。
      -->
    <appender name="File" class="ch.qos.logback.core.FileAppender">
        <file>${LOG_HOME}/${appName}.file.log</file>
        <append>true</append>　
        <encoder>
            <pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern>
        </encoder>
    </appender>
    <!-- 异步输出 -->
    <appender name="ASYNC-INFO" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
        <queueSize>256</queueSize>
        <!-- 添加附加的appender,最多只能添加一个 -->
        <appender-ref ref="INFO-LOG"/>
    </appender>

    <appender name="ASYNC-ERROR" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
        <queueSize>256</queueSize>
        <!-- 添加附加的appender,最多只能添加一个 -->
        <appender-ref ref="ERROR-LOG"/>
    </appender>

    <!--
    子节点<loger>：用来设置某一个包或具体的某一个类的日志打印级别、以及指定<appender>。<loger>仅有一个name属性，一个可选的level和一个可选的addtivity属性。可以包含零个或多个<appender-ref>元素，标识这个appender将会添加到这个loger
      name: 用来指定受此loger约束的某一个包或者具体的某一个类。
      level: 用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL和OFF，还有一个特俗值INHERITED或者同义词NULL，代表强制执行上级的级别。 如果未设置此属性，那么当前loger将会继承上级的级别。
      addtivity: 是否向上级loger传递打印信息。默认是true。同<loger>一样，可以包含零个或多个<appender-ref>元素，标识这个appender将会添加到这个loger。
    -->
    <!--<logger name="com.zjw" level="INFO" addtivity="false"/>-->

    <!--
    子节点<root>:它也是<loger>元素，但是它是根loger,是所有<loger>的上级。只有一个level属性，因为name已经被命名为"root",且已经是最上级了。
  　　　　level: 用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL和OFF，不能设置为INHERITED或者同义词NULL。 默认是DEBUG。
    -->
    <root level="info">
        <appender-ref ref="CONSOLE-LOG"/>
        <appender-ref ref="ASYNC-INFO"/>
        <appender-ref ref="ASYNC-ERROR"/>
    </root>
</configuration>
```
因为springboot默认集成logback，如果要使用其他框架比如log4j，需要将自带的日志框架jar包移除掉，引入新的jar包
```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
		<exclusions>
			<exclusion>
				<artifactId>spring-boot-starter-logging</artifactId>
				<groupId>org.springframework.boot</groupId>
			</exclusion>
		</exclusions>
	</dependency>
	<!--引入log4j2框架jar包-->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-log4j2</artifactId>
	</dependency>
</dependencies>
```
### 集成mybatis
1.添加依赖jar包
```xml
<!--mybatis-->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.0</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.26</version>
</dependency>
```
2.配置datasource连接属性
```properties
#mysql
spring.datasource.url=jdbc:mysql://localhost:3306/chenyue?user=root&password=123456&useUnicode=true&characterEncoding=utf8&autoReconnect=true
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=123456

#mybaits
mybatis.mapper-locations=classpath*:/sqlmap/*.xml
mybatis.type-aliases-package=com.chenyue.combat.server.domain
```

## Springboot集成Redis
docker启动redis镜像
```shell
# 启动redis镜像
docker run -d --name redis -p 6379:6379 redis 

#后台进入redis容器
docker exec -it <image_id> /bin/bash

redis-cli -h 127.0.0.1 -p 6379
```

docker 启动mysql镜像
```shell
# 启动docker镜像，MYSQL_ROOT_PASSWORD设置root用户的登陆密码 
 docker run -d --name mysql_0 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 --privileged mysql
# 进入mysql容器
docker exec -it <image_id> /bin/bash
# 登陆root用户
mysql -uroot -p123456
#修改root可以通过任何客户端链接
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
```

## Redis应用场景之抢红包系统
[二倍均值随机算法之抢拼手气红包场景应用](https://developer.aliyun.com/article/1166688)


数据表设计：
```sql
--发红包记录表
CREATE TABLE `red_record` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `red_packet` varbinary(255) DEFAULT NULL COMMENT '红包全局唯一标识ID',
  `total` int DEFAULT NULL COMMENT '人数',
  `amount` double DEFAULT NULL COMMENT '总金额（单位分）',
  `is_active` tinyint DEFAULT NULL COMMENT '是否有效，1-有效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  comment='发红包记录';

--红包明细金额表
CREATE TABLE `red_detail` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `record_id` int(11) NOT NULL COMMENT '包记录id', 
    `amount` decimal(8,2) DEFAULT NULL COMMENT '金额（单位为分）',
    `is_active` tinyint(4) DEFAULT '1',
    `create_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB   COMMENT='红包明细金额';

-- 抢红包记录表
CREATE TABLE `red_rob_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户账号',
  `red_packet` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '红包标识串',
   `amount` decimal(8,2) DEFAULT NULL COMMENT '红包金额（单位为分）',
  `rob_time` datetime DEFAULT NULL COMMENT '时间',
  `is_active` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='抢红包记录';
```

## Redis分布式锁
分布式锁的要求：
- 排他性：保证在分布式情况，共享资源同一时间只能被一台机器的一个线程执行
- 避免死锁：锁被获取后，在一段时间后，一定要能被释放
- 高可用：获取和释放锁，要保证高可用且性能要佳
- 可重入：
- 公平性（可选）：保证来自不同机器的并发线程可以公平获取到锁

目前业界也提供了多种可靠的方式实现分布式锁，常见的方式有：
- 基于数据库级别的乐观锁：主要是通过在查询、操作共享数据记录时带上一个标识字段version，通过version来控制每次对数据记录执行的更新操作。
- 基于数据库级别的悲观锁：在这里以MySQL的InnoDB引擎为例，它主要是通过在查询共享的数据记录时加上`for Update`字眼，表示该共享的数据记录已经被当前线程锁住了（行级别锁、表级别锁），只有当该线程操作完成并提交事务之后，才会释放该锁，从而其他线程才能获取到该数据记录
- 基于Redis的原子操作：主要是通过Redis提供的原子操作`SETNX`与`EXPIRE`来实现。`SETNX`表示只有当Key在Redis不存在时才能设置成功，通常这个Key需要设计为与共享的资源有联系，用于间接地当作“锁”，并采用EXPIRE操作释放获取的锁。
- 基于ZooKeeper的互斥排它锁：这种机制主要是通过ZooKeeper在指定的标识字符串（通常这个标识字符串需要设计为跟共享资源有联系，即可以间接地当作“锁”）下维护一个临时有序的节点列表Node List，并保证同一时刻并发线程访问共享资源时只能有一个最小序号的节点（即代表获取到锁的线程），该节点对应的线程即可执行访问共享资源的操作

### 基于数据库实现分布式锁
以“用户提现金额”为例子。在高并发场景下，用户同时不间断的进行提现操作，很可能出现账户余额为负的情况。下面以基于数据库的分布式锁的方式，实现这个场景操作。
```sql
CREATE TABLE `user_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户账户id',
  `amount` decimal(10,4) NOT NULL COMMENT '账户余额',
  `version` int(11) DEFAULT '1' COMMENT '版本号字段',
  `is_active` tinyint(11) DEFAULT '1' COMMENT '是否有效(1=是；0=否)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账户余额记录表';

CREATE TABLE `user_account_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_id` int(11) NOT NULL COMMENT '账户表主键id',
  `money` decimal(10,4) DEFAULT NULL COMMENT '提现成功时记录的金额',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户每次成功提现时的金额记录表';
```
#### 数据库-乐观锁
乐观锁，通常是采用“版本号version”机制进行实现。当前线程取出数据记录时，会顺带把版本号字段version的值取出来，最后在更新该数据记录时，将该version的取值作为更新的条件。
当更新成功之后，同时将版本号version的值加1，而其他同时获取到该数据记录的线程在更新时由于version已经不是当初获取的那个数据记录，因而将更新失败， 从而避免并发多线程访问
共享数据时出现数据不一致的现象。
```sql
update table set key=value, version=version+1 where id=#{id} and version= #{version};
```

核心代码：
```java
@Slf4j
@Service
public class DataBaseLockServiceImpl implements DataBaseLockService {
  @Resource
  private UserAccountMapper userAccountMapper;
  
  @Resource
  private UserAccountRecordMapper userAccountRecordMapper;

  /**
   * 提现-基于数据库的乐观锁
   * @param dto
   */
  @Override
  public void takeMoneyWithOptimisticLock(UserCountDTO dto) {
    //查询用户的账户信息
    UserAccount userAccount = userAccountMapper.selectByUserId(dto.getUserId());

    //账户余额是否够，如果够，进行提现
    if(Objects.nonNull(userAccount) && userAccount.getAmount().doubleValue() >= dto.getAmount()) {
      //基于乐观锁更新账户余额，保证查询和更新的原子操作
      int res = userAccountMapper.updateAmountByVersion(dto.getAmount(), userAccount.getId(), userAccount.getVersion());

      if(res > 0) {
        //插入提交申请提现记录
        UserAccountRecord userAccountRecord = new UserAccountRecord();
        userAccountRecord.setAccountId(userAccount.getId());
        userAccountRecord.setMoney(BigDecimal.valueOf(dto.getAmount()));
        userAccountRecordMapper.insertSelective(userAccountRecord);
        log.info("当前提取金额为：{}, 账户余额为：{}", dto.getAmount(), userAccount.getAmount());
      }else {
        throw new BusinessException("账户不存在或者账户余额不足！");
      }
    }
  }
}
```

```xml
<update id="updateAmountByVersion">
    update user_account set amount = amount - #{money}, version = #{version} + 1
    where id = #{id} and version = #{version} and amount > 0 and (amount - #{money}) >= 0
</update>
```

另外，使用jmeter在QPS 2000的场景测试如下的sql也是可行的：
```sql
<update id="updateAmount" >
    update user_account set amount = amount - #{money}
    where id = #{id} and amount > 0 and (amount - #{money}) >= 0
</update>
```
因为，虽然查询余额和扣除余额不是一个原子操作，但是Innodb的行锁保证同一时刻只一个线程执行update操作，且执行时因为有amount>0 和
(amount - #{money}) >= 0 的限制，所以保证不会出现多扣的情况。

### 数据库-悲观锁
悲观锁，即每次并发线程在获取数据的时候认为其他线程会对数据进行修改，因而每次在获取数据时都会上锁，而其他线程访问该数据的时候就会发生阻塞的现象，最终只有当前线程释放了该共享资源的锁，其他线程才能获取到锁，并对共享资源进行操作。
比如在关系型数据库中，行锁、表锁、读锁和写锁等，都是类似悲观锁的机制，在进行操作之前先上锁。
```sql
select 字段列表 from 数据库表 for update
```
缺点：
- 由于每个线程在查询数据的时候都需要上锁，只有当该线程对该共享资源操作完毕并释放锁之后，其他正在等待中的线程才能获取到锁。这种方式将会造成大量的线程发生堵塞的现象，在某种程度上会对DB（数据库）服务器造成一定的压力

所以，基于数据库级别的悲观锁适用于并发量不大的情况，特别是“读”请求数据量不大的情况。
