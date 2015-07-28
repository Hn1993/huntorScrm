package com.huntor.scrm.model;

/**
 * 分类列表
 * Created by cao_cgq on 2015/5/7.
 */
public class Categorie {
    public int id ; //分类id
    public int parentId;//父分类Id
    public String name;//分类名称

    public boolean isExpand = false; //是否展开

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                '}';
    }
}
