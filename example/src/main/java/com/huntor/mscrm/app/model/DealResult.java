package com.huntor.mscrm.app.model;

import java.util.List;

/**
 * 交易返回记录和详情
 * Created by Administrator on 2015/5/6.
 */
public class DealResult extends Response {
    public Deal deal;//交易记录

    public class Deal{
        public int id;//交易id
        public double money;//总金额

        public List<DealDetailsResult> details;

    }
}
