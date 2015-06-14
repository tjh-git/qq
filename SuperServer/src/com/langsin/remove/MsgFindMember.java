package com.langsin.remove;

import java.util.ArrayList;
import java.util.List;

import comm.langsin.model.UserObject;
import comm.langsin.msg_log_reg.MsgHead;

public class MsgFindMember extends MsgHead {
	private List<UserObject> User = new ArrayList<UserObject>();
	private int size;


	public String toString() {
		return super.toString() + "MsgFindMember [User=" + User + ", size="
				+ size + "]";
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<UserObject> getUser() {
		return User;
	}

	public void setUser(List<UserObject> user) {
		User = user;
	}

}
