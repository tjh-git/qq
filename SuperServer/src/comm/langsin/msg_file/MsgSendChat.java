package comm.langsin.msg_file;

import java.util.ArrayList;
import java.util.List;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgSendChat extends MsgHead{
	private List<byte[]> bq = new ArrayList<byte[]>();
	private List<Integer> bqsize = new ArrayList<Integer>();
	private String Name;
	private String Msg;
	private int Msgsize;
	private int bqnum;
	
	@Override
	public String toString() {
		return super.toString()+"MsgSendChat [bq=" + bq + ", bqsize=" + bqsize + ", Name="
				+ Name + ", Msg=" + Msg + ", Msgsize=" + Msgsize + ", bqnum="
				+ bqnum + "]";
	}
	public int getBqnum() {
		return bqnum;
	}
	public void setBqnum(int bqnum) {
		this.bqnum = bqnum;
	}
	public List<Integer> getBqsize() {
		return bqsize;
	}
	public void setBqsize(List<Integer> bqsize) {
		this.bqsize = bqsize;
	}
	public int getMsgsize() {
		return Msgsize;
	}
	public void setMsgsize(int msgsize) {
		Msgsize = msgsize;
	}
	public List<byte[]> getBq() {
		return bq;
	}
	public void setBq(List<byte[]> bq) {
		this.bq = bq;
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	
}
