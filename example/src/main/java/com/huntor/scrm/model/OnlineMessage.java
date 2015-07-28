package com.huntor.scrm.model;

/**
 * Created by cao on 2015/5/15.
 */
public class OnlineMessage {
    public int id;  //消息编号，自增长
    public String type;// text|image|voice，消息类型
    public String content;//消息内容，当type为text时表示文本内容，当消息为image|voice时，内容为对应的media url
    public long timestamp;//时间戳
    public int fid;//粉丝ID
    public int eid;//导购ID

    @Override
    public String toString() {
        return "OnlineMessage{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", fid=" + fid +
                ", eid=" + eid +
                '}';
    }
}
