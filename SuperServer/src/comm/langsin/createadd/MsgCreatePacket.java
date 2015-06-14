package comm.langsin.createadd;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgCreatePacket extends MsgHead {
	private String Packetname;
	private String Packetsign;

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
