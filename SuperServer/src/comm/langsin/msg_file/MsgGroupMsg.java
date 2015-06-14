package comm.langsin.msg_file;

import java.util.ArrayList;
import java.util.List;

import comm.langsin.model.UserObject;
import comm.langsin.msg_log_reg.MsgHead;

public class MsgGroupMsg extends MsgHead {
	private List<byte[]> bq = new ArrayList<byte[]>();
	private List<Integer> bqsize = new ArrayList<Integer>();
	private List<Integer> Member = new ArrayList<Integer>();
	private List<UserObject> membeList = new ArrayList<UserObject>();
	private List<Object> File = new ArrayList<Object>();
	private String Name;
	private String Msg;
	private int Msgsize;
	private int bqnum;
	private int Packetnum;
	private byte State;
	private String Packetname;
	private int Filenum;
	@Override
	public String toString() {
		return super.toString()+"MsgGroupMsg [bq=" + bq + ", bqsize=" + bqsize + ", Member="
				+ Member + ", membeList=" + membeList + ", File=" + File
				+ ", Name=" + Name + ", Msg=" + Msg + ", Msgsize=" + Msgsize
				+ ", bqnum=" + bqnum + ", Packetnum=" + Packetnum + ", State="
				+ State + ", Packetname=" + Packetname + ", Filenum=" + Filenum
				+ "]";
	}

	public int getFilenum() {
		return Filenum;
	}

	public void setFilenum(int filenum) {
		Filenum = filenum;
	}

	public List<Object> getFile() {
		return File;
	}

	public void setFile(List<Object> file) {
		File = file;
	}

	public String getPacketname() {
		return Packetname;
	}

	public void setPacketname(String packetname) {
		Packetname = packetname;
	}

	public int getPacketnum() {
		return Packetnum;
	}

	public void setPacketnum(int packetnum) {
		Packetnum = packetnum;
	}

	public List<Integer> getMember() {
		return Member;
	}

	public void setMember(List<Integer> member) {
		Member = member;
	}

	public List<byte[]> getBq() {
		return bq;
	}

	public void setBq(List<byte[]> bq) {
		this.bq = bq;
	}

	public List<Integer> getBqsize() {
		return bqsize;
	}

	public void setBqsize(List<Integer> bqsize) {
		this.bqsize = bqsize;
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

	public int getMsgsize() {
		return Msgsize;
	}

	public void setMsgsize(int msgsize) {
		Msgsize = msgsize;
	}

	public int getBqnum() {
		return bqnum;
	}

	public void setBqnum(int bqnum) {
		this.bqnum = bqnum;
	}

	public byte getState() {
		return State;
	}

	public void setState(byte state) {
		State = state;
	}

	public List<UserObject> getMembeList() {
		return membeList;
	}

	public void setMembeList(List<UserObject> membeList) {
		this.membeList = membeList;
	}

}
