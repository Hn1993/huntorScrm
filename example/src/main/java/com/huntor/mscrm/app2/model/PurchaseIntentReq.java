package com.huntor.mscrm.app2.model;

import java.util.Date;

/**
 * 用户购买意向
 * Created by cao_cgq on 2015/5/6.
 */
public class PurchaseIntentReq {
    public int productId;//产品id
    public String desc;//描述
    public long intentTime;//意向时间

    @Override
    public String toString() {
        return "PurchaseIntentReq{" +
                "productId=" + productId +
                ", desc='" + desc + '\'' +
                ", intentTime=" + intentTime +
                '}';
    }
}
