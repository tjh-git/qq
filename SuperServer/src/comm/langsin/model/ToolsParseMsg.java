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

//�Կͻ��˷�������Ϣ������
public class ToolsParseMsg {
	private static ByteArrayInputStream Bytein;
	private static DataInputStream Datain;

	public static MsgHead parseMsg(byte[] data) throws Exception {
		Bytein = new ByteArrayInputStream(data);// ���ֽ�����ת��Ϊ������
		Datain = new DataInputStream(Bytein);// ���ֽ�����������ת��Ϊ���������󣬣��ô�����˵��
		MsgHead msg = AssigmMsgHead(data);
		byte MsgType = msg.getType();
		if (MsgType == Protocol.common_log) {// ��½������Ϣ
			MsgLog Ml = new MsgLog();
			CopyHead(msg, Ml);
			String Pwd = ReadString(Datain, 10);// ��ȡ10���ֽ���Ϊ����
			Ml.setPasswd(Pwd);
			return Ml;
		} else if (MsgType == Protocol.common_reg) {// ע��������Ϣ
			MsgReg Mr = new MsgReg();
			CopyHead(msg, Mr);
			String Username = ReadString(Datain, 10);
			String Userpwd = ReadString(Datain, 10);
			String UserSign = ReadString(Datain, 100);
			Mr.setUsername(Username);
			Mr.setUserpwd(Userpwd);
			Mr.setUserSign(UserSign);
			return Mr;
		} else if (MsgType == Protocol.common_finduser) {// ���Һ���
			MsgFindUser Mfu = new MsgFindUser();
			CopyHead(msg, Mfu);
			int Friendnum = Datain.readInt();
			Mfu.setFriendnum(Friendnum);
			return Mfu;
		} else if (MsgType == Protocol.common_findpacket) {// ����Ⱥ��
			MsgFindPacket Mfp = new MsgFindPacket();
			CopyHead(msg, Mfp);
			int Packetnum = Datain.readInt();
			Mfp.setPacketnum(Packetnum);
			return Mfp;
		} else if (MsgType == Protocol.common_createpacket) {// ����Ⱥ��������Ϣ
			MsgCreatePacket Mcp = new MsgCreatePacket();
			CopyHead(msg, Mcp);
			String Packetname = ReadString(Datain, 10);
			String Packetsign = ReadString(Datain, 100);
			Mcp.setPacketname(Packetname);
			Mcp.setPacketsign(Packetsign);
			return Mcp;
		} else if (MsgType == Protocol.common_addfriend) {// ��Ӻ���������Ϣ
			MsgAddFriend Maf = new MsgAddFriend();
			CopyHead(msg, Maf);
			String Mainname = ReadString(Datain, 10);
			String Mainmsg = ReadString(Datain, 100);
			Maf.setMyName(Mainname);
			Maf.setMymag(Mainmsg);
			return Maf;
		} else if (MsgType == Protocol.common_addfriend_resp) {// ��Ӻ���Ӧ����Ϣ
			MsgAddFriendResp Mafr = new MsgAddFriendResp();
			CopyHead(msg, Mafr);
			byte State = Datain.readByte();
			String Friendname = ReadString(Datain, 10);
			Mafr.setFriendname(Friendname);
			Mafr.setState(State);
			return Mafr;
		} else if (MsgType == Protocol.common_chatmsg) {// ������Ϣ
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
		} else if (msg.getType() == Protocol.common_nostatic) {// ��������
			MsgShake Msh = new MsgShake();
			CopyHead(msg, Msh);
			Msh.setName(ReadString(Datain, 10));
			return Msh;
		} else if (msg.getType() == Protocol.common_chatfile) {// �����ļ�
			MsgSendFile Msf = new MsgSendFile();
			CopyHead(msg, Msf);
			Msf.setName(ReadString(Datain, 10));
			Msf.setFileName(ReadString(Datain, 50));
			byte[] filedata = new byte[Msf.getTotalLength() - 4 - 1 - 4 - 4
					- 10 - 50];
			Datain.readFully(filedata);
			Msf.setFile(filedata);
			return Msf;
		} else if (msg.getType() == Protocol.common_addgroup) {// ���Ⱥ��������Ϣ
			MsgAddGroup Mag = new MsgAddGroup();
			CopyHead(msg, Mag);
			Mag.setPacketnum(Datain.readInt());
			Mag.setName(ReadString(Datain, 10));
			Mag.setPascketname(ReadString(Datain, 10));
			Mag.setContent(ReadString(Datain, 100));
			return Mag;
		} else if (msg.getType() == Protocol.common_addgroupresp) {// ���Ⱥ��Ӧ����Ϣ
			MsgAddGroupResp Magr = new MsgAddGroupResp();
			CopyHead(msg, Magr);
			Magr.setPacketnum(Datain.readInt());
			Magr.setName(ReadString(Datain, 10));
			Magr.setPacketname(ReadString(Datain, 10));
			Magr.setState(Datain.readByte());
			return Magr;
		} else if (msg.getType() == Protocol.common_getmember) {// ���Ⱥ��Ա
			MsgGetMember Mgm = new MsgGetMember();
			CopyHead(msg, Mgm);
			Mgm.setPacketnum(Datain.readInt());
			return Mgm;
		} else if (msg.getType() == Protocol.common_chatgroupmsg) {// Ⱥ����Ϣ
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
		} else if (msg.getType() == Protocol.common_remove) {// ɾ������
			MsgRemove Mrv = new MsgRemove();
			CopyHead(msg, Mrv);
			String NAME = ReadString(Datain, 10);
			Mrv.setName(NAME);
			return Mrv;
		} else if (msg.getType() == Protocol.commpn_remove_group) {// �Ƴ�Ⱥ��
			MsgRemoveGroup Mrg = new MsgRemoveGroup();
			CopyHead(msg, Mrg);
			int Groupnum = Datain.readInt();
			String Name = ReadString(Datain, 10);
			String Groupname = ReadString(Datain, 10);
			Mrg.setGroupNum(Groupnum);
			Mrg.setName(Name);
			Mrg.setGroupName(Groupname);
			return Mrg;
		} else if (msg.getType() == Protocol.common_sendgroupfile) {// ����Ⱥ�ļ�
			MsgGroupFile Mgf = new MsgGroupFile();
			CopyHead(msg, Mgf);
			int Groupnum = Datain.readInt();
			String Name = ReadString(Datain, 100);
			String Filepath = ReadString(Datain, 100);
			Mgf.setGroupnum(Groupnum);
			Mgf.setFilename(Name);
			Mgf.setFilePath(Filepath);
			return Mgf;
		} else if (msg.getType() == Protocol.common_receivefile) {// ����Ⱥ�ļ�
			MsgReceiveFile Mrf = new MsgReceiveFile();
			CopyHead(msg, Mrf);
			int Packetnum = Datain.readInt();
			String Filename = ReadString(Datain, 100);
			Mrf.setPacketnum(Packetnum);
			Mrf.setFile(Filename);
			return Mrf;
		} else if (msg.getType() == Protocol.common_removemeber) {// ɾ��Ⱥ��Ա
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
		} else if (msg.getType() == Protocol.common_addmember) {// ���Ⱥ��Ա������Ϣ
			MsgAddMember Mam = new MsgAddMember();
			CopyHead(msg, Mam);
			int Packetnum = Datain.readInt();
			String Name = ReadString(Datain, 10);
			String Packetname = ReadString(Datain, 10);
			Mam.setPacketnum(Packetnum);
			Mam.setName(Name);
			Mam.setPacketname(Packetname);
			return Mam;
		} else if (msg.getType() == Protocol.common_addmemberresp) {// ���Ⱥ��ԱӦ����Ϣ
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

	public static MsgHead AssigmMsgHead(byte[] Data) throws Exception {// ��ȡ��Ϣͷ��Ϣ��������Ϣͷ����ֵ
		int Totallength = Data.length + 4;// ��Ϣ�ܳ���
		byte Type = Datain.readByte();// ��ȡ��Ϣ����
		int Srcnum = Datain.readInt();// ��ȡ��ϢԴ��ַ
		int Destnum = Datain.readInt();// ��ȡĿ����Ϣ��ַ
		MsgHead msg = new MsgHead();
		msg.setTotalLength(Totallength);
		msg.setType(Type);
		msg.setSrcNum(Srcnum);
		msg.setDestNum(Destnum);
		return msg;
	}

	private static void CopyHead(MsgHead head, MsgHead dest) {// ������Ϣͷ����
		dest.setTotalLength(head.getTotalLength());
		dest.setType(head.getType());
		dest.setSrcNum(head.getSrcNum());
		dest.setDestNum(head.getDestNum());
	}

	private static String ReadString(DataInputStream dins, int len)
			throws Exception {// �����϶�ȡ��Ϣ���ֽڣ�
		byte[] data = new byte[len];
		dins.readFully(data);
		// ʹ��ϵͳĬ�ϵ��ַ�������
		return new String(data);
	}
}
