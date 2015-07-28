package com.huntor.scrm.model;

public class Response {
	public String code;// 结果状态码  正确时为0
	public String msg;// 结果状态信息	正确时为OK

	@Override
	public String toString() {
		return "Response{" +
				"code='" + code + '\'' +
				", msg='" + msg + '\'' +
				'}';
	}
}
