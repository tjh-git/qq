package comm.langsin.model;

public class UserObject {//用户对象
	private int Usernum;
	private String Password;
	private String Username;
	private String Signature;
	private byte[] Userhead;
	public UserObject(int Usernum , String Passwd , String Username , String Signature , byte[] Userhead){
		this.Usernum = Usernum;
		this.Password = Passwd;
		this.Username = Username;
		this.Signature = Signature;
		this.Userhead = Userhead;
	}
	public int getUsernum() {
		return Usernum;
	}
	public void setUsernum(int usernum) {
		Usernum = usernum;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		Signature = signature;
	}
	public byte[] getUserhead() {
		return Userhead;
	}
	public void setUserhead(byte[] userhead) {
		Userhead = userhead;
	}
}
