package comm.langsin.find;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgFindUser extends MsgHead {//查找好友请求消息
	private int Friendnum;

	public int getFriendnum() {
		return Friendnum;
	}

	public void setFriendnum(int friendnum) {
		Friendnum = friendnum;
	}

}
