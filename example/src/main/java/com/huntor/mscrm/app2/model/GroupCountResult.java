package com.huntor.mscrm.app2.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cary.xi on 2015/5/3.
 */
public class GroupCountResult {

    private String code;//0,
    private String msg;//OK,
    private int countAdd;//add fans,
    private int countCommon;//common fans,
    private int countTall; // tall latent fans,
    private int countBought;//has bought fans,
    private JSONArray data;

    public void parseJSON(JSONObject jsonObject){

        try {
            code = jsonObject.getString("code");
            msg = jsonObject.getString("msg");
            data = jsonObject.getJSONArray("data");
            JSONObject temp = null;
            for(int i=0; i< data.length(); i++) {
                temp = (JSONObject) data.get(i);
                int groupId = temp.getInt("group");
                int count = temp.getInt("count");

                if(1 == groupId){
                    countAdd = count;
                } else if(2 == groupId){
                    countCommon = count;
                } else if(3 == groupId){
                    countTall = count;
                } else if(4 == groupId){
                    countBought = count;
                }
            }
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

    public int getCountAdd() {
        return countAdd;
    }

    public int getCountCommon() {
        return countCommon;
    }

    public int getCountTall() {
        return countTall;
    }

    public int getCountBought() {
        return countBought;
    }
}
