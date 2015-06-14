package com.langsin.remove;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgRemoveGroup extends MsgHead {// ÍË³öÈº×é
	private String Name;
	private int GroupNum;
	private String GroupName;

	@Override
	public String toString() {
		return "MsgRemoveGroup [Name=" + Name + ", GroupNum=" + GroupNum
				+ ", GroupName=" + GroupName + "]";
	}

	public int getGroupNum() {
		return GroupNum;
	}

	public void setGroupNum(int groupNum) {
		GroupNum = groupNum;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}
}
