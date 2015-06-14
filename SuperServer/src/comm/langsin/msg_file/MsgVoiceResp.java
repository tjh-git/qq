package comm.langsin.msg_file;

import java.util.Arrays;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgVoiceResp extends MsgHead {
	private String Name;
	private byte[] Voice;

	@Override
	public String toString() {
		return super.toString() + "MsgVoiceResp [Voice="
				+ Arrays.toString(Voice) + "]";
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public byte[] getVoice() {
		return Voice;
	}

	public void setVoice(byte[] voice) {
		Voice = voice;
	}
}
