package com.huntor.mscrm.app2.model;

import java.util.Date;

/**
 * 粉丝
 * Created by cao_cgq on 2015/5/6.
 */
public class Fans {

    public int id;//粉丝id
    public int group;//粉丝所在组
    public int targetId;//自定义分组ID
    public String nickName;//粉丝昵称
    public String name;//账户名称
    public String avatar;//粉丝头像
    public long registTime;//注册时间
    public long subscribeTime;//关注时间
    public boolean isCheck = false;//是否被选中
    public String productName;//最后一次购买的产品名

    @Override
    public String toString() {
        return "Fans{" +
                "id=" + id +
                ", group=" + group +
                ", targetId=" + targetId +
                ", nickName='" + nickName + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", registTime=" + registTime +
                ", subscribeTime=" + subscribeTime +
                ", isCheck=" + isCheck +
                ", productName='" + productName + '\'' +
                '}';
    }
}
