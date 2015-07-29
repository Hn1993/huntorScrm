package com.huntor.mscrm.app.model;

import java.util.List;

/**
 * 员工固定分组中的粉丝列表
 * Created by cao_cgq on 2015/5/6.
 */
public class FansGroupResult extends Response {
    public List<Fans> fans;//粉丝列表
    public boolean nextPage;//是否有下一页

    @Override
    public String toString() {
        return "FansGroupResult{" +
                "fans=" + fans +
                ", nextPage=" + nextPage +
                '}';
    }
}
