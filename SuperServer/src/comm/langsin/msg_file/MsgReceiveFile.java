package comm.langsin.msg_file;

import java.util.Arrays;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgReceiveFile extends MsgHead {// 接受群文件
	private int Packetnum;
	private String File;
	private byte[] Filedata;

	@Override
	public String toString() {
		return super.toString() + "MsgReceiveFile [Packetnum=" + Packetnum
				+ ", File=" + File + ", Filedata=" + Arrays.toString(Filedata)
				+ "]";
	}

	public int getPacketnum() {
		return Packetnum;
	}

	public void setPacketnum(int packetnum) {
		Packetnum = packetnum;
	}

	public String getFile() {
		return File;
	}

	public void setFile(String file) {
		File = file;
	}

	public byte[] getFiledata() {
		return Filedata;
	}

	public void setFiledata(byte[] filedata) {
		Filedata = filedata;
	}

}
