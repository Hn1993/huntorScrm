package com.huntor.scrm.model;

import java.util.List;

/**
 * Created by IDEA
 * User : SL
 * on 2015/5/7 0007
 * 14:09.
 */
public class ChatExtraGroup {

    private int id;
    private int parentId;
    private String name;

    private List<ChatExtraChild> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChatExtraChild> getList() {
        return list;
    }

    public void setList(List<ChatExtraChild> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ChatExtraKnowLedge:{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name=" + name +
                ", list.size()=" + list.size() +
                "}";
    }
}
