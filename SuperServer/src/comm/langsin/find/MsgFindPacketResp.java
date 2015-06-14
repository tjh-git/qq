package comm.langsin.find;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgFindPacketResp extends MsgHead {
	private int Mainnum;
	private int Packetnum;
	private String Packetname;
	private byte[] Packethead;

	public int getMainnum() {
		return Mainnum;
	}

	public void setMainnum(int mainnum) {
		Mainnum = mainnum;
	}

	public int getPacketnum() {
		return Packetnum;
	}

	public void setPacketnum(int packetnum) {
		Packetnum = packetnum;
	}

	public String getPacketname() {
		return Packetname;
	}

	public void setPacketname(String packetname) {
		Packetname = packetname;
	}

	public byte[] getPackethead() {
		return Packethead;
	}

	public void setPackethead(byte[] packethead) {
		Packethead = packethead;
	}

}
