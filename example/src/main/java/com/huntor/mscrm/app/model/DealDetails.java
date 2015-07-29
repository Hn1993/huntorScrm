package com.huntor.mscrm.app.model;

/**
 * 交易详情
 * Created by cao_cgq on 2015/5/6.
 */
public class DealDetails {
    public int productId;//购买产品id
    public int amount;//数量
    public int sn;//购买产品SN

    @Override
    public String toString() {
        return "DealDetails{" +
                "productId=" + productId +
                ", amount=" + amount +
                ", sn=" + sn +
                '}';
    }
}
