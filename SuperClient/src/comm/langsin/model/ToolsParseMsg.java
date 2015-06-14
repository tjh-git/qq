package comm.langsin.model;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

import com.langsin.onoff.MsgOffline;
import com.langsin.onoff.MsgOnline;
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
import comm.langsin.createadd.MsgCreatePacketResp;
import comm.langsin.find.MsgFindPacketResp;
import comm.langsin.find.MsgFindUserResp;
import comm.langsin.msg_file.MsgGetMemberResp;
import comm.langsin.msg_file.MsgGroupMsg;
import comm.langsin.msg_file.MsgReceiveFile;
import comm.langsin.msg_file.MsgSendChat;
import comm.langsin.msg_file.MsgSendFile;
import comm.langsin.msg_file.MsgShake;
import comm.langsin.msg_file.MsgVoice;
import comm.langsin.msg_log_reg.MsgHead;
import comm.langsin.msg_log_reg.MsgLogResp;
import comm.langsin.msg_log_reg.MsgRegResp;
import comm.langsin.msgoffline.MsgFriendList;
import comm.langsin.msgoffline.MsgGroupList;
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
		if (MsgType == Protocol.common_log_resp) {// ��½Ӧ����Ϣ
			MsgLogResp Mlr = new MsgLogResp();
			CopyHead(msg, Mlr);
			int MsgLength = msg.getTotalLength();
			if (MsgLength == 13) {
				Mlr.setState(Datain.readByte());
			} else if (MsgLength == 14) {

			} else {
				String Username = ReadString(Datain, 10);
				String Usersign = ReadString(Datain, 100);
				byte[] Userhead = new byte[5318];
				Datain.readFully(Userhead);
				Mlr.setUsername(Username);
				Mlr.setUsersign(Usersign);
				Mlr.setUserhead(Userhead);
			}
			return Mlr;
		} else if (MsgType == Protocol.common_reg_resp) {// ע��Ӧ����Ϣ
			MsgRegResp Mrr = new MsgRegResp();
			CopyHead(msg, Mrr);
			byte State = Datain.readByte();
			Mrr.setState(State);
			return Mrr;
		} else if (MsgType == Protocol.common_finduser_resp) {// ���Һ���Ӧ����Ϣ
			MsgFindUserResp Mfur = new MsgFindUserResp();
			CopyHead(msg, Mfur);
			int Friendnum = Datain.readInt();
			String Friendname = ReadString(Datain, 10);
			byte[] Friendhead = new byte[5318];
			Datain.readFully(Friendhead);
			Mfur.setFriendnum(Friendnum);
			Mfur.setFriendname(Friendname);
			Mfur.setFriendhead(Friendhead);
			return Mfur;
		} else if (MsgType == Protocol.common_findpacket_resp) {// ����Ⱥ��Ӧ����Ϣ
			MsgFindPacketResp Mfpr = new MsgFindPacketResp();
			CopyHead(msg, Mfpr);
			int Mainnum = Datain.readInt();
			int Packetnum = Datain.readInt();
			String Packetname = ReadString(Datain, 10);
			byte[] Packethead = new byte[1218];
			Datain.readFully(Packethead);
			Mfpr.setMainnum(Mainnum);
			Mfpr.setPacketnum(Packetnum);
			Mfpr.setPacketname(Packetname);
			Mfpr.setPackethead(Packethead);
			return Mfpr;
		} else if (MsgType == Protocol.common_createpacket_resp) {
			MsgCreatePacketResp Mcpr = new MsgCreatePacketResp();
			CopyHead(msg, Mcpr);
			int Packetnum = Datain.readInt();
			String Packetname = ReadString(Datain, 10);
			String Packetsign = ReadString(Datain, 100);
			Mcpr.setPacketnum(Packetnum);
			Mcpr.setPacketname(Packetname);
			Mcpr.setPacketsign(Packetsign);
			return Mcpr;
		} else if (MsgType == Protocol.common_addfriend) {
			MsgAddFriend Maf = new MsgAddFriend();
			CopyHead(msg, Maf);
			String Name = ReadString(Datain, 10);
			String Msg = ReadString(Datain, 100);
			String Sign = ReadString(Datain, 100);
			Maf.setMyName(Name);
			Maf.setMymag(Msg);
			Maf.setSign(Sign);
			return Maf;
		} else if (MsgType == Protocol.common_addfriend_resp) {
			MsgAddFriendResp Mafr = new MsgAddFriendResp();
			CopyHead(msg, Mafr);
			String Name = ReadString(Datain, 10);
			String Sign = ReadString(Datain, 100);
			byte State = Datain.readByte();
			Mafr.setFriendname(Name);
			Mafr.setFriendsign(Sign);
			Mafr.setState(State);
			return Mafr;
		} else if (MsgType == Protocol.common_friendlist) {
			MsgFriendList Mfl = new MsgFriendList();
			CopyHead(msg, Mfl);
			List<UserObject> Friendlist = new ArrayList<UserObject>();
			int Friendsize = Datain.readInt();// ��ȡһ���ж��ٸ�����
			Mfl.setFriendsize(Friendsize);
			while (Friendsize > 0) {
				Friendsize--;
				int Friendnum = Datain.readInt();
				String Friendname = ReadString(Datain, 10);
				String Friendsign = ReadString(Datain, 100);
				byte[] Friendhead = new byte[5318];
				Datain.readFully(Friendhead);
				UserObject Friend = new UserObject(Friendnum, null, Friendname,
						Friendsign, Friendhead);
				Friendlist.add(Friend);
			}
			Mfl.setFriendlist(Friendlist);
			return Mfl;
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
				Datain.readFully(bq);
				list.add(bq);
			}
			Msc.setName(Name);
			Msc.setMsg(Msg);
			Msc.setMsgsize(MsgSize);
			Msc.setBqsize(listsize);
			Msc.setBq(list);
			return Msc;
		} else if (MsgType == Protocol.common_nostatic) {
			MsgShake Msh = new MsgShake();
			CopyHead(msg, Msh);
			Msh.setName(ReadString(Datain, 10));
			return Msh;
		} else if (MsgType == Protocol.common_chatfile) {
			MsgSendFile Msf = new MsgSendFile();
			CopyHead(msg, Msf);
			Msf.setName(ReadString(Datain, 10));
			Msf.setFileName(ReadString(Datain, 50));
			byte[] filedata = new byte[Msf.getTotalLength() - 4 - 1 - 4 - 4
					- 10 - 50];
			Datain.readFully(filedata);
			Msf.setFile(filedata);
			return Msf;
		} else if (MsgType == Protocol.common_addgroup) {// ���Ⱥ��
			MsgAddGroup Mag = new MsgAddGroup();
			CopyHead(msg, Mag);
			Mag.setPacketnum(Datain.readInt());
			Mag.setName(ReadString(Datain, 10));
			Mag.setPascketname(ReadString(Datain, 10));
			Mag.setContent(ReadString(Datain, 100));
			return Mag;
		} else if (MsgType == Protocol.common_grouplist) {// Ⱥ���б�
			MsgGroupList Mgl = new MsgGroupList();
			CopyHead(msg, Mgl);
			List<PacketObject> Packetlist = new ArrayList<PacketObject>();
			int size = Datain.readInt();
			while (size > 0) {
				size--;
				int Packetnum = Datain.readInt();
				String Packetname = ReadString(Datain, 10);
				String Packetsign = ReadString(Datain, 100);
				byte[] head = new byte[1218];
				Datain.readFully(head);
				PacketObject Packet = new PacketObject(Packetnum, Packetname,
						head, Packetsign);
				Packetlist.add(Packet);
			}
			Mgl.setPacketlist(Packetlist);
			return Mgl;
		} else if (msg.getType() == Protocol.common_addgroupresp) {// ���Ⱥ��Ӧ����Ϣ
			MsgAddGroupResp Magr = new MsgAddGroupResp();
			CopyHead(msg, Magr);
			Magr.setPacketnum(Datain.readInt());
			Magr.setName(ReadString(Datain, 10));
			Magr.setPacketname(ReadString(Datain, 10));
			Magr.setState(Datain.readByte());
			return Magr;
		} else if (msg.getType() == Protocol.common_getmember_resp) {// Ⱥ��ԱӦ����Ϣ
			MsgGetMemberResp Mgmr = new MsgGetMemberResp();
			CopyHead(msg, Mgmr);
			List<UserObject> Memberlist = new ArrayList<UserObject>();
			List<Object> File = new ArrayList<Object>();
			int Membernum = Datain.readInt();
			byte State = Datain.readByte();
			while (Membernum > 0) {
				Membernum--;
				int NUM = Datain.readInt();
				String NAME = ReadString(Datain, 10);
				UserObject User = new UserObject(NUM, null, NAME, null, null);
				Memberlist.add(User);
			}
			int Filesize = Datain.readInt();
			while (Filesize > 0) {
				Filesize--;
				String Filename = ReadString(Datain, 100);
				File.add(Filename);
			}
			Mgmr.setMembernum(Membernum);
			Mgmr.setState(State);
			Mgmr.setList(Memberlist);
			Mgmr.setFilenum(Filesize);
			Mgmr.setFile(File);
			return Mgmr;
		} else if (MsgType == Protocol.common_chatgroupmsg) {// Ⱥ��
			MsgGroupMsg Mgm = new MsgGroupMsg();
			CopyHead(msg, Mgm);
			List<byte[]> list = new ArrayList<byte[]>();
			List<Integer> listsize = new ArrayList<Integer>();
			byte State = Datain.readByte();
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
			List<UserObject> Member = new ArrayList<UserObject>();
			int Membernum = Datain.readInt();
			while (Membernum > 0) {
				Membernum--;
				UserObject user = new UserObject(Datain.readInt(), null,
						ReadString(Datain, 10), null, null);
				Member.add(user);
			}
			int Filenum = Datain.readInt();
			List<Object> File = new ArrayList<Object>();
			while (Filenum > 0) {
				Filenum--;
				String Filename = ReadString(Datain, 100);
				File.add(Filename);
			}
			Mgm.setName(Name);
			Mgm.setPacketname(Packetname);
			Mgm.setMsg(Msg);
			Mgm.setMsgsize(MsgSize);
			Mgm.setBqsize(listsize);
			Mgm.setBq(list);
			Mgm.setState(State);
			Mgm.setPacketnum(Packetnum);
			Mgm.setMemberlist(Member);
			Mgm.setFilenum(Filenum);
			Mgm.setFile(File);
			return Mgm;
		} else if (msg.getType() == Protocol.common_voice) {// ��������
			MsgVoice Mv = new MsgVoice();
			CopyHead(msg, Mv);
			String Name = ReadString(Datain, 10);
			Mv.setName(Name);
			return Mv;
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
		} else if (msg.getType() == Protocol.common_online) {
			MsgOnline Mol = new MsgOnline();
			CopyHead(msg, Mol);
			String Name = ReadString(Datain, 10);
			Mol.setName(Name);
			return Mol;
		} else if (msg.getType() == Protocol.common_offline) {
			MsgOffline Mol = new MsgOffline();
			CopyHead(msg, Mol);
			String Name = ReadString(Datain, 10);
			Mol.setName(Name);
			return Mol;
		} else if (msg.getType() == Protocol.common_receivefile) {
			MsgReceiveFile Mrf = new MsgReceiveFile();
			CopyHead(msg, Mrf);
			int Packetnum = Datain.readInt();
			String Filename = ReadString(Datain, 100);
			byte[] filedata = new byte[Mrf.getTotalLength() - 4 - 1 - 4 - 4 - 4
					- 100];
			Datain.readFully(filedata);
			Mrf.setFile(Filename);
			Mrf.setPacketnum(Packetnum);
			Mrf.setFiledata(filedata);
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
			List<UserObject> List = new ArrayList<UserObject>();
			int num = Datain.readInt();
			while (num > 0) {
				num--;
				int number = Datain.readInt();
				String Name = ReadString(Datain, 10);
				UserObject User = new UserObject(number, null, Name, null, null);
				List.add(User);
			}
			Mfm.setUser(List);
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
