package comm.langsin.msg_file;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgGetMember extends MsgHead{//获得群成员
	private int Packetnum;
	
	@Override
	public String toString() {
		return super.toString()+"MsgGetMember [Packetnum=" + Packetnum + "]";
	}

	public int getPacketnum() {
		return Packetnum;
	}

	public void setPacketnum(int packetnum) {
		Packetnum = packetnum;
	}
	
}
