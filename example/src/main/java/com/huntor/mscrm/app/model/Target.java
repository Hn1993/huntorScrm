package com.huntor.mscrm.app.model;

import java.util.Date;

/**
 * 分组组信息
 * Created by cao_cgq on 2015/5/7.
 */
public class Target {
    public int id;
    public String name;
    public long updateTime;
    public int count;

    @Override
    public String toString() {
        return "Target{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", count=" + count +
                '}';
    }
}
