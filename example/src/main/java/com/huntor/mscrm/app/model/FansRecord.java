package com.huntor.mscrm.app.model;

public class FansRecord {
	public String nickName;// 昵称：（String）
	public int accountId;// 账户ID
	public String realName;// 真是姓名
	public String gender;// 性别
	public String city;// 城市
	public String province;// 省份
	public boolean followStatus;// 关注状态
	public long subscribeTime;// 关注时间
	public long lastInteractionTime;// 上次交互时间
	public int interactionTimes;// 交互次数

	@Override
	public String toString() {
		return "FansRecord [nickName=" + nickName + ", accountId=" + accountId
				+ ", realName=" + realName + ", gender=" + gender + ", city="
				+ city + ", province=" + province + ", followStatus="
				+ followStatus + ", subscribeTime=" + subscribeTime
				+ ", lastInteractionTime=" + lastInteractionTime
				+ ", interactionTimes=" + interactionTimes + "]";
	}

}
