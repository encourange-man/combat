package com.chenyue.combat.web;

import com.chenyue.combat.server.entity.dto.BloomDTO;
import com.chenyue.combat.web.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author chenyue
 * @Date 2023/6/12
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AllArgsConstructor
public class RedisTemplateTest {

    private final RedisTemplate redisTemplate;
    private final RedissonClient redissonClient;

    @Test
    public void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("name","chenyue");

        System.out.println(valueOperations.get("name"));
    }

    @Test
    public void testRedissonClient() throws IOException {
        log.info("redisson配置信息：{}", redissonClient.getConfig().toYAML());
    }


    @Test
    public void testBloomFilter() {
        //创建布隆过滤器组建
        final String key = "myBloomFilter";
        RBloomFilter<BloomDTO> bloomFilter = redissonClient.getBloomFilter(key);
        //初始化布隆过滤器，预计统计数据量1000，期望误报率为0.01
        bloomFilter.tryInit(1000, 0.01);

        BloomDTO bloomDTO = new BloomDTO(1, "1");
        BloomDTO bloomDTO1 = new BloomDTO(100, "100");
        BloomDTO bloomDTO2 = new BloomDTO(1000, "1000");
        BloomDTO bloomDTO3 = new BloomDTO(10000, "10000");

        //向布隆过滤器添加对象
        bloomFilter.add(bloomDTO);
        bloomFilter.add(bloomDTO1);
        bloomFilter.add(bloomDTO2);
        bloomFilter.add(bloomDTO3);

        //检查特定的元素在布隆过滤器中是否存在
        log.info("该布隆过滤器是否包含数据（1，\"1\"）:{}", bloomFilter.contains(new BloomDTO(1, "1")));
        log.info("该布隆过滤器是否包含数据（2，\"2\"）:{}", bloomFilter.contains(new BloomDTO(2, "2")));
        log.info("该布隆过滤器是否包含数据（11，\"11\"）:{}", bloomFilter.contains(new BloomDTO(11, "11")));
        log.info("该布隆过滤器是否包含数据（32323，\"32323\"）:{}", bloomFilter.contains(new BloomDTO(32323, "32323")));
        log.info("该布隆过滤器是否包含数据（1000，\"1000\"）:{}", bloomFilter.contains(new BloomDTO(1000, "1000")));
        log.info("该布隆过滤器是否包含数据（10000，\"10000\"）:{}", bloomFilter.contains(new BloomDTO(10000, "10000")));

    }


    @Test
    public void testRedissonLock(String redId, String userId) {
       //定义锁的名称
        final String lockKey = redId + userId + "_lock";
        //获取分布式锁实例
        RLock lock = redissonClient.getLock(lockKey);
        try{
            //尝试加锁，最多等待100秒，上锁后10秒自动释放锁
            boolean result = lock.tryLock(100,10, TimeUnit.SECONDS);
            if(result) {
                //从预计算的随机红包列表中，拿出一个红包

                //更新缓存中的红包个数-1

                //记录抢红包记录

                //将当前抢到红包的用户设置进缓存系统中，用于表示当前用户已经抢过红包了
            }

        }catch (Exception e) {
            //释放分布式锁，forceUnlock() 强制释放锁，
            if(lock != null) {
                lock.forceUnlock();
            }
            throw new BusinessException("系统异常-抢红包-分布式加锁失败！");
        }
    }
}

