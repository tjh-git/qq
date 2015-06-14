package comm.langsin.msgoffline;

import java.util.ArrayList;
import java.util.List;

import comm.langsin.model.PacketObject;
import comm.langsin.msg_log_reg.MsgHead;

public class MsgGroupList extends MsgHead {// 群组列表
	private int Groupsize;
	private List<PacketObject> Packetlist = new ArrayList<PacketObject>();

	public int getGroupsize() {
		return Groupsize;
	}

	public void setGroupsize(int groupsize) {
		Groupsize = groupsize;
	}

	public List<PacketObject> getPacketlist() {
		return Packetlist;
	}

	public void setPacketlist(List<PacketObject> packetlist) {
		Packetlist = packetlist;
	}

}
