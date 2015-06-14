package comm.langsin.jdbcuntil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import comm.langsin.model.PacketObject;
import comm.langsin.model.UserObject;

public class JdbcUntil {
	private static String Username = "tt";
	private static String Passwd = "123";
	private static String Url = "jdbc:oracle:thin:@localhost:1521:ORCL";// 这个背过
	private static String Driver = "oracle.jdbc.driver.OracleDriver";// 这个背过
	private static Connection Conn = null;
	private FileInputStream fis;
	private FileInputStream fis2;
	private FileInputStream fis3;
	private FileInputStream fis4;
	private FileInputStream fis5;

	public static Connection getconnection() {
		try {
			Class.forName(Driver);
			Conn = DriverManager.getConnection(Url, Username, Passwd);// 试图与数据库建立连接
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Conn;
	}

	public int GetMaxNum() {// 获得最大的账号
		int Maxnum = 0;
		Conn = JdbcUntil.getconnection();
		String sql = "select max(USERNUM) from USERMSG";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Maxnum = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Maxnum++;
		return Maxnum;
	}

	public int GetPacketNum() {// 获得最大的群号码
		int PacketNum = 0;
		Conn = JdbcUntil.getconnection();
		String sql = "select max(GROUPNUM ) from USERGROUP ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				PacketNum = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PacketNum++;
		return PacketNum;
	}

	public UserObject Log(int Mainnum, String Pwd) {// 登录验证
		UserObject User = null;
		Conn = JdbcUntil.getconnection();
		String sql = "select USERNUM ,USERNAME,USERSIGN ,USERHEAD from USERMSG where USERNUM = ? and USERPWD  = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Mainnum);
			ps.setString(2, Pwd);
			rs = ps.executeQuery();
			while (rs.next()) {
				try {
					fis = new FileInputStream(new File(rs.getString(4)));
					byte[] head = new byte[fis.available()];
					fis.read(head);
					User = new UserObject(rs.getInt(1), null, rs.getString(2),
							rs.getString(3), head);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return User;
	}

	public void Reg(int oonum, String nickname, String pwd, String signature) {
		Conn = JdbcUntil.getconnection();// 与数据库；建立连接
		String sql = "insert into USERMSG values(?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, oonum);
			ps.setString(2, nickname);
			ps.setString(3, pwd);
			ps.setString(4, signature);
			String head = "E:\\Head\\head.png";
			ps.setString(5, head);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UserObject FindUser(int Friendnum) {// 查找好友
		UserObject User = null;
		Conn = JdbcUntil.getconnection();
		String sql = "select USERNUM , USERNAME ,USERHEAD from USERMSG where USERNUM = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Friendnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				fis3 = new FileInputStream(new File(rs.getString(3)));
				byte[] head = new byte[fis3.available()];
				fis3.read(head);
				User = new UserObject(rs.getInt(1), null, rs.getString(2),
						null, head);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return User;
	}

	public PacketObject FindGroup(int Packetnum) {
		PacketObject Packet = null;
		Conn = JdbcUntil.getconnection();
		String sql = "select MAINNUM,GROUPNUM ,GROUPNAME , GROUPHEAD from USERGROUP  where GROUPNUM = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Packetnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				fis4 = new FileInputStream(new File(rs.getString(4)));
				byte[] packethead = new byte[fis4.available()];
				fis4.read(packethead);
				Packet = new PacketObject(rs.getInt(1), rs.getInt(2),
						rs.getString(3), null, packethead);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Packet;
	}

	public void CreatePacket(int Mainnum, int Packetnum, String Packetname,
			String Packetsign) {// 创建群组
		Conn = JdbcUntil.getconnection();
		String path = "E:\\Head\\group.png";
		String sql = "INSERT INTO USERGROUP VALUES(?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Mainnum);
			ps.setInt(2, Packetnum);
			ps.setString(3, Packetname);
			ps.setString(4, Packetsign);
			ps.setString(5, path);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void SaveMember(int Membernum, int Packetnum) {
		Conn = JdbcUntil.getconnection();
		String sql = "INSERT INTO GROUPMEMBERS values(?,?)";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Membernum);
			ps.setInt(2, Packetnum);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void SaveFriend(int MAINNUM, int FRIENDNUM) {// 存储好友列表
		Conn = JdbcUntil.getconnection();
		String sql = "INSERT INTO  USERFRIENDS VALUES(?,?)";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, MAINNUM);
			ps.setInt(2, FRIENDNUM);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<UserObject> FriendList(int Mainnum) {
		List<UserObject> FriendList = new ArrayList<UserObject>();
		Conn = JdbcUntil.getconnection();
		String sql1 = "SELECT FRIENDNUM FROM USERFRIENDS WHERE MAINNUM = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql1);
			ps.setInt(1, Mainnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				String sql2 = "SELECT USERNUM,USERNAME ,USERHEAD,USERSIGN  FROM USERMSG WHERE USERNUM = ?";
				PreparedStatement ps1 = null;
				ResultSet rs1 = null;
				ps1 = Conn.prepareStatement(sql2);
				ps1.setInt(1, rs.getInt(1));
				rs1 = ps1.executeQuery();
				while (rs1.next()) {
					fis2 = new FileInputStream(new File(rs1.getString(3)));
					byte[] head = new byte[fis2.available()];
					fis2.read(head);
					UserObject Friend = new UserObject(rs1.getInt(1), null,
							rs1.getString(2), rs1.getString(4), head);
					FriendList.add(Friend);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FriendList;
	}

	public List<PacketObject> GroupList(int Mainnum) {
		List<PacketObject> Packetlist = new ArrayList<PacketObject>();
		Conn = JdbcUntil.getconnection();
		String sql1 = "SELECT GROUPNUM FROM GROUPMEMBERS WHERE MEMBERNUM = ?";
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		try {
			ps1 = Conn.prepareStatement(sql1);
			ps1.setInt(1, Mainnum);
			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				String sql2 = "SELECT GROUPNUM ,GROUPNAME,GROUPSIGN,GROUPHEAD FROM USERGROUP WHERE GROUPNUM = ?";
				PreparedStatement ps2 = null;
				ResultSet rs2 = null;
				ps2 = Conn.prepareStatement(sql2);
				ps2.setInt(1, rs1.getInt(1));
				rs2 = ps2.executeQuery();
				while (rs2.next()) {
					FileInputStream fis;
					try {
						fis = new FileInputStream(new File(rs2.getString(4)));
						byte[] head = new byte[fis.available()];
						fis.read(head);
						PacketObject Packet = new PacketObject(0,
								rs2.getInt(1), rs2.getString(2),
								rs2.getString(3), head);
						Packetlist.add(Packet);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Packetlist;
	}

	public String GetSign(int Mainnum) {
		String Sign = null;
		Conn = JdbcUntil.getconnection();
		String sql = "SELECT USERSIGN  FROM USERMSG WHERE USERNUM = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Mainnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				Sign = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Sign;
	}

	public List<UserObject> GetMember(int Packetnum) {
		Conn = JdbcUntil.getconnection();
		List<UserObject> Member = new ArrayList<UserObject>();
		String sql1 = "SELECT MEMBERNUM  FROM GROUPMEMBERS WHERE GROUPNUM = ?";
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		try {
			ps1 = Conn.prepareStatement(sql1);
			ps1.setInt(1, Packetnum);
			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				PreparedStatement ps2 = null;
				ResultSet rs2 = null;
				String sql2 = "SELECT USERNUM ,USERNAME  FROM USERMSG WHERE USERNUM  = ?";
				ps2 = Conn.prepareStatement(sql2);
				ps2.setInt(1, rs1.getInt(1));
				rs2 = ps2.executeQuery();
				while (rs2.next()) {
					UserObject User = new UserObject(rs2.getInt(1), null,
							rs2.getString(2), null, null);
					Member.add(User);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Member;
	}

	public byte GroupState(int Mainnum, int Packetnum) {
		byte State = 1;
		String sql = "SELECT * FROM USERGROUP WHERE MAINNUM = ? AND GROUPNUM = ?";
		Conn = JdbcUntil.getconnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Mainnum);
			ps.setInt(2, Packetnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				State = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return State;
	}

	public List<Integer> GetAllMember(int Mainnum, int Packetnum) {
		List<Integer> Membernum = new ArrayList<Integer>();
		Conn = JdbcUntil.getconnection();
		String sql = "SELECT MEMBERNUM FROM GROUPMEMBERS  WHERE GROUPNUM = ? AND MEMBERNUM != ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Packetnum);
			ps.setInt(2, Mainnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				Membernum.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Membernum;
	}

	/**
	 * 离线消息
	 * **/
	public void SaveChatMsg(int Srcnum, int Destnum, String Srcname, String Msg) {// 存储单聊消息
		Conn = JdbcUntil.getconnection();// 连接数据库
		String sql = "INSERT INTO SINGLECHATMSG VALUES(?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Srcnum);
			ps.setInt(2, Destnum);
			ps.setString(3, Srcname);
			ps.setString(4, Msg);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Object> GetChatMsg(int Destnum) {// 获取用户的离线单聊消息
		List<Object> Msg = new ArrayList<Object>();
		Conn = JdbcUntil.getconnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT SRCNUM,SRCNAME,OFFLINEMSG FROM SINGLECHATMSG WHERE DESTNUM = ?";
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Destnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				Msg.add(rs.getInt(1));
				Msg.add(rs.getString(2));
				Msg.add(rs.getString(3));
			}
			PreparedStatement ps1 = null;
			String sql1 = "DELETE SINGLECHATMSG WHERE DESTNUM = ?";
			ps1 = Conn.prepareStatement(sql1);
			ps1.setInt(1, Destnum);
			ps1.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Msg;
	}

	public void SaveGroupMsg(int SRCNUM, String SRCNAME, int DESTNUM,
			int PACKETNUM, String PACKETNAME, String OFFLINEMSG) {// 存储群聊消息
		Conn = JdbcUntil.getconnection();
		String sql = " INSERT INTO GROUPCHATOFFLINEMSG VALUES(?,?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, SRCNUM);
			ps.setString(2, SRCNAME);
			ps.setInt(3, DESTNUM);
			ps.setInt(4, PACKETNUM);
			ps.setString(5, PACKETNAME);
			ps.setString(6, OFFLINEMSG);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Object> GetGroupMsg(int Srcnum) {
		List<Object> GroupMsg = new ArrayList<Object>();
		Conn = JdbcUntil.getconnection();
		String sql = "SELECT SRCNUM,SRCNAME,PACKETNUM,PACKETNAME,OFFLINEMSG FROM GROUPCHATOFFLINEMSG WHERE DESTNUM = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Srcnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				GroupMsg.add(rs.getInt(1));
				GroupMsg.add(rs.getString(2));
				GroupMsg.add(rs.getInt(3));
				GroupMsg.add(rs.getString(4));
				GroupMsg.add(rs.getString(5));
			}
			String sql1 = "DELETE GROUPCHATOFFLINEMSG WHERE DESTNUM = ?";
			PreparedStatement ps1 = null;
			ps1 = Conn.prepareStatement(sql1);
			ps1.setInt(1, Srcnum);
			ps1.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return GroupMsg;
	}

	public void RemoveFriend(int Mainnum, int Friendnum) {// 删除好友
		Conn = JdbcUntil.getconnection();
		String sql = "DELETE USERFRIENDS WHERE MAINNUM = ? AND FRIENDNUM = ?";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Mainnum);
			ps.setInt(2, Friendnum);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void RemoveGroup(int Mainnum, int Packetnum) {// 删除群组
		Conn = JdbcUntil.getconnection();
		String sql = "DELETE GROUPMEMBERS GROUPMEMBERS WHERE MEMBERNUM =? AND GROUPNUM = ?";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Mainnum);
			ps.setInt(2, Packetnum);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Integer> GetAllFriend(int Mainnum) {
		Conn = JdbcUntil.getconnection();
		String sql = "SELECT FRIENDNUM FROM USERFRIENDS WHERE MAINNUM =? AND FRIENDNUM != ?";
		List<Integer> Friendnum = new ArrayList<Integer>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Mainnum);
			ps.setInt(2, Mainnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				Friendnum.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Friendnum;
	}

	public void AddFriendOffline(int Srcnum, int Destnum, String SRCNAME,
			String SRCSIGN, String MSG) {// 添加好友离线请求消息
		Conn = JdbcUntil.getconnection();
		String sql = "INSERT INTO ADDFRIENDOFFLINE VALUES(?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Srcnum);
			ps.setInt(2, Destnum);
			ps.setString(3, SRCNAME);
			ps.setString(4, SRCSIGN);
			ps.setString(5, MSG);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Object> GetAddFriendOffline(int Mainnum) {
		List<Object> List = new ArrayList<Object>();
		Conn = JdbcUntil.getconnection();
		String sql = "SELECT SRCNUM,SRCNAME,SRCSIGN,MSG FROM ADDFRIENDOFFLINE WHERE DESTNUM = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Mainnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				List.add(rs.getInt(1));
				List.add(rs.getString(2));
				List.add(rs.getString(3));
				List.add(rs.getString(4));
			}
			PreparedStatement ps1 = null;
			String sql1 = "DELETE ADDFRIENDOFFLINE WHERE DESTNUM = ?";
			ps1 = Conn.prepareStatement(sql1);
			ps1.setInt(1, Mainnum);
			ps1.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return List;
	}

	public void SaveAddFriendResp(int SRCNUM, int Destnum, String Name,
			String Sign, int State) {
		Conn = JdbcUntil.getconnection();
		String sql = "INSERT INTO ADDFRIENDOFFLINERESP VALUES(?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, SRCNUM);
			ps.setInt(2, Destnum);
			ps.setString(3, Name);
			ps.setString(4, Sign);
			ps.setInt(5, State);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Object> GetAddFriendResp(int Mainnum) {// 获得添加好友离线应答消息
		List<Object> List = new ArrayList<Object>();
		Conn = JdbcUntil.getconnection();
		String sql = "SELECT SRCNUM,SRCNAME,SRCSIGN,STATE FROM ADDFRIENDOFFLINERESP WHERE DESTNUM = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Mainnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				List.add(rs.getInt(1));
				List.add(rs.getString(2));
				List.add(rs.getString(3));
				List.add(rs.getInt(4));
			}
			PreparedStatement ps1 = null;
			String sql1 = "DELETE ADDFRIENDOFFLINERESP WHERE DESTNUM = ?";
			ps1 = Conn.prepareStatement(sql1);
			ps1.setInt(1, Mainnum);
			ps1.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return List;
	}

	public void AddGroup(int Srcnum, int Destnum, int Groupnum,
			String Groupname, String NAME, String Msg) {
		Conn = JdbcUntil.getconnection();
		String sql = "INSERT INTO ADDGROUPOFFLINE VALUES(?,?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Srcnum);
			ps.setInt(2, Destnum);
			ps.setInt(3, Groupnum);
			ps.setString(4, Groupname);
			ps.setString(5, NAME);
			ps.setString(6, Msg);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Object> GetAddGroup(int Mainnum) {
		List<Object> Msg = new ArrayList<Object>();
		Conn = JdbcUntil.getconnection();
		String sql = "SELECT SRCNUM,GROUPNUM,GROUPNAME,SRCNAME,MSG FROM ADDGROUPOFFLINE WHERE DESTNUM =?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Mainnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				Msg.add(rs.getInt(1));
				Msg.add(rs.getInt(2));
				Msg.add(rs.getString(3));
				Msg.add(rs.getString(4));
				Msg.add(rs.getString(5));
			}
			PreparedStatement ps1 = null;
			String sql1 = "DELETE ADDGROUPOFFLINE WHERE DESTNUM = ?";
			ps1 = Conn.prepareStatement(sql1);
			ps1.setInt(1, Mainnum);
			ps1.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Msg;
	}

	public void PacketFile(int Groupnum, String Filename, String Filepath) {
		Conn = JdbcUntil.getconnection();
		String sql = "INSERT INTO GROUPFIE VALUES(?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Groupnum);
			ps.setString(2, Filename);
			ps.setString(3, Filepath);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Object> GetGroupFile(int Groupnum) {// 文件名字
		List<Object> File = new ArrayList<Object>();
		Conn = JdbcUntil.getconnection();
		String sql = "SELECT FILENAME FROM GROUPFIE WHERE PACKETNUM = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Groupnum);
			rs = ps.executeQuery();
			while (rs.next()) {
				File.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return File;
	}

	public byte[] GetPacketFile(int Packetnum, String Filename) {
		byte[] filedata = null;
		Conn = JdbcUntil.getconnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT FILEPATH FROM GROUPFIE WHERE PACKETNUM = ? AND FILENAME = ?";
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, Packetnum);
			ps.setString(2, Filename);
			rs = ps.executeQuery();
			while (rs.next()) {
				fis5 = new FileInputStream(new File(rs.getString(1).trim()));
				filedata = new byte[fis5.available()];
				fis5.read(filedata);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filedata;
	}

	public void RemoveMember(int num, int Packetnum) {
		Conn = JdbcUntil.getconnection();
		String sql = "DELETE GROUPMEMBERS  WHERE MEMBERNUM = ? AND GROUPNUM = ?";
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement(sql);
			ps.setInt(1, num);
			ps.setInt(2, Packetnum);
			ps.executeQuery();
			Conn.commit();
			Conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<UserObject> FindaddMember() {
		List<UserObject> List = new ArrayList<UserObject>();
		Conn = JdbcUntil.getconnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT USERNUM ,USERNAME FROM USERMSG ";
		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				UserObject User = new UserObject(rs.getInt(1), null,
						rs.getString(2), null, null);
				List.add(User);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return List;
	}
	public static void main(String[] args) {
		System.out.println(new JdbcUntil().GetMaxNum());
		
	}
}
