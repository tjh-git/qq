package comm.langsin.find;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgFindPacket extends MsgHead{
	private int Packetnum;

	public int getPacketnum() {
		return Packetnum;
	}

	public void setPacketnum(int packetnum) {
		Packetnum = packetnum;
	}
	
}
