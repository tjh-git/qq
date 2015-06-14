package com.langsin.onoff;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgOffline extends MsgHead {
	private String Name;


	public String toString() {
		return super.toString() + "MsgOffline [Name=" + Name + "]";
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

}
