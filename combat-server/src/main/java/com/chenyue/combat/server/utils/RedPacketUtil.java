package com.chenyue.combat.server.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 拼手气红包有两种计算方法，一种是预计算，一种是实时算。
 * <p>
 * 预计算在生成拼手气红包的时候，会提前根据总金额M和红包人数N，生成每个红包的金额，在抢红包的过程中，只需要从金额列表中取出，直至取完。这种方式需要占用额外的存储空间并且增加额外的I/O，在高并发场景下并不是最优的解决方案。
 * 实时算采用的是纯内存计算，不需要预算空间存储，实时性很高。
 *
 * @Author chenyue
 * @Date 2023/6/14
 */
public class RedPacketUtil {

    /**
     * @param totalAmount
     * @param totalPeopleNum
     * @return
     */
    public static List<Integer> divideRedPackage(Integer totalAmount, Integer totalPeopleNum) {
        //用于存储每次产生的小红包随机金额List -金额单位为分
        List<Integer> amountList = new ArrayList<>();

        //判断总金额和总个数参数的合法性
        if (totalAmount > 0 && totalPeopleNum > 0) {
            //记录剩余的总金额-初始化时金额即为红包的总金额
            Integer restAmount = totalAmount;
            //记录剩余的总人数-初始化时即为指定的总人数
            Integer restPeopleNum = totalPeopleNum;

            //不断循环遍历、迭代更新地产生随机金额，直到N-1>0
            Random random = new Random();
            for (int i = 0; i < totalPeopleNum - 1; i++) {
                //随机范围：[1，剩余人均金额的两倍)，左闭右开-amount即为产生的（保证没人至少分到1分元）
                //随机金额R-单位为分
                int amount = random.nextInt(restAmount / restPeopleNum * 2 - 1) + 1;
                //更新剩余的总金额M=M-R
                restAmount -= amount;
                //更新剩余的总人数N=N-1
                restPeopleNum--;
                //将产生的随机金额添加进列表List中
                amountList.add(amount);
            }
            //循环完毕，剩余的金额即为最后一个随机金额，也需要将其添加进列表中
            amountList.add(restAmount);
        }
        //将最终产生的随机金额列表返回
        return amountList;
    }
}
