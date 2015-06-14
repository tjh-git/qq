package comm.langsin.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import comm.langsin.createadd.MsgAddFriend;
import comm.langsin.createadd.MsgAddFriendResp;
import comm.langsin.createadd.MsgAddGroup;
import comm.langsin.jdbcuntil.JdbcUntil;
import comm.langsin.model.PacketObject;
import comm.langsin.model.ToolsCreateMsg;
import comm.langsin.model.ToolsParseMsg;
import comm.langsin.model.UserObject;
import comm.langsin.msg_file.MsgGroupMsg;
import comm.langsin.msg_file.MsgSendChat;
import comm.langsin.msg_log_reg.MsgHead;
import comm.langsin.msg_log_reg.MsgLog;
import comm.langsin.msg_log_reg.MsgLogResp;
import comm.langsin.msg_log_reg.MsgReg;
import comm.langsin.msg_log_reg.MsgRegResp;
import comm.langsin.msgoffline.MsgFriendList;
import comm.langsin.msgoffline.MsgGroupList;
import comm.langsin.protocol.Protocol;

public class ServerThread extends Thread {// 服务器处理客户端线程
	private JdbcUntil JDBC = new JdbcUntil();
	private Socket ClientObject = null;// 客户端线程对象
	private InputStream Ins = null;// 读取客户端发来的消息
	private OutputStream Ous = null;// 向客户端发送消息
	private DataInputStream Datain = null;// 数据流对象对基本流对象进行包装
	private DataOutputStream Dataou = null;// 数据流对象对基本流对象进行包装
	private UserObject User = null;// 用户对象
	private boolean LogOk = false;// 用户登陆成功的标志

	public ServerThread(Socket Client) {// 构造方法
		this.ClientObject = Client;
		try {
			this.Ins = Client.getInputStream();// 用于接收客户端发来的数据的输入流
			this.Ous = Client.getOutputStream();// 用于向客户端发送消息的输出流
			this.Datain = new DataInputStream(Ins);// 将普通输入流对象包装成
			this.Dataou = new DataOutputStream(Ous);// 将普通输入流对象包装成
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UserObject getUser() {// 取得这个线程对象代表的用户对象；
		return this.User;
	}

	public void run() {
		LogOk = ReadFirstMsg();// 读取为true
		System.out.println(LogOk);
		if (LogOk) {
			DealTools.AddClient(User, this);// 将线程保存到Map队列中
		}
		while (LogOk) {// 登陆成功跳转到消息处理类，等到玩家匹配
			try {
				MsgHead Msg = this.ReceiveData();
				DealTools.SendMsg(User, Msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				LogOk = false;
				// 这句话是让服务器打印，注释掉的话就不会报 Connection refused
			}
		}
		DealTools.RemoveClient(User);// 当用户没加入游戏推出时将线程移除
	}

	private boolean ReadFirstMsg() {// 接收客户端发来的第一条消息
		try {
			MsgHead Msg = ReceiveData();
			if (Msg.getType() == Protocol.common_log) {// 登陆请求消息
				MsgLog Ml = (MsgLog) Msg;
				return CheckLogin(Ml);
			} else if (Msg.getType() == Protocol.common_reg) {// 注册请求消息
				MsgReg Mr = (MsgReg) Msg;// 注册请求消息
				int Srcnum = JDBC.GetMaxNum();// 从数据库中获得最大的账号作为返回给注册者
				JDBC.Reg(Srcnum, Mr.getUsername(), Mr.getUserpwd(),
						Mr.getUserSign()); // 将注册消息资料保存到数据库
				MsgRegResp Mrr = new MsgRegResp();// 注册应答消息
				Mrr.setTotalLength(4 + 1 + 4 + 4 + 1);
				Mrr.setType(Protocol.common_reg_resp);
				Mrr.setSrcNum(Protocol.ServerNUMBER);
				Mrr.setDestNum(Srcnum);
				Mrr.setState((byte) 1);
				this.SendMsgToClient(Mrr);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return false;
	}

	private boolean CheckLogin(MsgLog msg) throws IOException {// 检验是否登陆成功
		int Srcnum = msg.getSrcNum();
		String Pwd = msg.getPasswd();
		User = JDBC.Log(Srcnum, Pwd);// 到数据库中去验证
		MsgLogResp Mlr = new MsgLogResp();
		if (User != null) {
			String Nickname = User.getUsername();// 得到用户昵称
			String UserSign = User.getSignature();// 得到用户个性签名
			byte[] UserHead = User.getUserhead();// 得到用户头像
			Mlr.setTotalLength(4 + 1 + 4 + 4 + 10 + 100 + 5318);
			Mlr.setType(Protocol.common_log_resp);
			Mlr.setSrcNum(Protocol.ServerNUMBER);
			Mlr.setDestNum(Srcnum);
			Mlr.setUsername(Nickname);
			Mlr.setUsersign(UserSign);
			Mlr.setUserhead(UserHead);
			this.SendMsgToClient(Mlr);
			SendFriendList(Srcnum);
			SendGroupList(Srcnum);
			GetChatMsg(Srcnum);
			GetGroupMsg(Srcnum);
			GetAddFriendOffline(Srcnum);
			GetAddFriendResp(Srcnum);
			GetAddGroup(Srcnum);
			return true;
		} else if (User == null) {
			Mlr.setTotalLength(4 + 1 + 4 + 4 + 1);
			Mlr.setType(Protocol.common_log_resp);
			Mlr.setSrcNum(Protocol.ServerNUMBER);
			Mlr.setDestNum(Srcnum);
			Mlr.setState((byte) 1);
			this.SendMsgToClient(Mlr);
		}
		this.disConn();
		return false;
	}

	private MsgHead ReceiveData() throws Exception {// 从流对象上读取数据块，解析为消息对象
		int TotalLength = Datain.readInt();// 读取消息总长度
		System.out.println("服务器读取到消息总长度为:  " + TotalLength);
		byte[] Data = new byte[TotalLength - 4];// 减去消息总长度创建出byte数组，用于存放数据
		Datain.readFully(Data);// 必须用readFully
		System.out.println("服务器将消息放入缓冲区 Data【】数组中！！！！");
		MsgHead Msg = ToolsParseMsg.parseMsg(Data);// 消息对象解包
		System.out.println("服务器读到的消息对象" + Msg);
		return Msg;
	}

	// 发送离线以及好友列表的方法
	public void SendFriendList(int MAINNUM) {
		List<UserObject> Friendlist = JDBC.FriendList(MAINNUM);
		MsgFriendList Mfl = new MsgFriendList();
		int Friendnum = Friendlist.size();
		Mfl.setTotalLength(4 + 1 + 4 + 4 + 4 + Friendnum
				* (4 + 10 + 100 + 5318));
		Mfl.setType(Protocol.common_friendlist);
		Mfl.setSrcNum(Protocol.ServerNUMBER);
		Mfl.setDestNum(MAINNUM);
		Mfl.setFriendsize(Friendnum);
		Mfl.setFriendlist(Friendlist);
		this.SendMsgToClient(Mfl);
	}

	// 发送群组列表
	public void SendGroupList(int Mainnum) {
		List<PacketObject> Packetlist = JDBC.GroupList(Mainnum);
		MsgGroupList Mgl = new MsgGroupList();
		int Groupnum = Packetlist.size();
		if (Groupnum > 0) {
			Mgl.setTotalLength(4 + 1 + 4 + 4 + 4 + Groupnum
					* (4 + 10 + 100 + 1218));
			Mgl.setType(Protocol.common_grouplist);
			Mgl.setSrcNum(Protocol.ServerNUMBER);
			Mgl.setDestNum(Mainnum);
			Mgl.setPacketlist(Packetlist);
			Mgl.setGroupsize(Packetlist.size());
			this.SendMsgToClient(Mgl);
		}
	}

	public boolean SendMsgToClient(MsgHead msg) {// 发送消息给客户端
		try {
			byte[] data = ToolsCreateMsg.PackMsg(msg);
			this.Dataou.write(data);
			this.Dataou.flush();
			System.out.println("服务器发出消息对象：" + msg);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("服务器发出消息对象出错：" + msg);
		return false;
	}

	public void GetChatMsg(int Destnum) {// 获得离线消息
		List<Object> Msg = JDBC.GetChatMsg(Destnum);
		int NUM = Msg.size();
		while (NUM > 0) {
			NUM = NUM - 3;
			MsgSendChat Msc = new MsgSendChat();
			int Srcnum = (Integer) Msg.get(0);
			String Srcname = (String) Msg.get(1);
			String Info = (String) Msg.get(2);
			Msc.setTotalLength(4 + 1 + 4 + 4 + 4 + 4 + 10
					+ Info.getBytes().length);
			Msc.setSrcNum(Srcnum);
			Msc.setDestNum(Destnum);
			Msc.setType(Protocol.common_chatmsg);
			Msc.setMsg(Info);
			Msc.setName(Srcname);
			Msc.setBqnum(0);
			Msc.setMsgsize(Info.getBytes().length);
			this.SendMsgToClient(Msc);
		}
	}

	public void GetGroupMsg(int Srcnum) {
		List<Object> GroupMsg = JDBC.GetGroupMsg(Srcnum);
		int NUM = GroupMsg.size();
		while (NUM > 0) {
			NUM = NUM - 5;
			MsgGroupMsg Mgm = new MsgGroupMsg();
			Mgm.setType(Protocol.common_chatgroupmsg);
			Mgm.setDestNum(Srcnum);
			Mgm.setSrcNum((Integer) GroupMsg.get(0));
			Mgm.setName((String) GroupMsg.get(1));
			Mgm.setPacketname((String) GroupMsg.get(3));
			Mgm.setPacketnum((Integer) GroupMsg.get(2));
			List<UserObject> Member = JDBC.GetMember((Integer) GroupMsg.get(2));
			Mgm.setMsg((String) GroupMsg.get(4));
			Mgm.setMembeList(Member);
			Mgm.setMsgsize(GroupMsg.get(4).toString().getBytes().length);
			Mgm.setTotalLength(4 + 1 + 4 + 4 + 10 + 10 + 4
					+ GroupMsg.get(4).toString().getBytes().length + 4 + 1 + 4
					+ 4 + Member.size() * (4 + 10) + 4);
			byte State = JDBC.GroupState(Srcnum, (Integer) GroupMsg.get(2));
			Mgm.setBqnum(0);
			Mgm.setState(State);
			Mgm.setFilenum(0);
			this.SendMsgToClient(Mgm);
		}
	}

	public void GetAddFriendOffline(int Srcnum) {
		List<Object> Msg = JDBC.GetAddFriendOffline(Srcnum);
		int NUM =Msg.size();
		while(NUM>0){
			NUM = NUM - 4;
			MsgAddFriend Maf = new MsgAddFriend();
			Maf.setTotalLength(4 + 1 + 4 + 4 + 10 + 100 + 100);
			Maf.setType(Protocol.common_addfriend);
			Maf.setSrcNum((Integer)Msg.get(0));
			Maf.setDestNum(Srcnum);
			Maf.setMyName((String) Msg.get(1));
			Maf.setSign((String) Msg.get(2));
			Maf.setMymag((String) Msg.get(3));
			this.SendMsgToClient(Maf);
		}
	}
	public void GetAddFriendResp(int Srcnum){
		List<Object> Msg = JDBC.GetAddFriendResp(Srcnum);
		int NUM = Msg.size();
		while(NUM>0){
			NUM = NUM-4;
			MsgAddFriendResp Mafr = new MsgAddFriendResp();
			Mafr.setTotalLength(4 + 1 + 4 + 4 + 10 + 100 + 1);
			Mafr.setType(Protocol.common_addfriend_resp);
			Mafr.setSrcNum((Integer) Msg.get(0));
			Mafr.setDestNum(Srcnum);
			Mafr.setFriendname((String) Msg.get(1));
			Mafr.setFriendsign((String) Msg.get(2));
			Mafr.setState((Byte) Msg.get(3));
			this.SendMsgToClient(Mafr);
		}
	}
	public void GetAddGroup(int Mainnum){
		List<Object> Msg = JDBC.GetAddGroup(Mainnum);
		int NUM =Msg.size();
		while(NUM>0){
			NUM = NUM - 5;
			MsgAddGroup Mag = new MsgAddGroup();
			Mag.setTotalLength(4 + 1 + 4 + 4 + 4 + 10 + 10 + 100);
			Mag.setType(Protocol.common_addgroup);
			Mag.setSrcNum((Integer) Msg.get(0));
			Mag.setDestNum(Mainnum);
			Mag.setPacketnum((Integer) Msg.get(1));
			Mag.setName((String) Msg.get(2));
			Mag.setPascketname((String) Msg.get(3));
			Mag.setContent((String) Msg.get(4));
			this.SendMsgToClient(Mag);
		}
	}

	// 断开连结这个处理线程与客户机的连结, 发生异常,或处理线程退出时调用

	public void disConn() {
		try {
			LogOk = false;
			this.ClientObject.close();
		} catch (Exception ef) {
		}
	}
}
