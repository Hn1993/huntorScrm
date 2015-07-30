package com.huntor.mscrm.app2.model;

/**
 * Created by cao on 2015/6/26.
 */
public class ModifyFansParam {

    public int accountId;//
    public int fanId;
    public String name = "";
    public int gender;
    public int ageGroup;
    public String occupation = "";
    public String phone1 = "";
    public String phone2 = "";
    public String phone3 = "";

    @Override
    public String toString() {
        return "ModifyFansParam{" +
                "accountId=" + accountId +
                ", fanId=" + fanId +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", ageGroup=" + ageGroup +
                ", occupation='" + occupation + '\'' +
                ", phone1='" + phone1 + '\'' +
                ", phone2='" + phone2 + '\'' +
                ", phone3='" + phone3 + '\'' +
                '}';
    }
}
