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

//将发往客户端的消息对象打包
public class ToolsCreateMsg {
	public static byte[] PackMsg(MsgHead msg) throws IOException {// 定义为static可以直接通过类名去调用
		ByteArrayOutputStream Byteou = new ByteArrayOutputStream();// 创建字节数组输出流对象，用于捕获内存缓冲区的数据，转换成字节数组
		DataOutputStream Dataou = new DataOutputStream(Byteou);// 将字节数组流对象转换为数据流对象，（好处不用说）
		WriteHead(Dataou, msg);
		byte MsgType = msg.getType();
		if (MsgType == Protocol.common_log_resp) {// 登陆应答消息消息
			MsgLogResp Mlr = (MsgLogResp) msg;
			if (Mlr.getTotalLength() == 14) {// 说明没有该用户
				Dataou.writeByte(Mlr.getState());
			} else {
				WriteString(Dataou, 10, Mlr.getUsername());// 发出用户昵称
				WriteString(Dataou, 100, Mlr.getUsersign());// 发出用户个性签名
				Dataou.write(Mlr.getUserhead());// 发出用户昵称
			}
		} else if (MsgType == Protocol.common_reg_resp) {
			MsgRegResp Mrr = (MsgRegResp) msg;
			byte State = Mrr.getState();
			Dataou.writeByte(State);// 发出注册状态
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
		} else if (MsgType == Protocol.common_addfriend) {// 添加好友
			MsgAddFriend Maf = (MsgAddFriend) msg;
			WriteString(Dataou, 10, Maf.getMyName());
			WriteString(Dataou, 100, Maf.getMymag());
			WriteString(Dataou, 100, Maf.getSign());
		} else if (MsgType == Protocol.common_addfriend_resp) {// 添加好友应答消息
			MsgAddFriendResp Mafr = (MsgAddFriendResp) msg;
			WriteString(Dataou, 10, Mafr.getFriendname());
			WriteString(Dataou, 100, Mafr.getFriendsign());
			Dataou.writeByte(Mafr.getState());
		} else if (MsgType == Protocol.common_friendlist) {// 好友列表
			MsgFriendList Mfl = (MsgFriendList) msg;
			int Friendnum = Mfl.getFriendlist().size();
			Dataou.writeInt(Friendnum);
			while (Friendnum > 0) {
				Friendnum--;
				int Friend = Mfl.getFriendlist().get(Friendnum).getUsernum();// 好友账号
				String Friendname = Mfl.getFriendlist().get(Friendnum)
						.getUsername();// 好友昵称
				String FriendSign = Mfl.getFriendlist().get(Friendnum)
						.getSignature();// 好友个性签名
				byte[] Friendhead = Mfl.getFriendlist().get(Friendnum)
						.getUserhead();// 好友头像
				Dataou.writeInt(Friend);
				WriteString(Dataou, 10, Friendname);
				WriteString(Dataou, 100, FriendSign);
				Dataou.write(Friendhead);
			}
		} else if (MsgType == Protocol.common_chatmsg) {// 发送单聊消息
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
		} else if (MsgType == Protocol.common_nostatic) {// 抖动窗体
			MsgShake Msh = (MsgShake) msg;
			WriteString(Dataou, 10, Msh.getName());
		} else if (MsgType == Protocol.common_chatfile) {// 发送文件
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
		} else if (MsgType == Protocol.common_grouplist) {// 群组列表
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
		} else if (MsgType == Protocol.common_addgroupresp) {// 添加群组应答消息
			MsgAddGroupResp Magr = (MsgAddGroupResp) msg;
			Dataou.writeInt(Magr.getPacketnum());
			WriteString(Dataou, 10, Magr.getName());
			WriteString(Dataou, 10, Magr.getPacketname());
			Dataou.writeByte(Magr.getState());
		} else if (MsgType == Protocol.common_getmember_resp) {// 群成员应答消息
			MsgGetMemberResp Mgmr = (MsgGetMemberResp) msg;
			int Size = Mgmr.getMembernum();
			Dataou.writeInt(Size);// 数量
			Dataou.writeByte(Mgmr.getState());// 表示是否是群主
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
		} else if (MsgType == Protocol.common_chatgroupmsg) {// 群聊
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
		} else if (MsgType == Protocol.common_remove) {// 删除好友
			MsgRemove Mrv = (MsgRemove) msg;
			WriteString(Dataou, 10, Mrv.getName());
		} else if (MsgType == Protocol.commpn_remove_group) {// 退出群组
			MsgRemoveGroup Mrg = (MsgRemoveGroup) msg;
			Dataou.writeInt(Mrg.getGroupNum());// 发出群号码
			WriteString(Dataou, 10, Mrg.getName());
			WriteString(Dataou, 10, Mrg.getGroupName());
		} else if (MsgType == Protocol.common_online) {// 上线通知
			MsgOnline Mol = (MsgOnline) msg;
			WriteString(Dataou, 10, Mol.getName());
		} else if (MsgType == Protocol.common_offline) {// 下载通知
			MsgOffline Mol = (MsgOffline) msg;
			WriteString(Dataou, 10, Mol.getName());
		} else if (MsgType == Protocol.common_receivefile) {// 接受群文件
			MsgReceiveFile Mrf = (MsgReceiveFile) msg;
			Dataou.writeInt(Mrf.getPacketnum());
			WriteString(Dataou, 100, Mrf.getFile());
			Dataou.write(Mrf.getFiledata());
		} else if (MsgType == Protocol.common_removemeber) {// 删除群成员
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
		} else if (MsgType == Protocol.common_addmember) {// 添加群成员请求消息
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
			throws IOException {// 该方法用于写出消息头对象
		dous.writeInt(m.getTotalLength());// 写出消息总长度
		dous.writeByte(m.getType());// 写出消息类型
		dous.writeInt(m.getSrcNum());// 写出源地址
		dous.writeInt(m.getDestNum());// 写出目标地址
	}

	private static void WriteString(DataOutputStream dous, int len, String s)
			throws IOException {// 将消息长度不够的用“\0”补齐
		byte[] data = s.getBytes();
		int length = data.length;// 消息真实长度
		if (length > len) {
			throw new IOException("写入长度为" + data.length + ",超长!");
		}
		dous.write(data);
		while (length < len) {// 如果消息长度不够则用“\0”补齐
			dous.writeByte('\0');
			len--;
		}
	}
}
