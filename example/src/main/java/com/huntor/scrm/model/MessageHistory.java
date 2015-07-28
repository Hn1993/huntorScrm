package com.huntor.scrm.model;

import java.util.List;

/**
 * 查询消息历史记录
 * Created by cao_cgq on 2015/5/7.
 */
public class MessageHistory extends Response {

    public List<Messages> messages; //消息列表
    public boolean nextPage;//分页标志

}
