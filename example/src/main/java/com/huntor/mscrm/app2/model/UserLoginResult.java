package com.huntor.mscrm.app2.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SL
 * on 2015/4/27 0027 10:19.
 * By IDEA 14.0.2
 */
public class UserLoginResult {

    private String code;//0,
    private String msg;//OK,
    private int userId;//USERID,
    private int empId;//EMPID,
    private String empName;//EMPNAME

    public void parseJSON(JSONObject jsonObject){
        try {
            code = jsonObject.getString("code");//0,
            msg = jsonObject.getString("msg");//OK,
            userId = jsonObject.getInt("userId");//USERID,
            empId = jsonObject.getInt("empId");//EMPID,
            empName = jsonObject.getString("empName");//EMPNAME
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public int getUserId() {
        return userId;
    }

    public int getEmpId() {
        return empId;
    }

    public String getEmpName() {
        return empName;
    }
}
