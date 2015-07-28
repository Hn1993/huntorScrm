package com.huntor.scrm.model;

import java.util.List;

/**
 * Created by cao_cgq on 2015/5/6.
 */
public class ProductCategories {

    public int id;//类别id
    public String name;//类别名称
    public List<Product> products;//产品列表



    @Override
    public String toString() {
        return "ProductCategories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", products=" + products +
                '}';
    }
}
