package com.huntor.scrm.model;

import java.util.List;

/**
 * Created by Administrator on 2015/5/6.
 */
public class FansQueryResult extends Response {
    public List<Fans> fans;//粉丝列表
    public boolean nextPage;//是否有下一页

    @Override
    public String toString() {
        return "FansQueryResult{" +
                "fans=" + fans +
                ", nextPage=" + nextPage +
                '}';
    }
}
