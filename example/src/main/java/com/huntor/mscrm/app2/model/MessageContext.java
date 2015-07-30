package com.huntor.mscrm.app2.model;

import java.io.Serializable;

/**
 * Created by cao on 2015/5/17.
 */
public class MessageContext implements Serializable {

    public int eid;//员工ID
    public int fid;//粉丝ID
    public int last_mid;//最新已读消息ID

    @Override
    public String toString() {
        return "Context{" +
                "eid=" + eid +
                ", fid=" + fid +
                ", last_mid=" + last_mid +
                '}';
    }
}
