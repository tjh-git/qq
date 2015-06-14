package comm.langsin.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import comm.langsin.createadd.MsgCreatePacket;
import comm.langsin.createadd.MsgCreatePacketResp;
import comm.langsin.find.MsgFindPacket;
import comm.langsin.find.MsgFindPacketResp;
import comm.langsin.find.MsgFindUser;
import comm.langsin.find.MsgFindUserResp;
import comm.langsin.jdbcuntil.JdbcUntil;
import comm.langsin.model.PacketObject;
import comm.langsin.model.UserObject;
import comm.langsin.msg_file.MsgGetMember;
import comm.langsin.msg_file.MsgGetMemberResp;
import comm.langsin.msg_file.MsgGroupFile;
import comm.langsin.msg_file.MsgGroupMsg;
import comm.langsin.msg_file.MsgReceiveFile;
import comm.langsin.msg_file.MsgSendChat;
import comm.langsin.msg_file.MsgSendFile;
import comm.langsin.msg_file.MsgShake;
import comm.langsin.msg_file.MsgVoice;
import comm.langsin.msg_file.MsgVoiceResp;
import comm.langsin.msg_log_reg.MsgHead;
import comm.langsin.protocol.Protocol;

public class DealTools {
	// ����Map���ڱ����½���̶߳���
	private static JdbcUntil JDBC = new JdbcUntil();
	private static Map<UserObject, ServerThread> stlist = new HashMap<UserObject, ServerThread>();

	/**
	 * ���췽��˽��
	 * **/
	private DealTools() {

	}

	/**
	 * ���û���½�ɹ��󽫶�Ӧ�Ĵ����̶߳�����뵽������ ��������ѷ���������Ϣ
	 * 
	 * @param ct
	 *            :�����̶߳���
	 */
	public static void SendMsg(UserObject SrcUser, MsgHead msg) {
		if (msg.getType() == Protocol.common_finduser) {// ���Һ���
			MsgFindUser Mfu = (MsgFindUser) msg;
			UserObject User = JDBC.FindUser(Mfu.getFriendnum());
			if (User != null) {
				MsgFindUserResp Mfur = new MsgFindUserResp();
				Mfur.setTotalLength(4 + 1 + 4 + 4 + 4 + 10 + 5318);
				Mfur.setType(Protocol.common_finduser_resp);
				Mfur.setSrcNum(Protocol.ServerNUMBER);
				Mfur.setDestNum(Mfur.getSrcNum());
				Mfur.setFriendnum(User.getUsernum());
				Mfur.setFriendname(User.getUsername());
				Mfur.setFriendhead(User.getUserhead());
				stlist.get(SrcUser).SendMsgToClient(Mfur);
			} else {

			}
		} else if (msg.getType() == Protocol.common_findpacket) {// ����Ⱥ��
			MsgFindPacket Mfp = (MsgFindPacket) msg;
			PacketObject Packet = JDBC.FindGroup(Mfp.getPacketnum());
			if (Packet != null) {
				MsgFindPacketResp Mfpr = new MsgFindPacketResp();
				Mfpr.setTotalLength(4 + 1 + 4 + 4 + 4 + 4 + 10 + 1218);
				Mfpr.setType(Protocol.common_findpacket_resp);
				Mfpr.setSrcNum(Protocol.ServerNUMBER);
				Mfpr.setDestNum(Mfp.getSrcNum());
				Mfpr.setMainnum(Packet.getMainnum());
				Mfpr.setPacketnum(Packet.getPacketnum());
				Mfpr.setPacketname(Packet.getPacketname());
				Mfpr.setPackethead(Packet.getPackethead());
				stlist.get(SrcUser).SendMsgToClient(Mfpr);
			} else {

			}
		} else if (msg.getType() == Protocol.common_createpacket) {// ����Ⱥ��������Ϣ
			MsgCreatePacket Mcp = (MsgCreatePacket) msg;
			int MaxPacketnum = JDBC.GetPacketNum();
			int Destnum = Mcp.getSrcNum();
			JDBC.SaveMember(Destnum, MaxPacketnum);
			String Packetname = Mcp.getPacketname();
			String Packetsign = Mcp.getPacketsign();
			JDBC.CreatePacket(Destnum, MaxPacketnum, Packetname, Packetsign);
			MsgCreatePacketResp Mcpr = new MsgCreatePacketResp();
			Mcpr.setTotalLength(4 + 1 + 4 + 4 + 4 + 10 + 100);
			Mcpr.setType(Protocol.common_createpacket_resp);
			Mcpr.setSrcNum(Protocol.ServerNUMBER);
			Mcpr.setDestNum(Destnum);
			Mcpr.setPacketnum(MaxPacketnum);
			Mcpr.setPacketname(Packetname);
			Mcpr.setPacketsign(Packetsign);
			stlist.get(SrcUser).SendMsgToClient(Mcpr);
		} else if (msg.getType() == Protocol.common_addfriend) {// ��Ӻ���������Ϣ
			MsgAddFriend Maf = (MsgAddFriend) msg;
			boolean flag = false;// �������ߵı�־
			String Sign = JDBC.GetSign(Maf.getSrcNum());
			Maf.setSign(Sign);
			Maf.setTotalLength(Maf.getTotalLength() + 100);
			int Destnum = Maf.getDestNum();
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Destnum) {
					stlist.get(obj).SendMsgToClient(Maf);
					flag = true;
				}
			}
			if (!flag) {
				JDBC.AddFriendOffline(Maf.getSrcNum(), Destnum,
						Maf.getMyName(), Sign, Maf.getMymag());
			}
		} else if (msg.getType() == Protocol.common_addfriend_resp) {// ��Ӻ���Ӧ����Ϣ
			MsgAddFriendResp Mafr = (MsgAddFriendResp) msg;
			int Mainnum = Mafr.getSrcNum();
			int Friendnum = Mafr.getDestNum();
			boolean flag = false;
			String Sign = JDBC.GetSign(Mainnum);
			Mafr.setTotalLength(Mafr.getTotalLength() + 100);
			Mafr.setFriendsign(Sign);
			if (Mafr.getState() == 0) {// ��ʾͬ�����Ϊ���ѣ������ݱ��浽���ݿ�
				JDBC.SaveFriend(Mainnum, Friendnum);
				JDBC.SaveFriend(Friendnum, Mainnum);	
			}
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Friendnum) {
					stlist.get(obj).SendMsgToClient(Mafr);
					flag = true;
				}
			}
			if (!flag) {
				JDBC.SaveAddFriendResp(Mainnum, Friendnum,
						Mafr.getFriendname(), Mafr.getFriendsign(),
						Mafr.getState());
			}
		} else if (msg.getType() == Protocol.common_chatmsg) {
			MsgSendChat Msc = (MsgSendChat) msg;
			boolean flag = false;// �������ߵı�־
			int Destnum = Msc.getDestNum();
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Destnum) {
					stlist.get(obj).SendMsgToClient(Msc);
					flag = true;
				}
			}
			if (!flag) {// �Է�������
				JDBC.SaveChatMsg(Msc.getSrcNum(), Destnum, Msc.getName(),
						Msc.getMsg());
			}
		} else if (msg.getType() == Protocol.common_nostatic) {// ��������
			MsgShake Msh = (MsgShake) msg;
			int Destnum = Msh.getDestNum();
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Destnum) {
					stlist.get(obj).SendMsgToClient(Msh);
				}
			}
		} else if (msg.getType() == Protocol.common_chatfile) {// �����ļ�
			MsgSendFile Msf = (MsgSendFile) msg;
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Msf.getDestNum()) {
					stlist.get(obj).SendMsgToClient(Msf);
				}
			}
		} else if (msg.getType() == Protocol.common_addgroup) {// ���Ⱥ��������Ϣ
			MsgAddGroup Mag = (MsgAddGroup) msg;
			boolean flag = false;
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Mag.getDestNum()) {
					stlist.get(obj).SendMsgToClient(Mag);
					flag = true;
				}
			}
			if (!flag) {
				JDBC.AddGroup(Mag.getSrcNum(), Mag.getDestNum(),
						Mag.getPacketnum(), Mag.getPascketname(),
						Mag.getName(), Mag.getContent());
			}
		} else if (msg.getType() == Protocol.common_addgroupresp) {// ���Ⱥ��Ӧ����Ϣ
			MsgAddGroupResp Magr = (MsgAddGroupResp) msg;
			if (Magr.getState() == 0) {
				JDBC.SaveMember(Magr.getDestNum(), Magr.getPacketnum());
			}
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Magr.getDestNum()) {
					stlist.get(obj).SendMsgToClient(Magr);
				}
			}
		} else if (msg.getType() == Protocol.common_getmember) {// ���Ⱥ��Ա������Ϣ
			MsgGetMember Mgm = (MsgGetMember) msg;
			int Destnum = Mgm.getSrcNum();
			List<UserObject> Member = JDBC.GetMember(Mgm.getPacketnum());
			List<Object> File = JDBC.GetGroupFile(Mgm.getPacketnum());
			int Filesize = File.size();
			int Size = Member.size();
			byte State = JDBC.GroupState(Mgm.getSrcNum(), Mgm.getPacketnum());
			MsgGetMemberResp Mgmr = new MsgGetMemberResp();
			Mgmr.setTotalLength(4 + 1 + 4 + 4 + 1 + 4 + Size * (4 + 10)
					+ Filesize * (100) + 4);
			Mgmr.setType(Protocol.common_getmember_resp);
			Mgmr.setSrcNum(Protocol.ServerNUMBER);
			Mgmr.setDestNum(Destnum);
			Mgmr.setState(State);
			Mgmr.setMembernum(Size);
			Mgmr.setList(Member);
			Mgmr.setFilenum(Filesize);
			Mgmr.setFile(File);
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Destnum) {
					stlist.get(obj).SendMsgToClient(Mgmr);
				}
			}
		} else if (msg.getType() == Protocol.common_chatgroupmsg) {// Ⱥ��
			MsgGroupMsg Mgm = (MsgGroupMsg) msg;
			int Srcnum = Mgm.getSrcNum();
			int Packetnum = Mgm.getPacketnum();
			List<Integer> Membernum = JDBC.GetAllMember(Srcnum, Packetnum);
			List<UserObject> MemberMsg = JDBC.GetMember(Packetnum);
			List<Object> File = JDBC.GetGroupFile(Packetnum);
			int num = MemberMsg.size();
			int Filenum = File.size();
			Mgm.setTotalLength(Mgm.getTotalLength() + 1 + 4 + num * (4 + 10)
					+ 4 + Filenum * 100);
			Mgm.setMembeList(MemberMsg);
			Mgm.setFilenum(Filenum);
			Mgm.setFile(File);
			int Destnum = 0;
			for (int i = 0; i < Membernum.size(); i++) {
				boolean flag = false;
				Iterator<UserObject> user = stlist.keySet().iterator();
				while (user.hasNext()) {
					UserObject obj = user.next();
					Destnum = Membernum.get(i);
					Mgm.setDestNum(Destnum);
					byte State = JDBC.GroupState(Membernum.get(i), Packetnum);
					Mgm.setState(State);
					if (obj.getUsernum() == Membernum.get(i)) {
						stlist.get(obj).SendMsgToClient(Mgm);
						flag = true;
					}
				}
				if (!flag) {// ��ʾû����
					JDBC.SaveGroupMsg(Srcnum, Mgm.getName(), Destnum,
							Packetnum, Mgm.getPacketname(), Mgm.getMsg());
				}
			}
		} else if (msg.getType() == Protocol.common_voice) {// ��������
			MsgVoice Mv = (MsgVoice) msg;
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Mv.getDestNum()) {
					stlist.get(obj).SendMsgToClient(Mv);
				}
			}
		} else if (msg.getType() == Protocol.common_voice_resp) {// ����Ӧ����Ϣ
			MsgVoiceResp Mvr = (MsgVoiceResp) msg;
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Mvr.getDestNum()) {
					stlist.get(obj).SendMsgToClient(Mvr);
				}
			}
		} else if (msg.getType() == Protocol.common_remove) {// ɾ������
			MsgRemove Mrv = (MsgRemove) msg;
			int Srcnum = Mrv.getSrcNum();
			int Destnum = Mrv.getDestNum();
			JDBC.RemoveFriend(Srcnum, Destnum);// ������Ҫ�����ݿ���ɾ������
			JDBC.RemoveFriend(Destnum, Srcnum);
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Destnum) {
					stlist.get(obj).SendMsgToClient(Mrv);
				}
			}
		} else if (msg.getType() == Protocol.commpn_remove_group) {// ɾ��Ⱥ��
			MsgRemoveGroup Mrg = (MsgRemoveGroup) msg;
			int Srcnum = Mrg.getSrcNum();
			int Groupnum = Mrg.getGroupNum();
			byte State = JDBC.GroupState(Srcnum, Groupnum);
			if (State != 0) {
				List<Integer> Members = JDBC.GetAllMember(Srcnum, Groupnum);
				JDBC.RemoveGroup(Srcnum, Groupnum);
				for (int j = 0; j < Members.size(); j++) {
					Iterator<UserObject> user = stlist.keySet().iterator();
					while (user.hasNext()) {
						UserObject obj = user.next();
						if (obj.getUsernum() == Members.get(j)) {
							stlist.get(obj).SendMsgToClient(Mrg);
						}
					}
				}
			} else {// ��ʾ�Լ���Ⱥ��
				System.out.println("����Ⱥ����������");
			}
		} else if (msg.getType() == Protocol.common_sendgroupfile) {
			MsgGroupFile Mgf = (MsgGroupFile) msg;
			int Groupnum = Mgf.getGroupnum();
			String Filename = Mgf.getFilename();
			String Filepath = Mgf.getFilePath();
			JDBC.PacketFile(Groupnum, Filename, Filepath);
		} else if (msg.getType() == Protocol.common_receivefile) {// ����Ⱥ�ʼ�������Ϣ
			MsgReceiveFile Mrf = (MsgReceiveFile) msg;
			int Packetnum = Mrf.getPacketnum();
			String Filename = Mrf.getFile();
			byte[] Filedata = JDBC.GetPacketFile(Packetnum, Filename);
			Mrf.setTotalLength(Mrf.getTotalLength() + Filedata.length);
			Mrf.setFiledata(Filedata);
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Mrf.getSrcNum()) {
					stlist.get(obj).SendMsgToClient(Mrf);
				}
			}
		} else if (msg.getType() == Protocol.common_removemeber) {// ɾ��Ⱥ��Ա
			MsgRemoveMember Mrm = (MsgRemoveMember) msg;
			JDBC.RemoveMember(Mrm.getDestNum(), Mrm.getPacketnum());
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Mrm.getDestNum()) {
					stlist.get(obj).SendMsgToClient(Mrm);
				}
			}
		} else if (msg.getType() == Protocol.common_findmember) {
			MsgFindMember Mfm = (MsgFindMember) msg;
			List<UserObject> List = JDBC.FindaddMember();
			int size = List.size();
			Mfm.setTotalLength(Mfm.getTotalLength() + size * (4 + 10) + 4);
			Mfm.setSize(size);
			Mfm.setUser(List);
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Mfm.getSrcNum()) {
					stlist.get(obj).SendMsgToClient(Mfm);
				}
			}
		} else if (msg.getType() == Protocol.common_addmember) {// ���Ⱥ��Ա������Ϣ
			MsgAddMember Mam = (MsgAddMember) msg;
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Mam.getDestNum()) {
					stlist.get(obj).SendMsgToClient(Mam);
				}
			}
		} else if (msg.getType() == Protocol.common_addmemberresp) {// ���Ⱥ��ԱӦ����Ϣ
			MsgAddMemberResp Mamr = (MsgAddMemberResp) msg;
			byte State = Mamr.getState();
			if (State == 0) {
				JDBC.SaveMember(Mamr.getSrcNum(), Mamr.getPacketnum());
			}
			Iterator<UserObject> user = stlist.keySet().iterator();
			while (user.hasNext()) {
				UserObject obj = user.next();
				if (obj.getUsernum() == Mamr.getDestNum()) {
					stlist.get(obj).SendMsgToClient(Mamr);
				}
			}
		}
	}

	public static boolean Client(UserObject User) {
		Iterator<UserObject> user = stlist.keySet().iterator();
		while (user.hasNext()) {
			UserObject obj = user.next();
			int OOnum = obj.getUsernum();
			if ((OOnum == User.getUsernum())) {
				// ������֤��Ϣ
				return true;
			}
		}
		return false;
	}

	public static void AddClient(UserObject User, ServerThread St) {
		stlist.put(User, St);
		List<Integer> AllFriendnum = JDBC.GetAllFriend(User.getUsernum());
		MsgOnline Mol = new MsgOnline();
		Mol.setTotalLength(4 + 1 + 4 + 4 + 10);
		Mol.setType(Protocol.common_online);
		Mol.setSrcNum(User.getUsernum());
		Mol.setName(User.getUsername());
		for (int i = 0; i < AllFriendnum.size(); i++) {
			Iterator<UserObject> iter = stlist.keySet().iterator();
			while (iter.hasNext()) {
				UserObject obj = iter.next();
				if (obj.getUsernum() == AllFriendnum.get(i)) {
					Mol.setDestNum(AllFriendnum.get(i));
					stlist.get(obj).SendMsgToClient(Mol);
				}
			}
		}
	}

	public static void RemoveClient(UserObject User) {
		if (User != null) {
			List<Integer> AllFriendnum = JDBC.GetAllFriend(User.getUsernum());
			MsgOffline Mol = new MsgOffline();
			Mol.setTotalLength(4 + 1 + 4 + 4 + 10);
			Mol.setType(Protocol.common_offline);
			Mol.setSrcNum(User.getUsernum());
			Mol.setName(User.getUsername());
			for (int i = 0; i < AllFriendnum.size(); i++) {
				Iterator<UserObject> iter = stlist.keySet().iterator();
				while (iter.hasNext()) {
					UserObject obj = iter.next();
					if (obj.getUsernum() == AllFriendnum.get(i)) {
						Mol.setDestNum(AllFriendnum.get(i));
						stlist.get(obj).SendMsgToClient(Mol);
					}
				}
			}
			ServerThread St1 = stlist.remove(User);
			if (St1 != null) {
				St1.disConn();
				St1 = null;
			}
		}
	}

}
