package com.huntor.mscrm.app2.model;

import java.io.Serializable;

public class MessageRecordModel implements Serializable, Comparable<MessageRecordModel> {
    public int artificialStatus;//人工状态 4进入人工  5人工下  6退出人工
    public int msgId;//消息id
    public String type;//消息类型   1 文本
    public String content;//文本消息内容
    public long timestamp;//发送时间，毫秒数
    public int fid;//粉丝ID
    public int eid;//导购ID
    public int groupId;//消息组id
    public int isRead;//已读未读  1已读  0未读
    public long time; //时间
    /**
     * 发送还是接收
     * <ol>
     * <li>0:接收</li>
     * <li>1:发送</li>
     * </ol>
     */
    public int sendOrReceive;//发送还是接收 1   or  0
    public int successOrFail;//成功 Or 失败  1  or 0

    @Override
    public String toString() {
        return "MessageRecordModel{" +
                "artificialStatus=" + artificialStatus +
                ", msgId=" + msgId +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", fid=" + fid +
                ", eid=" + eid +
                ", groupId=" + groupId +
                ", isRead=" + isRead +
                ", sendOrReceive=" + sendOrReceive +
                ", successOrFail=" + successOrFail +
                '}';
    }

    @Override
    public int compareTo(MessageRecordModel arg0) {
        if (time < arg0.time) {
            return -1;
        } else if (time > arg0.time) {
            return 1;
        } else {
            return 0;
        }

    }
}
