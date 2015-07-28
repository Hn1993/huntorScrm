package com.huntor.scrm.model;

/**
 * Created by cary.xi on 2015/5/4.
 */
public class MemberListInfo {

    private String headPortrait;//ͷ头像
    private String name;//名字
    private String time;//时间

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
}
