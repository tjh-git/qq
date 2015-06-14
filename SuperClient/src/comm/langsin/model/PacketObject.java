package comm.langsin.model;

public class PacketObject {
	private int Packetnum;
	private String Packetname;
	private byte[] Packethead;
	private String Packetsign;
	public PacketObject(int PACKETNUM, String PACKETNAME, byte[] PACKETHEAD,String SIGN) {
		this.Packetnum = PACKETNUM;
		this.Packetname = PACKETNAME;
		this.Packethead = PACKETHEAD;
		this.Packetsign = SIGN;
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

	public String getPacketsign() {
		return Packetsign;
	}

	public void setPacketsign(String packetsign) {
		Packetsign = packetsign;
	}

}
