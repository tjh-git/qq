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

public class ServerThread extends Thread {// ����������ͻ����߳�
	private JdbcUntil JDBC = new JdbcUntil();
	private Socket ClientObject = null;// �ͻ����̶߳���
	private InputStream Ins = null;// ��ȡ�ͻ��˷�������Ϣ
	private OutputStream Ous = null;// ��ͻ��˷�����Ϣ
	private DataInputStream Datain = null;// ����������Ի�����������а�װ
	private DataOutputStream Dataou = null;// ����������Ի�����������а�װ
	private UserObject User = null;// �û�����
	private boolean LogOk = false;// �û���½�ɹ��ı�־

	public ServerThread(Socket Client) {// ���췽��
		this.ClientObject = Client;
		try {
			this.Ins = Client.getInputStream();// ���ڽ��տͻ��˷��������ݵ�������
			this.Ous = Client.getOutputStream();// ������ͻ��˷�����Ϣ�������
			this.Datain = new DataInputStream(Ins);// ����ͨ�����������װ��
			this.Dataou = new DataOutputStream(Ous);// ����ͨ�����������װ��
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UserObject getUser() {// ȡ������̶߳��������û�����
		return this.User;
	}

	public void run() {
		LogOk = ReadFirstMsg();// ��ȡΪtrue
		System.out.println(LogOk);
		if (LogOk) {
			DealTools.AddClient(User, this);// ���̱߳��浽Map������
		}
		while (LogOk) {// ��½�ɹ���ת����Ϣ�����࣬�ȵ����ƥ��
			try {
				MsgHead Msg = this.ReceiveData();
				DealTools.SendMsg(User, Msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				LogOk = false;
				// ��仰���÷�������ӡ��ע�͵��Ļ��Ͳ��ᱨ Connection refused
			}
		}
		DealTools.RemoveClient(User);// ���û�û������Ϸ�Ƴ�ʱ���߳��Ƴ�
	}

	private boolean ReadFirstMsg() {// ���տͻ��˷����ĵ�һ����Ϣ
		try {
			MsgHead Msg = ReceiveData();
			if (Msg.getType() == Protocol.common_log) {// ��½������Ϣ
				MsgLog Ml = (MsgLog) Msg;
				return CheckLogin(Ml);
			} else if (Msg.getType() == Protocol.common_reg) {// ע��������Ϣ
				MsgReg Mr = (MsgReg) Msg;// ע��������Ϣ
				int Srcnum = JDBC.GetMaxNum();// �����ݿ��л�������˺���Ϊ���ظ�ע����
				JDBC.Reg(Srcnum, Mr.getUsername(), Mr.getUserpwd(),
						Mr.getUserSign()); // ��ע����Ϣ���ϱ��浽���ݿ�
				MsgRegResp Mrr = new MsgRegResp();// ע��Ӧ����Ϣ
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

	private boolean CheckLogin(MsgLog msg) throws IOException {// �����Ƿ��½�ɹ�
		int Srcnum = msg.getSrcNum();
		String Pwd = msg.getPasswd();
		User = JDBC.Log(Srcnum, Pwd);// �����ݿ���ȥ��֤
		MsgLogResp Mlr = new MsgLogResp();
		if (User != null) {
			String Nickname = User.getUsername();// �õ��û��ǳ�
			String UserSign = User.getSignature();// �õ��û�����ǩ��
			byte[] UserHead = User.getUserhead();// �õ��û�ͷ��
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

	private MsgHead ReceiveData() throws Exception {// ���������϶�ȡ���ݿ飬����Ϊ��Ϣ����
		int TotalLength = Datain.readInt();// ��ȡ��Ϣ�ܳ���
		System.out.println("��������ȡ����Ϣ�ܳ���Ϊ:  " + TotalLength);
		byte[] Data = new byte[TotalLength - 4];// ��ȥ��Ϣ�ܳ��ȴ�����byte���飬���ڴ������
		Datain.readFully(Data);// ������readFully
		System.out.println("����������Ϣ���뻺���� Data���������У�������");
		MsgHead Msg = ToolsParseMsg.parseMsg(Data);// ��Ϣ������
		System.out.println("��������������Ϣ����" + Msg);
		return Msg;
	}

	// ���������Լ������б�ķ���
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

	// ����Ⱥ���б�
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

	public boolean SendMsgToClient(MsgHead msg) {// ������Ϣ���ͻ���
		try {
			byte[] data = ToolsCreateMsg.PackMsg(msg);
			this.Dataou.write(data);
			this.Dataou.flush();
			System.out.println("������������Ϣ����" + msg);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("������������Ϣ�������" + msg);
		return false;
	}

	public void GetChatMsg(int Destnum) {// ���������Ϣ
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

	// �Ͽ�������������߳���ͻ���������, �����쳣,�����߳��˳�ʱ����

	public void disConn() {
		try {
			LogOk = false;
			this.ClientObject.close();
		} catch (Exception ef) {
		}
	}
}
