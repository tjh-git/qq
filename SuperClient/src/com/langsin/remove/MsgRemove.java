package com.langsin.remove;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgRemove extends MsgHead{//É¾³ýºÃÓÑ
	private String name;

	@Override
	public String toString() {
		return super.toString()+"MsgRemove [name=" + name + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
