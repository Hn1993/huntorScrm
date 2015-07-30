package com.huntor.mscrm.app2.model;

/**
 * Created by cao on 2015/5/26.
 */
/**
 * 结果数据集
 */
public class Data{
    public int group;//分组类型
    public int count;//分组内粉丝数量

    @Override
    public String toString() {
        return "Data{" +
                "group=" + group +
                ", count=" + count +
                '}';
    }
}