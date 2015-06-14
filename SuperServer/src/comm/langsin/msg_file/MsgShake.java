package comm.langsin.msg_file;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgShake extends MsgHead{
	private String Name;
	private String Sign;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getSign() {
		return Sign;
	}
	public void setSign(String sign) {
		Sign = sign;
	}
	
}
