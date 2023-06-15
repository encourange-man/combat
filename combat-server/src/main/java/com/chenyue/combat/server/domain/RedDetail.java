package com.chenyue.combat.server.domain;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
* 红包明细金额
* @TableName red_detail
*/
public class RedDetail {

    /**
    * 
    */
    private Integer id;
    /**
    * 包记录id
    */
    private Long recordId;
    /**
    * 金额（单位为分）
    */
    private BigDecimal amount;
    /**
    * 
    */
    @ApiModelProperty("")
    private Integer isActive;
    /**
    * 
    */
    @ApiModelProperty("")
    private Date createTime;
    /**
    * 修改时间
    */
    @ApiModelProperty("修改时间")
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
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
}
