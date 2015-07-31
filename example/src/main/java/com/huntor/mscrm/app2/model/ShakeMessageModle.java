package com.huntor.mscrm.app2.model;

/**
 * 摇一摇modle
 * Created by tangtang on 15/7/30.
 */
public class ShakeMessageModle {

  
    public int status;//1选中设备  2周围设备
    public int socialId;//设备所属公众号
    public int deviceId;//设备唯一id
    public String distance;//摇手机时与设备的距离
    public int empId;//设备所属员工
    public String fanRealId;//摇手机的粉丝的realId
    public int fanId;//摇手机的粉丝id
    public long timestamp;//时间，毫秒
    public int groupId;//消息组id
    public int isRead;//0 未读1已读

    public String nickName;//	昵称	String
    public String avatar;//	头像	String	头像图片url
    public String time;//时间 MM-dd HH:mm
}
