package com.huntor.mscrm.app2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 粉丝详细信息
 * Created by cao_cgq on 2015/5/7.
 */
public class FanInfo {


    public String nickName;//	昵称	String
    public String city;//	所在城市	String
    public String province;//	所在省	String
    public boolean followStatus;//	关注状态	boolean	true已关注 false未关注
    public long subscribeTime;//	关注时间	Date
    public long lastInteractionTime;//	上次交互时间	Date
    public int interactionTimes;//	交互次数	int
    public String avatar;//	头像	String	头像图片url
    public int accountId;//	账户id	int
    public String realName;//	真实姓名	String
    public String gender;//	性别	String	m男 f女 n未知
    public int ageGroup;//年龄段
    public String occupation;//职业
    public String phone1;//	电话1	String
    public String phone2;    //电话2	String
    public String phone3;//电话3
    public List<TargetList> targetLists;//粉丝所在自定义分组
    public ArrayList<Deals> deals;//交易记录
    public ArrayList<PurchaseIntents> purchaseIntents;//购买意向记录
    public int fanId; //

    /***
     * 粉丝所在自定义分组
     */
    public static class TargetList {
        public int id;//自定义分组Id
        public String name;//自定义分组名称
        public int accountId;
    }

    /**
     * 交易记录
     */
    public static class Deals {
        public int id;//交易ID
        public double money; //总金额
        public long dealTime;//交易时间
        public List<Details> details; //交易明细
        public int accountId;
    }

    /**
     * 交易明细
     */
    public static class Details {
        public int productId;//产品id
        public String productName;//产品名
        public int amount;//购买数量
        public String sn;//SN码
        public int dealId;

    }

    /**
     * 购买意向
     */
    public static class PurchaseIntents {
        public int id;//	购买意向id	int
        public int productId;//	产品id	int
        public String productName;//	产品名	String
        public String desc;//	描述	String 自定义购买意向 对应不到产品，只有一个意向描述的情况
        public long intentTime;//	意向时间	Date	打算购买的截止时间

        public int accountId;
    }
}
