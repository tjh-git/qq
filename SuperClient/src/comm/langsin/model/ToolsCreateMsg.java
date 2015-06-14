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

//将发往客户端的消息对象打包
public class ToolsCreateMsg {
	public static byte[] PackMsg(MsgHead msg) throws IOException {// 定义为static可以直接通过类名去调用
		ByteArrayOutputStream Byteou = new ByteArrayOutputStream();// 创建字节数组输出流对象，用于捕获内存缓冲区的数据，转换成字节数组
		DataOutputStream Dataou = new DataOutputStream(Byteou);// 将字节数组流对象转换为数据流对象，（好处不用说）
		WriteHead(Dataou, msg);
		byte MsgType = msg.getType();
		if (MsgType == Protocol.common_log) {// 登陆应答消息消息
			MsgLog Ml = (MsgLog) msg;
			WriteString(Dataou, 10, Ml.getPasswd());// 登陆密码
		} else if (MsgType == Protocol.common_reg) {// 注册请求消息
			MsgReg Mr = (MsgReg) msg;
			WriteString(Dataou, 10, Mr.getUsername());
			WriteString(Dataou, 10, Mr.getUserpwd());
			WriteString(Dataou, 100, Mr.getUserSign());
		} else if (MsgType == Protocol.common_finduser) {// 查找好友请求消息
			MsgFindUser Mfu = (MsgFindUser) msg;
			Dataou.writeInt(Mfu.getFriendnum());
		} else if (MsgType == Protocol.common_findpacket) {// 查找群组请求消息
			MsgFindPacket Mfp = (MsgFindPacket) msg;
			Dataou.writeInt(Mfp.getPacketnum());
		} else if (MsgType == Protocol.common_createpacket) {// 创建群组请求消息
			MsgCreatePacket Mcp = (MsgCreatePacket) msg;
			WriteString(Dataou, 10, Mcp.getPacketname());
			WriteString(Dataou, 100, Mcp.getPacketsign());
		} else if (MsgType == Protocol.common_addfriend) {// 添加好友请求消息
			MsgAddFriend Maf = (MsgAddFriend) msg;
			WriteString(Dataou, 10, Maf.getMyName());
			WriteString(Dataou, 100, Maf.getMymag());
		}else if(MsgType == Protocol.common_addfriend_resp){//添加好友应答消息
			MsgAddFriendResp Mafr = (MsgAddFriendResp) msg;
			Dataou.writeByte(Mafr.getState());
			WriteString(Dataou, 10, Mafr.getFriendname());	
		}else if(MsgType == Protocol.common_chatmsg){//发送聊天消息
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
		}else if(MsgType == Protocol.common_nostatic){//抖动窗体
			MsgShake Msh =(MsgShake) msg;
			WriteString(Dataou, 10, Msh.getName());
		}else if(MsgType == Protocol.common_chatfile){//发送文件
			MsgSendFile Msf = (MsgSendFile) msg;
			WriteString(Dataou, 10, Msf.getName());
			WriteString(Dataou, 50, Msf.getFileName());
			Dataou.write(Msf.getFile());
		}else if(MsgType == Protocol.common_addgroup){//添加群组
			MsgAddGroup Mag = (MsgAddGroup) msg;
			Dataou.writeInt(Mag.getPacketnum());
			WriteString(Dataou, 10, Mag.getName());
			WriteString(Dataou, 10, Mag.getPascketname());
			WriteString(Dataou, 100, Mag.getContent());
		}else if(MsgType == Protocol.common_addgroupresp){//添加群组应答消息
			MsgAddGroupResp Magr = (MsgAddGroupResp) msg;
			Dataou.writeInt(Magr.getPacketnum());
			WriteString(Dataou, 10, Magr.getName());
			WriteString(Dataou, 10, Magr.getPacketname());
			Dataou.writeByte(Magr.getState());
		}else if(MsgType == Protocol.common_getmember){//获得群成员请求消息
			MsgGetMember Mgm = (MsgGetMember) msg;
			Dataou.writeInt(Mgm.getPacketnum());
		}else if(MsgType == Protocol.common_chatgroupmsg){//群聊
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
		}else if(MsgType == Protocol.common_remove){//删除好友
			MsgRemove Mrv = (MsgRemove) msg;
			WriteString(Dataou, 10, Mrv.getName());
		}else if(MsgType == Protocol.commpn_remove_group){//退出群组
			MsgRemoveGroup Mrg = (MsgRemoveGroup) msg;
			Dataou.writeInt(Mrg.getGroupNum());//发出群号码
			WriteString(Dataou, 10, Mrg.getName());
			WriteString(Dataou, 10, Mrg.getGroupName());
		}else if(MsgType == Protocol.common_sendgroupfile){//群文件
			MsgGroupFile Mgf = (MsgGroupFile) msg;
			Dataou.writeInt(Mgf.getGroupnum());
			WriteString(Dataou, 100, Mgf.getFilename());
			WriteString(Dataou, 100, Mgf.getFilePath());
		}else if(MsgType == Protocol.common_receivefile){//接受群文件
			MsgReceiveFile Mrf = (MsgReceiveFile) msg;
			Dataou.writeInt(Mrf.getPacketnum());
			WriteString(Dataou, 100, Mrf.getFile());
		}else if(MsgType == Protocol.common_removemeber){//删除群成员
			MsgRemoveMember Mrm = (MsgRemoveMember) msg;
			Dataou.writeInt(Mrm.getPacketnum());
			WriteString(Dataou, 10, Mrm.getMainname());
			WriteString(Dataou, 10, Mrm.getPascketname());
		}else if(MsgType == Protocol.common_findmember){
		}else if(MsgType == Protocol.common_addmember){//添加群成员请求消息
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
