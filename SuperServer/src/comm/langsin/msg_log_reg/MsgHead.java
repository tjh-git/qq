package comm.langsin.msg_log_reg;

public class MsgHead {// ��Ϣͷ������Ϣ����ĸ���
	private int TotalLength;// ��Ϣ�ܳ���
	private byte Type;// ��Ϣ����
	private int SrcNum;// Դ��Ϣ��ַ
	private int DestNum;// Ŀ����Ϣ��ַ

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
