package comm.langsin.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import comm.langsin.protocol.Protocol;

public class MainServer extends Thread {// 服务器主类
	private ServerSocket server;

	MainServer(int port) {// 构造函数

	}

	public void SetServer() {
		try {
			server = new ServerSocket(Protocol.ServerPort);
			System.out.println("服务器创建成功！！！！");
			while (true) {// 死循环不断接受客户端发来的
				Socket Client = server.accept();// 阻塞，不断接受客户端发来的消息
				ServerThread ST = new ServerThread(Client);
				ST.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {// Thread类run方法
		SetServer();
	}

	public static void main(String[] args) {
		MainServer Server = new MainServer(Protocol.ServerPort);
		Server.start();
	}
}
