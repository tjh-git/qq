package comm.langsin.createadd;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgAddGroupResp extends MsgHead {// 添加群组应答消息
	private byte State;
	private int Packetnum;
	private String Name;
	private String Packetname;

	public byte getState() {
		return State;
	}

	public void setState(byte state) {
		State = state;
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
