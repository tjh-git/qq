package comm.langsin.msg_log_reg;

public class MsgReg extends MsgHead {// ×¢²áÇëÇóÏûÏ¢
	private String Username;
	private String Userpwd;
	private String UserSign;

	@Override
	public String toString() {
		return super.toString() + "MsgReg [Username=" + Username + ", Userpwd="
				+ Userpwd + ", UserSign=" + UserSign + "]";
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getUserpwd() {
		return Userpwd;
	}

	public void setUserpwd(String userpwd) {
		Userpwd = userpwd;
	}

	public String getUserSign() {
		return UserSign;
	}

	public void setUserSign(String userSign) {
		UserSign = userSign;
	}

}
