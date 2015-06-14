package comm.langsin.createadd;

import comm.langsin.msg_log_reg.MsgHead;

public class MsgAddFriend extends MsgHead {
	private String MyName;
	private String Mymag;
	private String Sign;
@Override
	public String toString() {
		return super.toString()+"MsgAddFriend [MyName=" + MyName + ", Mymag=" + Mymag
				+ ", Sign=" + Sign + "]";
	}

	public String getMyName() {
		return MyName;
	}

	public void setMyName(String myName) {
		MyName = myName;
	}

	public String getMymag() {
		return Mymag;
	}

	public void setMymag(String mymag) {
		Mymag = mymag;
	}

	public String getSign() {
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

}
