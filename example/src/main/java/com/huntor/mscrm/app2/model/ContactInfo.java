package com.huntor.mscrm.app2.model;

/**
 * Created by IDEA
 * User : SL
 * on 2015/5/16 0016
 * 18:09.
 */
public class ContactInfo {
    public int id;
    public String type;
    public String content;
    public long timestamp;
    public int fid;
    public int eid;
    /**
     * 0 是收到的消息，1是发送的消息
     */
    public int fromOrTo;

    @Override
    public String toString() {
        return "ContactInfo{" +
                "id:" + id +
                "type:" + type +
                "content:" + content +
                "timestamp:" + timestamp +
                "fid:" + fid +
                "eid:" + eid +
                "fromOrTo:" + fromOrTo +
                "}";
    }
}
