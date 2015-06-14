package com.langsin.onoff;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgOnline extends MsgHead {// 上下线通知
	private String Name;

	public String toString() {
		return super.toString() + "MsgOnline [Name=" + Name + "]";
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

}
