package com.langsin.remove;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgRemoveMember extends MsgHead {
	private int Packetnum;
	private String Mainname;
	private String Pascketname;

	@Override
	public String toString() {
		return super.toString() + "MsgRemoveMember [Packetnum=" + Packetnum
				+ ", Mainname=" + Mainname + ", Pascketname=" + Pascketname
				+ "]";
	}

	public int getPacketnum() {
		return Packetnum;
	}

	public void setPacketnum(int packetnum) {
		Packetnum = packetnum;
	}

	public String getMainname() {
		return Mainname;
	}

	public void setMainname(String mainname) {
		Mainname = mainname;
	}

	public String getPascketname() {
		return Pascketname;
	}

	public void setPascketname(String pascketname) {
		Pascketname = pascketname;
	}

}
