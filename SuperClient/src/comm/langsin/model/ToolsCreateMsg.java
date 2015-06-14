package comm.langsin.model;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.langsin.remove.MsgAddMember;
import com.langsin.remove.MsgAddMemberResp;
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

//�������ͻ��˵���Ϣ������
public class ToolsCreateMsg {
	public static byte[] PackMsg(MsgHead msg) throws IOException {// ����Ϊstatic����ֱ��ͨ������ȥ����
		ByteArrayOutputStream Byteou = new ByteArrayOutputStream();// �����ֽ�����������������ڲ����ڴ滺���������ݣ�ת�����ֽ�����
		DataOutputStream Dataou = new DataOutputStream(Byteou);// ���ֽ�����������ת��Ϊ���������󣬣��ô�����˵��
		WriteHead(Dataou, msg);
		byte MsgType = msg.getType();
		if (MsgType == Protocol.common_log) {// ��½Ӧ����Ϣ��Ϣ
			MsgLog Ml = (MsgLog) msg;
			WriteString(Dataou, 10, Ml.getPasswd());// ��½����
		} else if (MsgType == Protocol.common_reg) {// ע��������Ϣ
			MsgReg Mr = (MsgReg) msg;
			WriteString(Dataou, 10, Mr.getUsername());
			WriteString(Dataou, 10, Mr.getUserpwd());
			WriteString(Dataou, 100, Mr.getUserSign());
		} else if (MsgType == Protocol.common_finduser) {// ���Һ���������Ϣ
			MsgFindUser Mfu = (MsgFindUser) msg;
			Dataou.writeInt(Mfu.getFriendnum());
		} else if (MsgType == Protocol.common_findpacket) {// ����Ⱥ��������Ϣ
			MsgFindPacket Mfp = (MsgFindPacket) msg;
			Dataou.writeInt(Mfp.getPacketnum());
		} else if (MsgType == Protocol.common_createpacket) {// ����Ⱥ��������Ϣ
			MsgCreatePacket Mcp = (MsgCreatePacket) msg;
			WriteString(Dataou, 10, Mcp.getPacketname());
			WriteString(Dataou, 100, Mcp.getPacketsign());
		} else if (MsgType == Protocol.common_addfriend) {// ��Ӻ���������Ϣ
			MsgAddFriend Maf = (MsgAddFriend) msg;
			WriteString(Dataou, 10, Maf.getMyName());
			WriteString(Dataou, 100, Maf.getMymag());
		}else if(MsgType == Protocol.common_addfriend_resp){//��Ӻ���Ӧ����Ϣ
			MsgAddFriendResp Mafr = (MsgAddFriendResp) msg;
			Dataou.writeByte(Mafr.getState());
			WriteString(Dataou, 10, Mafr.getFriendname());	
		}else if(MsgType == Protocol.common_chatmsg){//����������Ϣ
			MsgSendChat Msc = (MsgSendChat) msg;
			WriteString(Dataou, 10, Msc.getName());
			int Msgsize = Msc.getMsgsize();
			Dataou.writeInt(Msgsize);
			String Msg = Msc.getMsg();
			WriteString(Dataou, Msgsize, Msg);
			int size = Msc.getBqnum();
			Dataou.writeInt(size);
			while(size>0){
				size--;
				Dataou.writeInt(Msc.getBqsize().get(size));
				Dataou.write(Msc.getBq().get(size));
			}
		}else if(MsgType == Protocol.common_nostatic){//��������
			MsgShake Msh =(MsgShake) msg;
			WriteString(Dataou, 10, Msh.getName());
		}else if(MsgType == Protocol.common_chatfile){//�����ļ�
			MsgSendFile Msf = (MsgSendFile) msg;
			WriteString(Dataou, 10, Msf.getName());
			WriteString(Dataou, 50, Msf.getFileName());
			Dataou.write(Msf.getFile());
		}else if(MsgType == Protocol.common_addgroup){//���Ⱥ��
			MsgAddGroup Mag = (MsgAddGroup) msg;
			Dataou.writeInt(Mag.getPacketnum());
			WriteString(Dataou, 10, Mag.getName());
			WriteString(Dataou, 10, Mag.getPascketname());
			WriteString(Dataou, 100, Mag.getContent());
		}else if(MsgType == Protocol.common_addgroupresp){//���Ⱥ��Ӧ����Ϣ
			MsgAddGroupResp Magr = (MsgAddGroupResp) msg;
			Dataou.writeInt(Magr.getPacketnum());
			WriteString(Dataou, 10, Magr.getName());
			WriteString(Dataou, 10, Magr.getPacketname());
			Dataou.writeByte(Magr.getState());
		}else if(MsgType == Protocol.common_getmember){//���Ⱥ��Ա������Ϣ
			MsgGetMember Mgm = (MsgGetMember) msg;
			Dataou.writeInt(Mgm.getPacketnum());
		}else if(MsgType == Protocol.common_chatgroupmsg){//Ⱥ��
			MsgGroupMsg Mgm = (MsgGroupMsg) msg;
			Dataou.writeInt(Mgm.getPacketnum());
			WriteString(Dataou, 10, Mgm.getPacketname());
			WriteString(Dataou, 10, Mgm.getName());
			int Msgsize = Mgm.getMsgsize();
			Dataou.writeInt(Msgsize);
			String Msg = Mgm.getMsg();
			WriteString(Dataou, Msgsize, Msg);
			int size = Mgm.getBqnum();
			Dataou.writeInt(size);
			while(size>0){
				size--;
				Dataou.writeInt(Mgm.getBqsize().get(size));
				Dataou.write(Mgm.getBq().get(size));
			}
		}else if(MsgType == Protocol.common_remove){//ɾ������
			MsgRemove Mrv = (MsgRemove) msg;
			WriteString(Dataou, 10, Mrv.getName());
		}else if(MsgType == Protocol.commpn_remove_group){//�˳�Ⱥ��
			MsgRemoveGroup Mrg = (MsgRemoveGroup) msg;
			Dataou.writeInt(Mrg.getGroupNum());//����Ⱥ����
			WriteString(Dataou, 10, Mrg.getName());
			WriteString(Dataou, 10, Mrg.getGroupName());
		}else if(MsgType == Protocol.common_sendgroupfile){//Ⱥ�ļ�
			MsgGroupFile Mgf = (MsgGroupFile) msg;
			Dataou.writeInt(Mgf.getGroupnum());
			WriteString(Dataou, 100, Mgf.getFilename());
			WriteString(Dataou, 100, Mgf.getFilePath());
		}else if(MsgType == Protocol.common_receivefile){//����Ⱥ�ļ�
			MsgReceiveFile Mrf = (MsgReceiveFile) msg;
			Dataou.writeInt(Mrf.getPacketnum());
			WriteString(Dataou, 100, Mrf.getFile());
		}else if(MsgType == Protocol.common_removemeber){//ɾ��Ⱥ��Ա
			MsgRemoveMember Mrm = (MsgRemoveMember) msg;
			Dataou.writeInt(Mrm.getPacketnum());
			WriteString(Dataou, 10, Mrm.getMainname());
			WriteString(Dataou, 10, Mrm.getPascketname());
		}else if(MsgType == Protocol.common_findmember){
		}else if(MsgType == Protocol.common_addmember){//���Ⱥ��Ա������Ϣ
			MsgAddMember Mam = (MsgAddMember) msg;
			Dataou.writeInt(Mam.getPacketnum());
			WriteString(Dataou, 10, Mam.getName());
			WriteString(Dataou, 10, Mam.getPacketname());
		}else if(MsgType == Protocol.common_addmemberresp){
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
