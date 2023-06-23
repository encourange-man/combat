package com.chenyue.combat.server.domain;

import java.util.Date;
import java.util.Date;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
* 用户点赞记录表
* @TableName praise
*/
public class Praise {

    /**
    * 主键
    */
    @ApiModelProperty("主键")
    private Integer id;
    /**
    * 博客id
    */
    @ApiModelProperty("博客id")
    private Integer blogId;
    /**
    * 点赞人
    */
    @ApiModelProperty("点赞人")
    private Integer userId;
    /**
    * 点赞时间
    */
    @ApiModelProperty("点赞时间")
    private Date praiseTime;
    /**
    * 状态(1=正常；0=取消点赞)
    */
    @ApiModelProperty("状态(1=正常；0=取消点赞)")
    private Integer status;
    /**
    * 是否有效(1=是；0=否)
    */
    @ApiModelProperty("是否有效(1=是；0=否)")
    private Integer isActive;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
    * 主键
    */
    public void setId(Integer id){
    this.id = id;
    }

    /**
    * 博客id
    */
    public void setBlogId(Integer blogId){
    this.blogId = blogId;
    }

    /**
    * 点赞人
    */
    public void setUserId(Integer userId){
    this.userId = userId;
    }

    /**
    * 点赞时间
    */
    public void setPraiseTime(Date praiseTime){
    this.praiseTime = praiseTime;
    }

    /**
    * 状态(1=正常；0=取消点赞)
    */
    public void setStatus(Integer status){
    this.status = status;
    }

    /**
    * 是否有效(1=是；0=否)
    */
    public void setIsActive(Integer isActive){
    this.isActive = isActive;
    }

    /**
    * 创建时间
    */
    public void setCreateTime(Date createTime){
    this.createTime = createTime;
    }

    /**
    * 更新时间
    */
    public void setUpdateTime(Date updateTime){
    this.updateTime = updateTime;
    }


    /**
    * 主键
    */
    public Integer getId(){
    return this.id;
    }

    /**
    * 博客id
    */
    public Integer getBlogId(){
    return this.blogId;
    }

    /**
    * 点赞人
    */
    public Integer getUserId(){
    return this.userId;
    }

    /**
    * 点赞时间
    */
    public Date getPraiseTime(){
    return this.praiseTime;
    }

    /**
    * 状态(1=正常；0=取消点赞)
    */
    public Integer getStatus(){
    return this.status;
    }

    /**
    * 是否有效(1=是；0=否)
    */
    public Integer getIsActive(){
    return this.isActive;
    }

    /**
    * 创建时间
    */
    public Date getCreateTime(){
    return this.createTime;
    }

    /**
    * 更新时间
    */
    public Date getUpdateTime(){
    return this.updateTime;
    }

}
