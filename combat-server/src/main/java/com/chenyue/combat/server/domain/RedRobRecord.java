package com.chenyue.combat.server.domain;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;

/**
* 抢红包记录
* @TableName red_rob_record
*/
public class RedRobRecord {

    /**
    * 
    */
    @ApiModelProperty("")
    private Integer id;
    /**
    * 用户账号
    */
    @ApiModelProperty("用户账号")
    private Integer userId;
    /**
    * 红包标识串
    */
    @ApiModelProperty("红包标识串")
    private String redPacket;
    /**
    * 红包金额（单位为分）
    */
    @ApiModelProperty("红包金额（单位为分）")
    private BigDecimal amount;
    /**
    * 时间
    */
    @ApiModelProperty("时间")
    private Date robTime;
    /**
    * 
    */
    @ApiModelProperty("")
    private Integer isActive;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(String redPacket) {
        this.redPacket = redPacket;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getRobTime() {
        return robTime;
    }

    public void setRobTime(Date robTime) {
        this.robTime = robTime;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}
