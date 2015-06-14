package com.langsin.remove;

import java.util.ArrayList;
import java.util.List;

import comm.langsin.model.UserObject;
import comm.langsin.msg_log_reg.MsgHead;

public class MsgFindMember extends MsgHead {
	private List<UserObject> User = new ArrayList<UserObject>();

	@Override
	public String toString() {
		return super.toString() + "MsgFindMember [User=" + User + "]";
	}

	public List<UserObject> getUser() {
		return User;
	}

	public void setUser(List<UserObject> user) {
		User = user;
	}

}
