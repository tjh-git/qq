package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.langsin.client.ClientConnection;
import comm.langsin.find.MsgFindPacket;
import comm.langsin.find.MsgFindUser;
import comm.langsin.protocol.Protocol;

//查找用户和群组
public class FindUserPacket {
	public static JFrame frame;
	private JTabbedPane tab;
	private JPanel friend;
	private JPanel packet;
	private JLabel label1;
	private JLabel label2;
	private JTextField usernum;
	private JTextField packetnum;
	private JButton finduser;
	private JButton findpacket;
	private JLabel headlabel;
	private JLabel namelabel;
	private JLabel numberlabel;
	private JLabel headpacket;
	private JLabel numpacket;
	private JLabel namepacket;
	private JLabel exit;
	private JLabel min;
	private int MainNum;
	private String MainName;
	private ClientConnection conn;
	private int GroupMain;
	private JLabel la1;
	private JLabel la2;

	public FindUserPacket(int OOnum, String Name, ClientConnection conn) {
		this.MainNum = OOnum;
		this.MainName = Name;
		this.conn = conn;
		frame = new JFrame();
		tab = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		friend = new JPanel();
		packet = new JPanel();
		label1 = new JLabel("请输入用户账号:");
		label2 = new JLabel("请输入群号码:");
		usernum = new JTextField();
		packetnum = new JTextField();
		finduser = new JButton("查找");
		findpacket = new JButton("查找");
		headlabel = new JLabel();// 存放用户头像
		numberlabel = new JLabel();// 存放用户号码
		namelabel = new JLabel();// 存放用户昵称
		exit = new JLabel("X");
		exit.setFont(new Font("楷体", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("楷体", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		friend.setLayout(null);
		label1.setBounds(10, 20, 100, 30);
		usernum.setBounds(110, 20, 150, 30);
		la1 = new JLabel("");
		la2 = new JLabel("");
		finduser.setBounds(270, 20, 60, 30);
		headlabel.setBounds(20, 60, 60, 60);
		la1.setBounds(340, 20, 60, 30);
		la1.setFont(new Font("楷体", Font.BOLD, 10));
		numberlabel.setBounds(80, 60, 40, 20);
		namelabel.setBounds(80, 80, 70, 20);
		exit.setBounds(380, 0, 20, 20);
		min.setBounds(360, 0, 20, 20);
		friend.add(label1);
		friend.add(usernum);
		friend.add(finduser);
		friend.add(headlabel);
		friend.add(numberlabel);
		friend.add(namelabel);
		packet.setLayout(null);
		label2.setBounds(10, 20, 100, 30);
		packetnum.setBounds(110, 20, 150, 30);
		findpacket.setBounds(270, 20, 60, 30);
		la2 = new JLabel("");
		la2.setBounds(340, 20, 60, 30);
		la2.setFont(new Font("楷体", Font.BOLD, 10));
		headpacket = new JLabel();
		numpacket = new JLabel();
		namepacket = new JLabel();
		headpacket.setBounds(20, 60, 60, 60);
		numpacket.setBounds(80, 60, 40, 20);
		namepacket.setBounds(80, 80, 70, 20);
		packet.add(label2);
		packet.add(packetnum);
		packet.add(findpacket);
		packet.add(headpacket);
		packet.add(numpacket);
		packet.add(namepacket);
		friend.add(la1);
		packet.add(la2);
		tab.addTab("查找好友", friend);
		tab.addTab("查找群组", packet);
		frame.add(exit);
		frame.add(min);
		frame.setSize(400, 300);
		frame.setLayout(null);
		tab.setBounds(0, 30, 400, 300);
		frame.add(tab);
		frame.setSize(400, 330);
		frame.setUndecorated(true);
		new Drag(frame).setDragable();
		frame.setVisible(true);
		finduser.addMouseListener(new Event());
		findpacket.addMouseListener(new Event());
		headlabel.addMouseListener(new Event());
		exit.addMouseListener(new Event());
		min.addMouseListener(new Event());
		headpacket.addMouseListener(new Event());
	}

	// 显示查找用户结果
	public void showmsguser(ImageIcon head, int Usernum, String Username) {
		headlabel.setIcon(head);
		numberlabel.setText(String.valueOf(Usernum));
		namelabel.setText(Username);
		friend.updateUI();
	}

	// 显示查找群组结果
	public void showmsgpacket(ImageIcon head, int Packetnum, String Packetname,
			int GroupMain) {
		this.GroupMain = GroupMain;
		headpacket.setIcon(head);
		numpacket.setText(String.valueOf(Packetnum));
		namepacket.setText(Packetname);
		packet.updateUI();
	}

	// 事件类
	class Event extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent().equals(min)) {
				frame.setExtendedState(JFrame.ICONIFIED);
			} else if (e.getComponent().equals(exit)) {
				frame.dispose();
			} else if (e.getComponent().equals(finduser)) {// 查找好友
				String UserNum = usernum.getText();
				if (UserNum.getBytes().length == 4) {
					MsgFindUser Mfu = new MsgFindUser();
					Mfu.setTotalLength(4 + 1 + 4 + 4 + 4);
					Mfu.setType(Protocol.common_finduser);
					Mfu.setSrcNum(MainNum);
					Mfu.setDestNum(Protocol.ServerNUMBER);
					Mfu.setFriendnum(Integer.parseInt(UserNum));
					try {
						conn.SendOrdinaryMsg(Mfu);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					la1.setForeground(new Color(255, 0, 0));
					la1.setText("NO！！");
				}
			} else if (e.getComponent().equals(findpacket)) {// 查找群组
				String PacketNum = packetnum.getText();
				if (PacketNum.getBytes().length == 5) {
					MsgFindPacket Mfu = new MsgFindPacket();
					Mfu.setTotalLength(4 + 1 + 4 + 4 + 4);
					Mfu.setType(Protocol.common_findpacket);
					Mfu.setSrcNum(MainNum);
					Mfu.setDestNum(Protocol.ServerNUMBER);
					Mfu.setPacketnum(Integer.parseInt(PacketNum));
					try {
						conn.SendOrdinaryMsg(Mfu);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					la2.setForeground(new Color(255, 0, 0));
					la2.setText("NO！！");
				}
			} else if (e.getComponent().equals(headlabel)) {// 添加好友
				int Friendnum = Integer.parseInt(numberlabel.getText().trim());
				new AddFriend(MainNum, MainName, Friendnum, conn);
			} else if (e.getComponent().equals(headpacket)) {// 添加群组
				new AddGroup(MainNum, MainName, Integer.parseInt(numpacket
						.getText()), namepacket.getText(), conn, GroupMain);
			}
		}

		public void mouseEntered(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				exit.setForeground(new Color(255, 0, 0));
				exit.setFont(new Font("楷体", Font.PLAIN, 27));
			} else if (e.getComponent().equals(min)) {
				min.setForeground(new Color(0, 255, 0));
				min.setFont(new Font("楷体", Font.PLAIN, 37));
			}
		}

		public void mouseExited(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				exit.setForeground(new Color(0, 0, 0));
				exit.setFont(new Font("楷体", Font.PLAIN, 20));
			} else if (e.getComponent().equals(min)) {
				min.setForeground(new Color(0, 0, 0));
				min.setFont(new Font("楷体", Font.PLAIN, 30));
			}
		}
	}
}
