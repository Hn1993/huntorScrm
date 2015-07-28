package com.huntor.scrm.model;

import java.io.Serializable;

public class ChatInfo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6240488099748291325L;
	public int id;
	public int fid;//粉丝ID
	public int eid;//员工ID
	public String type;
	public String content;
	public long timestamp;
	/**
	 * 0 是收到的消息，1是发送的消息
	 */
	public int fromOrTo;// 0 是收到的消息，1是发送的消息
	@Override
	public String toString() {
		return "ChatInfoEntity [" +
				"id=" + id +
				", type=" + type +
				", eid=" + eid
				+ ", content=" + content
				+ ", timestamp=" + timestamp
				+ ", fromOrTo=" + fromOrTo + "]";
	}
}
