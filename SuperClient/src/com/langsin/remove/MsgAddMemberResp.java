package com.langsin.remove;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgAddMemberResp extends MsgHead {
	private String Name;
	private String Packetname;
	private int Packetnum;
	private byte State;

	@Override
	public String toString() {
		return super.toString() + "MsgAddMemberResp [Name=" + Name
				+ ", Packetname=" + Packetname + ", Packetnum=" + Packetnum
				+ ", State=" + State + "]";
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

	public byte getState() {
		return State;
	}

	public void setState(byte state) {
		State = state;
	}

}
