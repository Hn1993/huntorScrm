package com.huntor.mscrm.app.model;

/**
 * Created by cao on 2015/5/13.
 */
public class Product{
    public int id;//产品id
    public String name;//产品名称

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}