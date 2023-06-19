package com.chenyue.combat.server.domain;

import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
* 用户账户余额记录表
* @TableName user_account
*/
public class UserAccount {

    /**
    * 主键
    */
    @ApiModelProperty("主键")
    private Integer id;
    /**
    * 用户账户id
    */
    @ApiModelProperty("用户账户id")
    private Integer userId;
    /**
    * 账户余额
    */
    @ApiModelProperty("账户余额")
    private BigDecimal amount;
    /**
    * 版本号字段
    */
    @ApiModelProperty("版本号字段")
    private Integer version;
    /**
    * 是否有效(1=是；0=否)
    */
    @ApiModelProperty("是否有效(1=是；0=否)")
    private Integer isActive;

    /**
    * 主键
    */
    public void setId(Integer id){
    this.id = id;
    }

    /**
    * 用户账户id
    */
    public void setUserId(Integer userId){
    this.userId = userId;
    }

    /**
    * 账户余额
    */
    public void setAmount(BigDecimal amount){
    this.amount = amount;
    }

    /**
    * 版本号字段
    */
    public void setVersion(Integer version){
    this.version = version;
    }

    /**
    * 是否有效(1=是；0=否)
    */
    public void setIsActive(Integer isActive){
    this.isActive = isActive;
    }

    /**
    * 主键
    */
    public Integer getId(){
    return this.id;
    }

    /**
    * 用户账户id
    */
    public Integer getUserId(){
    return this.userId;
    }

    /**
    * 账户余额
    */
    public BigDecimal getAmount(){
    return this.amount;
    }

    /**
    * 版本号字段
    */
    public Integer getVersion(){
    return this.version;
    }

    /**
    * 是否有效(1=是；0=否)
    */
    public Integer getIsActive(){
    return this.isActive;
    }

}
