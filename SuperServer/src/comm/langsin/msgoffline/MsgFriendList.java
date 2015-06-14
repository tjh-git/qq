package comm.langsin.msgoffline;

import java.util.ArrayList;
import java.util.List;

import comm.langsin.model.UserObject;
import comm.langsin.msg_log_reg.MsgHead;

public class MsgFriendList extends MsgHead {
	private int Friendsize;
	private List<UserObject> Friendlist = new ArrayList<UserObject>();

	public int getFriendsize() {
		return Friendsize;
	}

	public void setFriendsize(int friendsize) {
		Friendsize = friendsize;
	}

	public List<UserObject> getFriendlist() {
		return Friendlist;
	}

	public void setFriendlist(List<UserObject> friendlist) {
		Friendlist = friendlist;
	}
}
