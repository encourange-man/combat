package com.chenyue.combat.web.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author chenyue
 * @Date 2023/6/12
 */
@EnableCaching
@Configuration
public class RedisConfig {

    @Autowired
    private Environment environment;

    /**
     * redisTemplate
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);


        //指定key序列化策略为String序列化，value为JDK自带的序列话策略
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //指定HashKey序列化策略为String序列化-针对hash散列存储
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        //初始实例化redisTempalte
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public ValueOperations<String, Object> redisValueOperate(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public RedissonClient config() {
        Config config = new Config();
        //单一节点模式
        config.useSingleServer()
                .setAddress(environment.getProperty("redisson.host.config"))
                .setKeepAlive(true);

        return Redisson.create(config);
    }

}
