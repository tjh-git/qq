package com.langsin.remove;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgAddMember extends MsgHead {
	private int Packetnum;
	private String Name;
	private String Packetname;

	@Override
	public String toString() {
		return super.toString() + "MsgAddMember [Packetnum=" + Packetnum
				+ ", Name=" + Name + ", Packetname=" + Packetname + "]";
	}

	public int getPacketnum() {
		return Packetnum;
	}

	public void setPacketnum(int packetnum) {
		Packetnum = packetnum;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPacketname() {
		return Packetname;
	}

	public void setPacketname(String packetname) {
		Packetname = packetname;
	}

}
