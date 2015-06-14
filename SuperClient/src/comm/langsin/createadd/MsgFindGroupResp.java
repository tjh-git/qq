package comm.langsin.createadd;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgFindGroupResp extends MsgHead{
	private int Groupnum;
	private String Groupname;
	private byte[] Grouphead;
	public int getGroupnum() {
		return Groupnum;
	}
	public void setGroupnum(int groupnum) {
		Groupnum = groupnum;
	}
	public String getGroupname() {
		return Groupname;
	}
	public void setGroupname(String groupname) {
		Groupname = groupname;
	}
	public byte[] getGrouphead() {
		return Grouphead;
	}
	public void setGrouphead(byte[] grouphead) {
		Grouphead = grouphead;
	}
	
}
