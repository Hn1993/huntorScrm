package com.huntor.mscrm.app2.model;

/**
 * 员工详情
 * Created by cao_cgq on 2015/5/6.
 */
public class EmpLoyeeInfo {
    public int id;//员工id
    public String number;//工号
    public String name;//姓名
    public int teamId;//所属团队id
    public String teamName;//所属团队名称
    public int organizationId;//所属门店id
    public String organizationName;//所属门店名称
    public int socialId;//相关公众号id
    public int employeeType;//员工类别
    public String phone;//电话
    public String qrcode;//二维码

    @Override
    public String toString() {
        return "EmpLoyeeInfo{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", organizationId=" + organizationId +
                ", organizationName='" + organizationName + '\'' +
                ", socialId=" + socialId +
                ", employeeType=" + employeeType +
                ", phone='" + phone + '\'' +
                ", qrcode='" + qrcode + '\'' +
                '}';
    }
}
