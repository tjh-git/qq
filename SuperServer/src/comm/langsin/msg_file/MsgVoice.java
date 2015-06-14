package comm.langsin.msg_file;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgVoice extends MsgHead {
	private String Name;

	@Override
	public String toString() {
		return super.toString() + "MsgVoiceResp [Name=" + Name + "]";
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

}
