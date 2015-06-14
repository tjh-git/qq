package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.langsin.client.ClientConnection;
import comm.langsin.createadd.MsgAddGroup;
import comm.langsin.protocol.Protocol;

//��Ӻ���������Ϣ�Ի���
public class AddGroup {
	private JFrame frame;
	private JLabel label;
	private JTextArea text;
	private JLabel exit;
	private JLabel min;
	private JButton button;
	private int Mainnum;
	private String Mainname;
	private int Packetnum;
	private String Pascketname;
	private ClientConnection conn;
	private int Groupmain;
	private String SIGN;
	public AddGroup(int OOnum, String Name, int Packetnum,String Packetname,
			ClientConnection conn,int Groupmain) {
		this.Mainnum = OOnum;
		this.Mainname = Name;
		this.Packetnum = Packetnum;
		this.Groupmain=Groupmain;
		this.Pascketname =Packetname;
		this.conn = conn;
		frame = new JFrame();
		frame.setSize(300, 200);
		label = new JLabel("�����븽����Ϣ:");
		label.setFont(new Font("����", Font.PLAIN, 15));
		label.setForeground(new Color(0, 0, 255));
		text = new JTextArea();
		button = new JButton("����");
		text.setBackground(new Color(210, 240, 255));
		frame.setLayout(null);
		frame.setUndecorated(true);
		new Drag(frame).setDragable();
		exit = new JLabel("X");
		exit.setFont(new Font("����", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("����", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		label.setBounds(100, 30, 150, 30);
		text.setBounds(20, 70, 260, 80);
		exit.setBounds(280, 0, 20, 20);
		min.setBounds(260, 0, 20, 20);
		button.setBounds(220, 160, 60, 30);
		text.setLineWrap(true);
		frame.add(label);
		frame.add(button);
		frame.add(text);
		frame.add(exit);
		frame.add(min);
		frame.setVisible(true);
		exit.addMouseListener(new Event());
		min.addMouseListener(new Event());
		button.addMouseListener(new Event());
	}

	class Event extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				frame.dispose();
			} else if (e.getComponent().equals(min)) {
				frame.setExtendedState(JFrame.ICONIFIED);
			} else if (e.getComponent().equals(button)) {// ���������Ϣ
				String Content = text.getText().trim();
			MsgAddGroup Mag = new MsgAddGroup();
			Mag.setTotalLength(4 + 1 + 4 + 4 + 4 + 10 + 10 + 100);
			Mag.setType(Protocol.common_addgroup);
			Mag.setSrcNum(Mainnum);
			Mag.setDestNum(Groupmain);
			Mag.setPacketnum(Packetnum);
			Mag.setName(Mainname);
			Mag.setPascketname(Pascketname);
			Mag.setContent(Content);
			Mag.setSign(SIGN);
			FindUserPacket.frame.dispose();
				frame.dispose();
				try {
					conn.SendOrdinaryMsg(Mag);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				exit.setForeground(new Color(255, 0, 0));
				exit.setFont(new Font("����", Font.PLAIN, 27));
			} else if (e.getComponent().equals(min)) {
				min.setForeground(new Color(0, 255, 0));
				min.setFont(new Font("����", Font.PLAIN, 37));
			}
		}

		public void mouseExited(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				exit.setForeground(new Color(0, 0, 0));
				exit.setFont(new Font("����", Font.PLAIN, 20));
			} else if (e.getComponent().equals(min)) {
				min.setForeground(new Color(0, 0, 0));
				min.setFont(new Font("����", Font.PLAIN, 30));
			}
		}
	}

	public static void main(String[] args) {
		new AddFriend(1000, " ", 1000, null);
	}
}
