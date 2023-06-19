package com.chenyue.combat.server.domain;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
* 用户每次成功提现时的金额记录表
* @TableName user_account_record
*/
public class UserAccountRecord {

    /**
    * 主键
    */
    @ApiModelProperty("主键")
    private Integer id;
    /**
    * 账户表主键id
    */
    @ApiModelProperty("账户表主键id")
    private Integer accountId;
    /**
    * 提现成功时记录的金额
    */
    @ApiModelProperty("提现成功时记录的金额")
    private BigDecimal money;
    /**
    * 
    */
    @ApiModelProperty("")
    private Date createTime;

    /**
    * 主键
    */
    public void setId(Integer id){
    this.id = id;
    }

    /**
    * 账户表主键id
    */
    public void setAccountId(Integer accountId){
    this.accountId = accountId;
    }

    /**
    * 提现成功时记录的金额
    */
    public void setMoney(BigDecimal money){
    this.money = money;
    }

    /**
    * 
    */
    public void setCreateTime(Date createTime){
    this.createTime = createTime;
    }


    /**
    * 主键
    */
    public Integer getId(){
    return this.id;
    }

    /**
    * 账户表主键id
    */
    public Integer getAccountId(){
    return this.accountId;
    }

    /**
    * 提现成功时记录的金额
    */
    public BigDecimal getMoney(){
    return this.money;
    }

    /**
    * 
    */
    public Date getCreateTime(){
    return this.createTime;
    }

}
