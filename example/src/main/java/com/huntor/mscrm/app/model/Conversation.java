package com.huntor.mscrm.app.model;

import java.util.List;

/**
 * Created by Administrator on 2015/5/6.
 */
public class Conversation extends Response {

    public List<Conversations> cvsts; //会话列表   与导购相关的所有会话列表
    public boolean nextPage;//是否有下一页

    /***
     * 会话记录
     */
    public class Conversations{
        public int fanId; //粉丝ID
        public String nickName;//粉丝昵称
        public String avatar;//粉丝头像
        public int msgId;//最近一条消息id
        public int groupId;//最近消息组id
        public String msgContent;//最近一条消息内容
        public String updateTime;//最近一次交互时间


        @Override
        public String toString() {
            return "Conversations{" +
                    "fanId=" + fanId +
                    ", nickName='" + nickName + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", msgId=" + msgId +
                    ", groupId=" + groupId +
                    ", msgContent='" + msgContent + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "cvsts=" + cvsts +
                ", nextPage=" + nextPage +
                '}';
    }
}
