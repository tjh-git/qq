package comm.langsin.createadd;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgCreatePacketResp extends MsgHead {
	private int Packetnum;
	private String Packetname;
	private String Packetsign;

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

	public String getPacketsign() {
		return Packetsign;
	}

	public void setPacketsign(String packetsign) {
		Packetsign = packetsign;
	}

}
