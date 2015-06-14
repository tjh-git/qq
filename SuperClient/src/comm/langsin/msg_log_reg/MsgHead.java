package comm.langsin.msg_log_reg;

public class MsgHead {// 消息头所有消息对象的父类
	private int TotalLength;// 消息总长度
	private byte Type;// 消息类型
	private int SrcNum;// 源消息地址
	private int DestNum;// 目标消息地址

	@Override
	public String toString() {
		return "MsgHead [TotalLength=" + TotalLength + ", Type=" + Type
				+ ", SrcNum=" + SrcNum + ", DestNum=" + DestNum + "]";
	}

	public int getTotalLength() {
		return TotalLength;
	}

	public void setTotalLength(int totalLength) {
		TotalLength = totalLength;
	}

	public byte getType() {
		return Type;
	}

	public void setType(byte type) {
		Type = type;
	}

	public int getSrcNum() {
		return SrcNum;
	}

	public void setSrcNum(int srcNum) {
		SrcNum = srcNum;
	}

	public int getDestNum() {
		return DestNum;
	}

	public void setDestNum(int destNum) {
		DestNum = destNum;
	}
}
