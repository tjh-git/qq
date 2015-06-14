package comm.langsin.createadd;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgAddFriendResp extends MsgHead {
	private byte State;
	private int Friendnum;
	private String Friendname;
	private String Friendsign;

	public byte getState() {
		return State;
	}

	public void setState(byte state) {
		State = state;
	}

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

	public String getFriendsign() {
		return Friendsign;
	}

	public void setFriendsign(String friendsign) {
		Friendsign = friendsign;
	}

}
