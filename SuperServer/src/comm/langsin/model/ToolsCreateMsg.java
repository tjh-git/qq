package comm.langsin.model;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
import comm.langsin.msg_log_reg.MsgHead;
import comm.langsin.msg_log_reg.MsgLogResp;
import comm.langsin.msg_log_reg.MsgRegResp;
import comm.langsin.msgoffline.MsgFriendList;
import comm.langsin.msgoffline.MsgGroupList;
import comm.langsin.protocol.Protocol;

//�������ͻ��˵���Ϣ������
public class ToolsCreateMsg {
	public static byte[] PackMsg(MsgHead msg) throws IOException {// ����Ϊstatic����ֱ��ͨ������ȥ����
		ByteArrayOutputStream Byteou = new ByteArrayOutputStream();// �����ֽ�����������������ڲ����ڴ滺���������ݣ�ת�����ֽ�����
		DataOutputStream Dataou = new DataOutputStream(Byteou);// ���ֽ�����������ת��Ϊ���������󣬣��ô�����˵��
		WriteHead(Dataou, msg);
		byte MsgType = msg.getType();
		if (MsgType == Protocol.common_log_resp) {// ��½Ӧ����Ϣ��Ϣ
			MsgLogResp Mlr = (MsgLogResp) msg;
			if (Mlr.getTotalLength() == 14) {// ˵��û�и��û�
				Dataou.writeByte(Mlr.getState());
			} else {
				WriteString(Dataou, 10, Mlr.getUsername());// �����û��ǳ�
				WriteString(Dataou, 100, Mlr.getUsersign());// �����û�����ǩ��
				Dataou.write(Mlr.getUserhead());// �����û��ǳ�
			}
		} else if (MsgType == Protocol.common_reg_resp) {
			MsgRegResp Mrr = (MsgRegResp) msg;
			byte State = Mrr.getState();
			Dataou.writeByte(State);// ����ע��״̬
		} else if (MsgType == Protocol.common_finduser_resp) {
			MsgFindUserResp Mfur = (MsgFindUserResp) msg;
			Dataou.writeInt(Mfur.getFriendnum());
			WriteString(Dataou, 10, Mfur.getFriendname());
			Dataou.write(Mfur.getFriendhead());
		} else if (MsgType == Protocol.common_findpacket_resp) {
			MsgFindPacketResp Mfpr = (MsgFindPacketResp) msg;
			Dataou.writeInt(Mfpr.getMainnum());
			Dataou.writeInt(Mfpr.getPacketnum());
			WriteString(Dataou, 10, Mfpr.getPacketname());
			Dataou.write(Mfpr.getPackethead());
		} else if (MsgType == Protocol.common_createpacket_resp) {
			MsgCreatePacketResp Mcpr = (MsgCreatePacketResp) msg;
			Dataou.writeInt(Mcpr.getPacketnum());
			WriteString(Dataou, 10, Mcpr.getPacketname());
			WriteString(Dataou, 100, Mcpr.getPacketsign());
		} else if (MsgType == Protocol.common_addfriend) {// ��Ӻ���
			MsgAddFriend Maf = (MsgAddFriend) msg;
			WriteString(Dataou, 10, Maf.getMyName());
			WriteString(Dataou, 100, Maf.getMymag());
			WriteString(Dataou, 100, Maf.getSign());
		} else if (MsgType == Protocol.common_addfriend_resp) {// ��Ӻ���Ӧ����Ϣ
			MsgAddFriendResp Mafr = (MsgAddFriendResp) msg;
			WriteString(Dataou, 10, Mafr.getFriendname());
			WriteString(Dataou, 100, Mafr.getFriendsign());
			Dataou.writeByte(Mafr.getState());
		} else if (MsgType == Protocol.common_friendlist) {// �����б�
			MsgFriendList Mfl = (MsgFriendList) msg;
			int Friendnum = Mfl.getFriendlist().size();
			Dataou.writeInt(Friendnum);
			while (Friendnum > 0) {
				Friendnum--;
				int Friend = Mfl.getFriendlist().get(Friendnum).getUsernum();// �����˺�
				String Friendname = Mfl.getFriendlist().get(Friendnum)
						.getUsername();// �����ǳ�
				String FriendSign = Mfl.getFriendlist().get(Friendnum)
						.getSignature();// ���Ѹ���ǩ��
				byte[] Friendhead = Mfl.getFriendlist().get(Friendnum)
						.getUserhead();// ����ͷ��
				Dataou.writeInt(Friend);
				WriteString(Dataou, 10, Friendname);
				WriteString(Dataou, 100, FriendSign);
				Dataou.write(Friendhead);
			}
		} else if (MsgType == Protocol.common_chatmsg) {// ���͵�����Ϣ
			MsgSendChat Msc = (MsgSendChat) msg;
			WriteString(Dataou, 10, Msc.getName());
			Dataou.writeInt(Msc.getMsgsize());
			String Msg = Msc.getMsg();
			WriteString(Dataou, Msg.getBytes().length, Msg);
			int size = Msc.getBq().size();
			Dataou.writeInt(Msc.getBqnum());
			while (size > 0) {
				size--;
				Dataou.writeInt(Msc.getBqsize().get(size));
				Dataou.write(Msc.getBq().get(size));
			}
		} else if (MsgType == Protocol.common_nostatic) {// ��������
			MsgShake Msh = (MsgShake) msg;
			WriteString(Dataou, 10, Msh.getName());
		} else if (MsgType == Protocol.common_chatfile) {// �����ļ�
			MsgSendFile Msf = (MsgSendFile) msg;
			WriteString(Dataou, 10, Msf.getName());
			WriteString(Dataou, 50, Msf.getFileName());
			Dataou.write(Msf.getFile());
		} else if (MsgType == Protocol.common_addgroup) {
			MsgAddGroup Mag = (MsgAddGroup) msg;
			Dataou.writeInt(Mag.getPacketnum());
			WriteString(Dataou, 10, Mag.getName());
			WriteString(Dataou, 10, Mag.getPascketname());
			WriteString(Dataou, 100, Mag.getContent());
		} else if (MsgType == Protocol.common_grouplist) {// Ⱥ���б�
			MsgGroupList Mgl = (MsgGroupList) msg;
			List<PacketObject> Packetlist = Mgl.getPacketlist();
			int size = Mgl.getGroupsize();
			Dataou.writeInt(size);
			while (size > 0) {
				size--;
				Dataou.writeInt(Packetlist.get(size).getPacketnum());
				WriteString(Dataou, 10, Packetlist.get(size).getPacketname());
				WriteString(Dataou, 100, Packetlist.get(size).getSign());
				Dataou.write(Packetlist.get(size).getPackethead());
			}
		} else if (MsgType == Protocol.common_addgroupresp) {// ���Ⱥ��Ӧ����Ϣ
			MsgAddGroupResp Magr = (MsgAddGroupResp) msg;
			Dataou.writeInt(Magr.getPacketnum());
			WriteString(Dataou, 10, Magr.getName());
			WriteString(Dataou, 10, Magr.getPacketname());
			Dataou.writeByte(Magr.getState());
		} else if (MsgType == Protocol.common_getmember_resp) {// Ⱥ��ԱӦ����Ϣ
			MsgGetMemberResp Mgmr = (MsgGetMemberResp) msg;
			int Size = Mgmr.getMembernum();
			Dataou.writeInt(Size);// ����
			Dataou.writeByte(Mgmr.getState());// ��ʾ�Ƿ���Ⱥ��
			while (Size > 0) {
				Size--;
				Dataou.writeInt(Mgmr.getList().get(Size).getUsernum());
				WriteString(Dataou, 10, Mgmr.getList().get(Size).getUsername());
			}
			int Filesize = Mgmr.getFilenum();
			Dataou.writeInt(Filesize);
			while (Filesize > 0) {
				Filesize--;
				WriteString(Dataou, 100, (String) Mgmr.getFile().get(Filesize));
			}
		} else if (MsgType == Protocol.common_chatgroupmsg) {// Ⱥ��
			MsgGroupMsg Mgm = (MsgGroupMsg) msg;
			Dataou.writeByte(Mgm.getState());
			Dataou.writeInt(Mgm.getPacketnum());
			WriteString(Dataou, 10, Mgm.getPacketname());
			WriteString(Dataou, 10, Mgm.getName());
			Dataou.writeInt(Mgm.getMsgsize());
			String Msg = Mgm.getMsg();
			WriteString(Dataou, Msg.getBytes().length, Msg);
			int size = Mgm.getBq().size();
			Dataou.writeInt(Mgm.getBqnum());
			while (size > 0) {
				size--;
				Dataou.writeInt(Mgm.getBqsize().get(size));
				Dataou.write(Mgm.getBq().get(size));
			}
			int Membernum = Mgm.getMembeList().size();
			Dataou.writeInt(Membernum);
			while (Membernum > 0) {
				Membernum--;
				Dataou.writeInt(Mgm.getMembeList().get(Membernum).getUsernum());
				WriteString(Dataou, 10, Mgm.getMembeList().get(Membernum)
						.getUsername());
			}
			int Filenum = Mgm.getFilenum();
			Dataou.writeInt(Filenum);
			List<Object> File = Mgm.getFile();
			while (Filenum > 0) {
				Filenum--;
				WriteString(Dataou, 100, (String) File.get(Filenum));
			}
		} else if (MsgType == Protocol.common_remove) {// ɾ������
			MsgRemove Mrv = (MsgRemove) msg;
			WriteString(Dataou, 10, Mrv.getName());
		} else if (MsgType == Protocol.commpn_remove_group) {// �˳�Ⱥ��
			MsgRemoveGroup Mrg = (MsgRemoveGroup) msg;
			Dataou.writeInt(Mrg.getGroupNum());// ����Ⱥ����
			WriteString(Dataou, 10, Mrg.getName());
			WriteString(Dataou, 10, Mrg.getGroupName());
		} else if (MsgType == Protocol.common_online) {// ����֪ͨ
			MsgOnline Mol = (MsgOnline) msg;
			WriteString(Dataou, 10, Mol.getName());
		} else if (MsgType == Protocol.common_offline) {// ����֪ͨ
			MsgOffline Mol = (MsgOffline) msg;
			WriteString(Dataou, 10, Mol.getName());
		} else if (MsgType == Protocol.common_receivefile) {// ����Ⱥ�ļ�
			MsgReceiveFile Mrf = (MsgReceiveFile) msg;
			Dataou.writeInt(Mrf.getPacketnum());
			WriteString(Dataou, 100, Mrf.getFile());
			Dataou.write(Mrf.getFiledata());
		} else if (MsgType == Protocol.common_removemeber) {// ɾ��Ⱥ��Ա
			MsgRemoveMember Mrm = (MsgRemoveMember) msg;
			Dataou.writeInt(Mrm.getPacketnum());
			WriteString(Dataou, 10, Mrm.getMainname());
			WriteString(Dataou, 10, Mrm.getPascketname());
		} else if (MsgType == Protocol.common_findmember) {
			MsgFindMember Mfm = (MsgFindMember) msg;
			List<UserObject> List = Mfm.getUser();
			int num = Mfm.getSize();
			Dataou.writeInt(num);
			while (num > 0) {
				num--;
				Dataou.writeInt(List.get(num).getUsernum());
				WriteString(Dataou, 10, List.get(num).getUsername());
			}
		} else if (MsgType == Protocol.common_addmember) {// ���Ⱥ��Ա������Ϣ
			MsgAddMember Mam = (MsgAddMember) msg;
			Dataou.writeInt(Mam.getPacketnum());
			WriteString(Dataou, 10, Mam.getName());
			WriteString(Dataou, 10, Mam.getPacketname());
		} else if (MsgType == Protocol.common_addmemberresp) {
			MsgAddMemberResp Mamr = (MsgAddMemberResp) msg;
			Dataou.writeInt(Mamr.getPacketnum());
			Dataou.writeByte(Mamr.getState());
			WriteString(Dataou, 10, Mamr.getName());
			WriteString(Dataou, 10, Mamr.getPacketname());
		}
		Dataou.flush();
		byte[] data = Byteou.toByteArray();
		return data;
	}

	private static void WriteHead(DataOutputStream dous, MsgHead m)
			throws IOException {// �÷�������д����Ϣͷ����
		dous.writeInt(m.getTotalLength());// д����Ϣ�ܳ���
		dous.writeByte(m.getType());// д����Ϣ����
		dous.writeInt(m.getSrcNum());// д��Դ��ַ
		dous.writeInt(m.getDestNum());// д��Ŀ���ַ
	}

	private static void WriteString(DataOutputStream dous, int len, String s)
			throws IOException {// ����Ϣ���Ȳ������á�\0������
		byte[] data = s.getBytes();
		int length = data.length;// ��Ϣ��ʵ����
		if (length > len) {
			throw new IOException("д�볤��Ϊ" + data.length + ",����!");
		}
		dous.write(data);
		while (length < len) {// �����Ϣ���Ȳ������á�\0������
			dous.writeByte('\0');
			len--;
		}
	}
}
