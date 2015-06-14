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

//客户端连接服务器类
public class ClientConnection extends Thread {
	private static ClientConnection Single;// 本类实例对象
	private Socket Client;// 客户端与服务器连接对象
	private InputStream Ins;// 普通输入流对象
	private OutputStream Ous;// 普通输出流对象
	private DataInputStream Datain;// 数据流对象对基本流对象进行包装
	private DataOutputStream Dataou;// 数据流对象对基本流对象进行包装
	private List<ClientMsgListener> Listener = new ArrayList<ClientMsgListener>();

	public ClientConnection() {
	}// 构造方法私有

	public static ClientConnection getinstance() {// 创建本类的单例对象
		if (Single == null) {
			Single = new ClientConnection();
		}
		return Single;
	}

	public void run() {
		while (true) {// 不断接受服务器发来的消息
			try {
				MsgHead msg = ReadOrdinaryFromServer();
				for (ClientMsgListener ClientListener : Listener) {// 将接收到的消息分发给监听器去处理
					ClientListener.FirstMsg(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("读取到错误消息！！！！！退出！！！！");
				break;
			}
		}
		System.out.println("客户端接受线程已退出！！！！");
	}

	public boolean ConnServer() {// 返回boolean值，作为是否与服务器连接的标志
		try {
			Client = new Socket(Protocol.ServerIp, Protocol.ServerPort);// 创建与服务器连接的Socket对象
			System.out.println(Client.getLocalPort() + "与服务器建立了连接！！！！！！");
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

	public void AddMsgListener(ClientMsgListener L) {// 客户端消息监听对象
		this.Listener.add(L);
	}

	public int Reg(String Nickname, String Pwd, String UserSign) {// 注册账号
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
			MsgHead msg = ReadOrdinaryFromServer();// 从服务器接收数据,同步消息
			MsgRegResp Mrr = (MsgRegResp) msg;
			if (Mrr.getState() == 1) {// 表示注册成功，一般不存在注册失败
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
		MsgLog Ml = new MsgLog();// 登陆请求消息
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
			if (Msglength == 14) {// 表示用户用户名和密码错误，用户登录失败
				
			}  else {// 用户登陆成功
				User = new UserObject(Mlr.getDestNum(), null,
						Mlr.getUsername(), Mlr.getUsersign(), Mlr.getUserhead());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return User;
	}

	public void SendOrdinaryMsg(MsgHead msg) throws Exception {// 发送用户普通消息给服务器
		byte[] data = ToolsCreateMsg.PackMsg(msg);// 将消息对象打包
		Dataou.write(data);// 发送消息给服务器
		Dataou.flush();// 刷新
		System.out.println("客户端发出消息！！！！");
	}

	public MsgHead ReadOrdinaryFromServer() throws Exception {// 从服务器读取一般消息对象
		int TotalLength = Datain.readInt();// 读取消息总长度
		System.out.println("客户端读取到服务器发来的消息总长度为： " + TotalLength);
		byte[] data = new byte[TotalLength - 4];
		Datain.readFully(data);
		MsgHead msg = ToolsParseMsg.parseMsg(data);
		System.out.println("客户端接收到的消息对象为：" + msg);
		return msg;
	}

	public void CloseClient() {// 关闭与服务器的连接
		try {
			this.Client.close();
			System.out.println("取消连接！！！！与服务器断开连接！！！！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
