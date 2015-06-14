package comm.langsin.msg_file;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgSendFile extends MsgHead {// ·¢ËÍÎÄ¼þ
	private String Name;
	private String FileName;
	private byte[] File;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public byte[] getFile() {
		return File;
	}

	public void setFile(byte[] file) {
		File = file;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

}
