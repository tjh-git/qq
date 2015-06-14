package comm.langsin.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import comm.langsin.protocol.Protocol;

public class MainServer extends Thread {// ����������
	private ServerSocket server;

	MainServer(int port) {// ���캯��

	}

	public void SetServer() {
		try {
			server = new ServerSocket(Protocol.ServerPort);
			System.out.println("�����������ɹ���������");
			while (true) {// ��ѭ�����Ͻ��ܿͻ��˷�����
				Socket Client = server.accept();// ���������Ͻ��ܿͻ��˷�������Ϣ
				ServerThread ST = new ServerThread(Client);
				ST.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {// Thread��run����
		SetServer();
	}

	public static void main(String[] args) {
		MainServer Server = new MainServer(Protocol.ServerPort);
		Server.start();
	}
}
