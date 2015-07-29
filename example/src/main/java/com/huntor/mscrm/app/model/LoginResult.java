package com.huntor.mscrm.app.model;

/**
 * Created by Administrator on 2015/5/5.
 */
public class LoginResult extends Response {

    private int userId;//USERID, 用户id
    private int empId;//EMPID,员工id
    private String empName;//EMPNAME  员工姓名
    private String token;//API令牌  所有接口中要用到    主要是为了防止多个设备登录


    public int getUserId() {
        return userId;
    }

    public int getEmpId() {
        return empId;
    }

    public String getEmpName() {
        return empName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "userId=" + userId +
                ", empId=" + empId +
                ", empName='" + empName + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
