package comm.langsin.msg_log_reg;

public class MsgLog extends MsgHead {//��¼������Ϣ
	private String Passwd;

	@Override
	public String toString() {
		return super.toString() + "MsgLog [Passwd=" + Passwd + "]";
	}

	public String getPasswd() {
		return Passwd;
	}

	public void setPasswd(String passwd) {
		Passwd = passwd;
	}
}
