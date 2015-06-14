package comm.langsin.createadd;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgAddGroup extends MsgHead {// 添加群组请求消息
	private int Packetnum;
	private String Pascketname;
	private String Name;
	private String Content;
	private String Sign;

	public int getPacketnum() {
		return Packetnum;
	}

	public void setPacketnum(int packetnum) {
		Packetnum = packetnum;
	}

	public String getPascketname() {
		return Pascketname;
	}

	public void setPascketname(String pascketname) {
		Pascketname = pascketname;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getSign() {
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

}
