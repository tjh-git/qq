package comm.langsin.createadd;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgFindGroup extends MsgHead{
	private int Groupnum;

	public int getGroupnum() {
		return Groupnum;
	}

	public void setGroupnum(int groupnum) {
		Groupnum = groupnum;
	}
}
