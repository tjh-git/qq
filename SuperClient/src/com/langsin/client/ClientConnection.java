package com.langsin.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import comm.langsin.model.ToolsCreateMsg;
import comm.langsin.model.ToolsParseMsg;
import comm.langsin.model.UserObject;
import comm.langsin.msg_log_reg.MsgHead;
import comm.langsin.msg_log_reg.MsgLog;
import comm.langsin.msg_log_reg.MsgLogResp;
import comm.langsin.msg_log_reg.MsgReg;
import comm.langsin.msg_log_reg.MsgRegResp;
import comm.langsin.protocol.Protocol;

//�ͻ������ӷ�������
public class ClientConnection extends Thread {
	private static ClientConnection Single;// ����ʵ������
	private Socket Client;// �ͻ�������������Ӷ���
	private InputStream Ins;// ��ͨ����������
	private OutputStream Ous;// ��ͨ���������
	private DataInputStream Datain;// ����������Ի�����������а�װ
	private DataOutputStream Dataou;// ����������Ի�����������а�װ
	private List<ClientMsgListener> Listener = new ArrayList<ClientMsgListener>();

	public ClientConnection() {
	}// ���췽��˽��

	public static ClientConnection getinstance() {// ��������ĵ�������
		if (Single == null) {
			Single = new ClientConnection();
		}
		return Single;
	}

	public void run() {
		while (true) {// ���Ͻ��ܷ�������������Ϣ
			try {
				MsgHead msg = ReadOrdinaryFromServer();
				for (ClientMsgListener ClientListener : Listener) {// �����յ�����Ϣ�ַ���������ȥ����
					ClientListener.FirstMsg(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("��ȡ��������Ϣ�����������˳���������");
				break;
			}
		}
		System.out.println("�ͻ��˽����߳����˳���������");
	}

	public boolean ConnServer() {// ����booleanֵ����Ϊ�Ƿ�����������ӵı�־
		try {
			Client = new Socket(Protocol.ServerIp, Protocol.ServerPort);// ��������������ӵ�Socket����
			System.out.println(Client.getLocalPort() + "����������������ӣ�����������");
			Ins = Client.getInputStream();
			Ous = Client.getOutputStream();
			Datain = new DataInputStream(Ins);
			Dataou = new DataOutputStream(Ous);
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void AddMsgListener(ClientMsgListener L) {// �ͻ�����Ϣ��������
		this.Listener.add(L);
	}

	public int Reg(String Nickname, String Pwd, String UserSign) {// ע���˺�
		int Mainnum = 0;
		MsgReg Mr = new MsgReg();
		Mr.setTotalLength(4 + 1 + 4 + 4 + 10 + 10 + 100);
		Mr.setType(Protocol.common_reg);
		Mr.setSrcNum(0);
		Mr.setDestNum(Protocol.ServerNUMBER);
		Mr.setUsername(Nickname);
		Mr.setUserpwd(Pwd);
		Mr.setUserSign(UserSign);
		try {
			this.SendOrdinaryMsg(Mr);
			MsgHead msg = ReadOrdinaryFromServer();// �ӷ�������������,ͬ����Ϣ
			MsgRegResp Mrr = (MsgRegResp) msg;
			if (Mrr.getState() == 1) {// ��ʾע��ɹ���һ�㲻����ע��ʧ��
				Mainnum = Mrr.getDestNum();
				return Mainnum;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Mainnum;
	}

	public UserObject Log(int Num, String Pwd) {
		UserObject User = null;
		MsgLog Ml = new MsgLog();// ��½������Ϣ
		Ml.setTotalLength(4 + 1 + 4 + 4 + 10);
		Ml.setType(Protocol.common_log);
		Ml.setSrcNum(Num);
		Ml.setDestNum(Protocol.ServerNUMBER);
		Ml.setPasswd(Pwd);
		try {
			this.SendOrdinaryMsg(Ml);
			MsgHead msg = ReadOrdinaryFromServer();
			MsgLogResp Mlr = (MsgLogResp) msg;
			int Msglength = Mlr.getTotalLength();
			if (Msglength == 14) {// ��ʾ�û��û�������������û���¼ʧ��
				
			}  else {// �û���½�ɹ�
				User = new UserObject(Mlr.getDestNum(), null,
						Mlr.getUsername(), Mlr.getUsersign(), Mlr.getUserhead());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return User;
	}

	public void SendOrdinaryMsg(MsgHead msg) throws Exception {// �����û���ͨ��Ϣ��������
		byte[] data = ToolsCreateMsg.PackMsg(msg);// ����Ϣ������
		Dataou.write(data);// ������Ϣ��������
		Dataou.flush();// ˢ��
		System.out.println("�ͻ��˷�����Ϣ��������");
	}

	public MsgHead ReadOrdinaryFromServer() throws Exception {// �ӷ�������ȡһ����Ϣ����
		int TotalLength = Datain.readInt();// ��ȡ��Ϣ�ܳ���
		System.out.println("�ͻ��˶�ȡ����������������Ϣ�ܳ���Ϊ�� " + TotalLength);
		byte[] data = new byte[TotalLength - 4];
		Datain.readFully(data);
		MsgHead msg = ToolsParseMsg.parseMsg(data);
		System.out.println("�ͻ��˽��յ�����Ϣ����Ϊ��" + msg);
		return msg;
	}

	public void CloseClient() {// �ر��������������
		try {
			this.Client.close();
			System.out.println("ȡ�����ӣ���������������Ͽ����ӣ�������");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
