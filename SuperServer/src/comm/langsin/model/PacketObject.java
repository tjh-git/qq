package comm.langsin.model;

public class PacketObject {
	private int Mainnum;
	private int Packetnum;
	private String Packetname;
	private String Sign;
	private byte[] Packethead;

	public PacketObject(int MAINNUM, int PACKETNUM, String PACKETNAME,String SIGN,
			byte[] PACKETHEAD) {
		this.Sign = SIGN;
		this.Mainnum = MAINNUM;
		this.Packetnum = PACKETNUM;
		this.Packetname = PACKETNAME;
		this.Packethead = PACKETHEAD;
	}

	public String getSign() {
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

	public int getMainnum() {
		return Mainnum;
	}

	public void setMainnum(int mainnum) {
		Mainnum = mainnum;
	}

	public int getPacketnum() {
		return Packetnum;
	}

	public void setPacketnum(int packetnum) {
		Packetnum = packetnum;
	}

	public String getPacketname() {
		return Packetname;
	}

	public void setPacketname(String packetname) {
		Packetname = packetname;
	}

	public byte[] getPackethead() {
		return Packethead;
	}

	public void setPackethead(byte[] packethead) {
		Packethead = packethead;
	}

}
