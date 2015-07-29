package com.huntor.mscrm.app.model;

import java.util.Date;

/**
 * 消息信息
 * Created by Administrator on 2015/5/7.
 */
public class Messages {
    public int id;//	消息id;//	int
    public int groupId;//	消息组id;//	int
    public int type;//	消息类型;//	int	1文本 2图片 3地理位置 4事件 10图文
    public String content;//	消息内容;//	String	各种类型的消息内容都封装为JSON格式
    public int receiveFlag;//	收发标志;//	int	1接收的消息 2发送的消息
    public long sendingTime;//	发送时间;//	Date


    @Override
    public String toString() {
        return "Messages{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", receiveFlag=" + receiveFlag +
                ", sendingTime=" + sendingTime +
                '}';
    }
}
