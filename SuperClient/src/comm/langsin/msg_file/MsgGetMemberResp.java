package comm.langsin.msg_file;

import java.util.ArrayList;
import java.util.List;

import comm.langsin.model.UserObject;
import comm.langsin.msg_log_reg.MsgHead;

public class MsgGetMemberResp extends MsgHead {//
	private byte State;
	private int Membernum;
	private int Filenum;
	private List<UserObject> list = new ArrayList<UserObject>();
	private List<Object> File = new ArrayList<Object>();

	@Override
	public String toString() {
		return super.toString() + "MsgGetMemberResp [State=" + State
				+ ", Membernum=" + Membernum + ", Filenum=" + Filenum
				+ ", list=" + list + ", File=" + File + "]";
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

	public int getMembernum() {
		return Membernum;
	}

	public void setMembernum(int membernum) {
		Membernum = membernum;
	}

	public byte getState() {
		return State;
	}

	public void setState(byte state) {
		State = state;
	}

	public List<UserObject> getList() {
		return list;
	}

	public void setList(List<UserObject> list) {
		this.list = list;
	}

}
