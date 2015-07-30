package com.huntor.mscrm.app2.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IDEA
 * User : SL
 * on 2015/5/6 0006
 * 18:06.
 */

/**
 * 知识库一级列表
 */
public class Categories {

    private String code;
    private String msg;
    private List<Entrys> list;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setList(List<Entrys> list) {
        this.list = list;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<Entrys> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "code=" + code +
                ", msg=" + msg +
                ", category.length=" + list.size() +
                "}";
    }

    public class Entrys {
        private int id;
        private int parentId;
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public int getParentId() {
            return parentId;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Categories.Categories{" +
                    "id=" + id +
                    ", parentId=" + parentId +
                    ", name=" + name + "}";
        }
    }
}
