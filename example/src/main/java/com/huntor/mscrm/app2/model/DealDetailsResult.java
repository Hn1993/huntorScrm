package com.huntor.mscrm.app2.model;

/**
 * 粉丝创建交易返回详情
 * Created by Administrator on 2015/5/6.
 */
public class DealDetailsResult {

    public int productId;//产品id
    public String productName;//产品名
    public String amount;//购买数量
    public String sn;//SN码

    @Override
    public String toString() {
        return "DealDetailsResult{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", amount='" + amount + '\'' +
                ", sn='" + sn + '\'' +
                '}';
    }
}
