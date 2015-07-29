package com.huntor.mscrm.app.model;

import java.util.Date;

/**
 * 用户购买意向
 * Created by cao_cgq on 2015/5/6.
 */
public class PurchaseIntent extends Response {
    public int id;//购买意向id
    public int productId;//产品id
    public int productName;//产品名
    public int desc;//描述
    public String intentTime;//意向时间  打算购买的截止时间

    @Override
    public String toString() {
        return "PurchaseIntent{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName=" + productName +
                ", desc=" + desc +
                ", intentTime=" + intentTime +
                '}';
    }
}
