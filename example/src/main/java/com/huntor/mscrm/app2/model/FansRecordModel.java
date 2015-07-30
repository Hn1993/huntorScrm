package com.huntor.mscrm.app2.model;

public class FansRecordModel {
	public String nickName;// 昵称：（String）
	public String avatar;//粉丝头像
	public int accountId;// 账户ID
	public int eid;//员工ID
	public String realName;// 真是姓名
	public String gender;// 性别
	public String city;// 城市
	public String province;// 省份
	public boolean followStatus;// 关注状态
	public long subscribeTime;// 关注时间
	public long lastInteractionTime;// 上次交互时间
	public int interactionTimes;// 交互次数
	public long time;
	public int group;//粉丝锁住固定分组

	@Override
	public String toString() {
		return "FansRecordModel{" +
				"nickName='" + nickName + '\'' +
				", avatar='" + avatar + '\'' +
				", accountId=" + accountId +
				", eid=" + eid +
				", realName='" + realName + '\'' +
				", gender='" + gender + '\'' +
				", city='" + city + '\'' +
				", province='" + province + '\'' +
				", followStatus=" + followStatus +
				", subscribeTime=" + subscribeTime +
				", lastInteractionTime=" + lastInteractionTime +
				", interactionTimes=" + interactionTimes +
				", time=" + time +
				", group=" + group +
				'}';
	}
}
