package com.huntor.mscrm.app.model;

/**
 * !!-> FUCK GFW <-!!
 * Copyright on me only, coz u won't need my codes anyway.
 * Please maintain the codes with self descipline, means:
 * No meaningless codes, blank lines, trail spaces, and comments please.
 * Created by Leon Guan on 5/16/15.
 */
public class SendMsg {

    private String type;//消息类型，1文本 2图片 3声音 4视频 10图文

    /*
     * 消息内容，json格式
     * 不同类型的消息封装格式如下：
     * 
     * 文本：不变，直接是文本内容
     * 图片：
     * {
     *     "picUrl":"http://huntor.cn/kgjhk1.jpg"
     * }
     * 音频：
     * {
     *     "voiceUrl":"http://huntor.cn/kgjhk1.amr"
     * }
     * 视频：
     * {
     *     "video":{
     *         "videoUrl":"http://huntor.cn/kgjhk1.mp4",
     *         "thumbUrl":"http://faufghi1u.jpg",
     *         "title":"title1",
     *         "desc":"desc1"
     *     }
     * }
     * 图文：
     * {
     *     "articles":[
     *         {
     *             "title":"33123",
     *             "desc":"2222",
     *             "picUrl":"http://fsdfsdgsdg.png",
     *             "url":"http://www.baidu.com"
     *         },...
     *     ]
     * }
     */
    private String content;
    private int groupId;//消息组id
    private long timestamp;//发送时间，毫秒数
    private int fid;//粉丝id
    private int eid;//导购id
    private int socialId;//公众号id
    private int recordId;//消息发送ID(导购给微信发消息的信息id)

    public SendMsg() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getSocialId() {
        return socialId;
    }

    public void setSocialId(int socialId) {
        this.socialId = socialId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

}
