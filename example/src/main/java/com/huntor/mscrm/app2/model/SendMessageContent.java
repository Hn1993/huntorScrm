package com.huntor.mscrm.app2.model;

/**
 * Created by Administrator on 2015/5/7.
 */
public class SendMessageContent {
    public int groupId;//消息组id
    public int type;//消息类型
    public String content;//消息内容

    @Override
    public String toString() {
        return "SendMessageContent{" +
                "groupId=" + groupId +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
