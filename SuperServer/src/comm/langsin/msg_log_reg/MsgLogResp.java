package comm.langsin.msg_log_reg;

import java.util.Arrays;

public class MsgLogResp extends MsgHead {// µÇÂ¼Ó¦´ğÏûÏ¢
	private String Username;
	private String Usersign;
	private byte[] Userhead;
	private byte State;
	@Override
	public String toString() {
		return super.toString() + "MsgLogResp [Username=" + Username
				+ ", Usersign=" + Usersign + ", Userhead="
				+ Arrays.toString(Userhead) + "]";
	}

	public byte getState() {
		return State;
	}

	public void setState(byte state) {
		State = state;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getUsersign() {
		return Usersign;
	}

	public void setUsersign(String usersign) {
		Usersign = usersign;
	}

	public byte[] getUserhead() {
		return Userhead;
	}

	public void setUserhead(byte[] userhead) {
		Userhead = userhead;
	}
}
