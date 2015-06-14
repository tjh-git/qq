package comm.langsin.find;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgFindUserResp extends MsgHead {// 查找好友应答消息
	private int Friendnum;
	private String Friendname;
	private byte[] Friendhead;

	public int getFriendnum() {
		return Friendnum;
	}

	public void setFriendnum(int friendnum) {
		Friendnum = friendnum;
	}

	public String getFriendname() {
		return Friendname;
	}

	public void setFriendname(String friendname) {
		Friendname = friendname;
	}

	public byte[] getFriendhead() {
		return Friendhead;
	}

	public void setFriendhead(byte[] friendhead) {
		Friendhead = friendhead;
	}

}
