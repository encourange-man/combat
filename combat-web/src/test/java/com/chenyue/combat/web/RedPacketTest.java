package com.chenyue.combat.web;

import com.chenyue.combat.server.utils.RedPacketUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author chenyue
 * @Date 2023/6/14
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedPacketTest {

    @Test
    public void test() {
        Integer amount = 1000;
        Integer total = 10;
        List<Integer> list = RedPacketUtil.divideRedPackage(amount, total);
        log.info("总金额={}分，总个数={}", amount, total);

        int sum = 0;
        for (Integer integer : list) {
            log.info("随机金额：{} 分，即：{} 元", integer, new BigDecimal(integer.toString()).divide(new BigDecimal(100)));
            sum += integer;
        }
        log.info("所有的随机金额总数：{}", sum);

    }
}
