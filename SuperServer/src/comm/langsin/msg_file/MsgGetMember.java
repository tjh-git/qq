package comm.langsin.msg_file;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgGetMember extends MsgHead{//���Ⱥ��Ա
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
