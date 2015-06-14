package comm.langsin.msg_file;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgGroupFile extends MsgHead {
	private String Filename;
	private String FilePath;
	private int Groupnum;

	@Override
	public String toString() {
		return super.toString() + "MsgGroupFile [Filename=" + Filename
				+ ", FilePath=" + FilePath + ", Groupnum=" + Groupnum + "]";
	}

	public String getFilename() {
		return Filename;
	}

	public void setFilename(String filename) {
		Filename = filename;
	}

	public String getFilePath() {
		return FilePath;
	}

	public void setFilePath(String filePath) {
		FilePath = filePath;
	}

	public int getGroupnum() {
		return Groupnum;
	}

	public void setGroupnum(int groupnum) {
		Groupnum = groupnum;
	}

}
