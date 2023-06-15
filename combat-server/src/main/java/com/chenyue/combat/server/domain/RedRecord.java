package com.chenyue.combat.server.domain;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;

/**
* 发红包记录
* @TableName red_record
*/
public class RedRecord {

    /**
    * 
    */
    @ApiModelProperty("")
    private Long id;
    /**
    * 用户ID
    */
    @ApiModelProperty("用户ID")
    private Integer userId;
    /**
    * 人数
    */
    @ApiModelProperty("人数")
    private Integer total;
    /**
    * 总金额（单位分）
    */
    @ApiModelProperty("总金额（单位分）")
    private BigDecimal amount;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
    * 修改时间
    */
    @ApiModelProperty("修改时间")
    private Date updateTime;
    /**
    * 红包全局唯一标识ID
    */
    @ApiModelProperty("红包全局唯一标识ID")
    private String redPacket;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(String redPacket) {
        this.redPacket = redPacket;
    }
}
