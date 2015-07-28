package com.huntor.scrm.model;

import java.io.Serializable;

public class SendMessage implements Serializable {

    public int recordId;//消息ID
    public String type;//消息类型，当前只支持文本
    public String content;//文本内容
    public int groupId;//消息组id
    public long timestamp;//发送时间，毫秒数
    public int fid;//粉丝id
    public int eid;//导购id
    public int socialId;//公众号id


    @Override
    public String toString() {
        return "SendMessage{" +
                "recordId=" + recordId +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", groupId=" + groupId +
                ", timestamp=" + timestamp +
                ", fid=" + fid +
                ", eid=" + eid +
                ", socialId=" + socialId +
                '}';
    }
}
