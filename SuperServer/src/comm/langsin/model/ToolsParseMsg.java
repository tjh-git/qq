package comm.langsin.model;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

import com.langsin.remove.MsgAddMember;
import com.langsin.remove.MsgAddMemberResp;
import com.langsin.remove.MsgFindMember;
import com.langsin.remove.MsgRemove;
import com.langsin.remove.MsgRemoveGroup;
import com.langsin.remove.MsgRemoveMember;
import comm.langsin.createadd.MsgAddFriend;
import comm.langsin.createadd.MsgAddFriendResp;
import comm.langsin.createadd.MsgAddGroup;
import comm.langsin.createadd.MsgAddGroupResp;
import comm.langsin.createadd.MsgCreatePacket;
import comm.langsin.find.MsgFindPacket;
import comm.langsin.find.MsgFindUser;
import comm.langsin.msg_file.MsgGetMember;
import comm.langsin.msg_file.MsgGroupFile;
import comm.langsin.msg_file.MsgGroupMsg;
import comm.langsin.msg_file.MsgReceiveFile;
import comm.langsin.msg_file.MsgSendChat;
import comm.langsin.msg_file.MsgSendFile;
import comm.langsin.msg_file.MsgShake;
import comm.langsin.msg_log_reg.MsgHead;
import comm.langsin.msg_log_reg.MsgLog;
import comm.langsin.msg_log_reg.MsgReg;
import comm.langsin.protocol.Protocol;

//对客户端发来的消息对象解包
public class ToolsParseMsg {
	private static ByteArrayInputStream Bytein;
	private static DataInputStream Datain;

	public static MsgHead parseMsg(byte[] data) throws Exception {
		Bytein = new ByteArrayInputStream(data);// 将字节数组转换为输入流
		Datain = new DataInputStream(Bytein);// 将字节数组流对象转换为数据流对象，（好处不用说）
		MsgHead msg = AssigmMsgHead(data);
		byte MsgType = msg.getType();
		if (MsgType == Protocol.common_log) {// 登陆请求消息
			MsgLog Ml = new MsgLog();
			CopyHead(msg, Ml);
			String Pwd = ReadString(Datain, 10);// 读取10个字节作为密码
			Ml.setPasswd(Pwd);
			return Ml;
		} else if (MsgType == Protocol.common_reg) {// 注册请求消息
			MsgReg Mr = new MsgReg();
			CopyHead(msg, Mr);
			String Username = ReadString(Datain, 10);
			String Userpwd = ReadString(Datain, 10);
			String UserSign = ReadString(Datain, 100);
			Mr.setUsername(Username);
			Mr.setUserpwd(Userpwd);
			Mr.setUserSign(UserSign);
			return Mr;
		} else if (MsgType == Protocol.common_finduser) {// 查找好友
			MsgFindUser Mfu = new MsgFindUser();
			CopyHead(msg, Mfu);
			int Friendnum = Datain.readInt();
			Mfu.setFriendnum(Friendnum);
			return Mfu;
		} else if (MsgType == Protocol.common_findpacket) {// 查找群组
			MsgFindPacket Mfp = new MsgFindPacket();
			CopyHead(msg, Mfp);
			int Packetnum = Datain.readInt();
			Mfp.setPacketnum(Packetnum);
			return Mfp;
		} else if (MsgType == Protocol.common_createpacket) {// 创建群组请求消息
			MsgCreatePacket Mcp = new MsgCreatePacket();
			CopyHead(msg, Mcp);
			String Packetname = ReadString(Datain, 10);
			String Packetsign = ReadString(Datain, 100);
			Mcp.setPacketname(Packetname);
			Mcp.setPacketsign(Packetsign);
			return Mcp;
		} else if (MsgType == Protocol.common_addfriend) {// 添加好友请求消息
			MsgAddFriend Maf = new MsgAddFriend();
			CopyHead(msg, Maf);
			String Mainname = ReadString(Datain, 10);
			String Mainmsg = ReadString(Datain, 100);
			Maf.setMyName(Mainname);
			Maf.setMymag(Mainmsg);
			return Maf;
		} else if (MsgType == Protocol.common_addfriend_resp) {// 添加好友应答消息
			MsgAddFriendResp Mafr = new MsgAddFriendResp();
			CopyHead(msg, Mafr);
			byte State = Datain.readByte();
			String Friendname = ReadString(Datain, 10);
			Mafr.setFriendname(Friendname);
			Mafr.setState(State);
			return Mafr;
		} else if (MsgType == Protocol.common_chatmsg) {// 单聊消息
			MsgSendChat Msc = new MsgSendChat();
			CopyHead(msg, Msc);
			List<byte[]> list = new ArrayList<byte[]>();
			List<Integer> listsize = new ArrayList<Integer>();
			String Name = ReadString(Datain, 10);
			int MsgSize = Datain.readInt();
			String Msg = ReadString(Datain, MsgSize);
			int bqnum = Datain.readInt();
			Msc.setBqnum(bqnum);
			while (bqnum > 0) {
				bqnum--;
				int bqsize = Datain.readInt();
				listsize.add(bqsize);
				byte[] bq = new byte[bqsize];
				Datain.readFully(bq);;
				list.add(bq);
			}
			Msc.setName(Name);
			Msc.setMsg(Msg);
			Msc.setMsgsize(MsgSize);
			Msc.setBqsize(listsize);
			Msc.setBq(list);
			return Msc;
		} else if (msg.getType() == Protocol.common_nostatic) {// 抖动窗体
			MsgShake Msh = new MsgShake();
			CopyHead(msg, Msh);
			Msh.setName(ReadString(Datain, 10));
			return Msh;
		} else if (msg.getType() == Protocol.common_chatfile) {// 发送文件
			MsgSendFile Msf = new MsgSendFile();
			CopyHead(msg, Msf);
			Msf.setName(ReadString(Datain, 10));
			Msf.setFileName(ReadString(Datain, 50));
			byte[] filedata = new byte[Msf.getTotalLength() - 4 - 1 - 4 - 4
					- 10 - 50];
			Datain.readFully(filedata);
			Msf.setFile(filedata);
			return Msf;
		} else if (msg.getType() == Protocol.common_addgroup) {// 添加群组请求消息
			MsgAddGroup Mag = new MsgAddGroup();
			CopyHead(msg, Mag);
			Mag.setPacketnum(Datain.readInt());
			Mag.setName(ReadString(Datain, 10));
			Mag.setPascketname(ReadString(Datain, 10));
			Mag.setContent(ReadString(Datain, 100));
			return Mag;
		} else if (msg.getType() == Protocol.common_addgroupresp) {// 添加群组应答消息
			MsgAddGroupResp Magr = new MsgAddGroupResp();
			CopyHead(msg, Magr);
			Magr.setPacketnum(Datain.readInt());
			Magr.setName(ReadString(Datain, 10));
			Magr.setPacketname(ReadString(Datain, 10));
			Magr.setState(Datain.readByte());
			return Magr;
		} else if (msg.getType() == Protocol.common_getmember) {// 添加群成员
			MsgGetMember Mgm = new MsgGetMember();
			CopyHead(msg, Mgm);
			Mgm.setPacketnum(Datain.readInt());
			return Mgm;
		} else if (msg.getType() == Protocol.common_chatgroupmsg) {// 群聊消息
			MsgGroupMsg Mgm = new MsgGroupMsg();
			CopyHead(msg, Mgm);
			List<byte[]> list = new ArrayList<byte[]>();
			List<Integer> listsize = new ArrayList<Integer>();
			int Packetnum = Datain.readInt();
			String Packetname = ReadString(Datain, 10);
			String Name = ReadString(Datain, 10);
			int MsgSize = Datain.readInt();
			String Msg = ReadString(Datain, MsgSize);
			int bqnum = Datain.readInt();
			Mgm.setBqnum(bqnum);
			while (bqnum > 0) {
				bqnum--;
				int bqsize = Datain.readInt();
				listsize.add(bqsize);
				byte[] bq = new byte[bqsize];
				Datain.readFully(bq);
				list.add(bq);
			}
			Mgm.setPacketnum(Packetnum);
			Mgm.setName(Name);
			Mgm.setPacketname(Packetname);
			Mgm.setMsg(Msg);
			Mgm.setMsgsize(MsgSize);
			Mgm.setBqsize(listsize);
			Mgm.setBq(list);
			return Mgm;
		} else if (msg.getType() == Protocol.common_remove) {// 删除好友
			MsgRemove Mrv = new MsgRemove();
			CopyHead(msg, Mrv);
			String NAME = ReadString(Datain, 10);
			Mrv.setName(NAME);
			return Mrv;
		} else if (msg.getType() == Protocol.commpn_remove_group) {// 推出群组
			MsgRemoveGroup Mrg = new MsgRemoveGroup();
			CopyHead(msg, Mrg);
			int Groupnum = Datain.readInt();
			String Name = ReadString(Datain, 10);
			String Groupname = ReadString(Datain, 10);
			Mrg.setGroupNum(Groupnum);
			Mrg.setName(Name);
			Mrg.setGroupName(Groupname);
			return Mrg;
		} else if (msg.getType() == Protocol.common_sendgroupfile) {// 发送群文件
			MsgGroupFile Mgf = new MsgGroupFile();
			CopyHead(msg, Mgf);
			int Groupnum = Datain.readInt();
			String Name = ReadString(Datain, 100);
			String Filepath = ReadString(Datain, 100);
			Mgf.setGroupnum(Groupnum);
			Mgf.setFilename(Name);
			Mgf.setFilePath(Filepath);
			return Mgf;
		} else if (msg.getType() == Protocol.common_receivefile) {// 接受群文件
			MsgReceiveFile Mrf = new MsgReceiveFile();
			CopyHead(msg, Mrf);
			int Packetnum = Datain.readInt();
			String Filename = ReadString(Datain, 100);
			Mrf.setPacketnum(Packetnum);
			Mrf.setFile(Filename);
			return Mrf;
		} else if (msg.getType() == Protocol.common_removemeber) {// 删除群成员
			MsgRemoveMember Mrm = new MsgRemoveMember();
			CopyHead(msg, Mrm);
			int Packetnum = Datain.readInt();
			String NAME = ReadString(Datain, 10);
			String Packetname = ReadString(Datain, 10);
			Mrm.setPacketnum(Packetnum);
			Mrm.setMainname(NAME);
			Mrm.setPascketname(Packetname);
			return Mrm;
		} else if (msg.getType() == Protocol.common_findmember) {
			MsgFindMember Mfm = new MsgFindMember();
			CopyHead(msg, Mfm);
			return Mfm;
		} else if (msg.getType() == Protocol.common_addmember) {// 添加群成员请求消息
			MsgAddMember Mam = new MsgAddMember();
			CopyHead(msg, Mam);
			int Packetnum = Datain.readInt();
			String Name = ReadString(Datain, 10);
			String Packetname = ReadString(Datain, 10);
			Mam.setPacketnum(Packetnum);
			Mam.setName(Name);
			Mam.setPacketname(Packetname);
			return Mam;
		} else if (msg.getType() == Protocol.common_addmemberresp) {// 添加群成员应答消息
			MsgAddMemberResp Mamr = new MsgAddMemberResp();
			CopyHead(msg, Mamr);
			int Packetnum = Datain.readInt();
			byte State = Datain.readByte();
			String Name = ReadString(Datain, 10);
			String Packetname = ReadString(Datain, 10);
			Mamr.setPacketnum(Packetnum);
			Mamr.setState(State);
			Mamr.setName(Name);
			Mamr.setPacketname(Packetname);
			return Mamr;
		}
		return null;
	}

	public static MsgHead AssigmMsgHead(byte[] Data) throws Exception {// 读取消息头消息，并给消息头对象赋值
		int Totallength = Data.length + 4;// 消息总长度
		byte Type = Datain.readByte();// 读取消息类型
		int Srcnum = Datain.readInt();// 读取消息源地址
		int Destnum = Datain.readInt();// 读取目标消息地址
		MsgHead msg = new MsgHead();
		msg.setTotalLength(Totallength);
		msg.setType(Type);
		msg.setSrcNum(Srcnum);
		msg.setDestNum(Destnum);
		return msg;
	}

	private static void CopyHead(MsgHead head, MsgHead dest) {// 复制消息头对象
		dest.setTotalLength(head.getTotalLength());
		dest.setType(head.getType());
		dest.setSrcNum(head.getSrcNum());
		dest.setDestNum(head.getDestNum());
	}

	private static String ReadString(DataInputStream dins, int len)
			throws Exception {// 从流上读取消息（字节）
		byte[] data = new byte[len];
		dins.readFully(data);
		// 使用系统默认的字符集编码
		return new String(data);
	}
}
