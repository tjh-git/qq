package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultMutableTreeNode;

import com.langsin.client.ClientConnection;
import com.langsin.client.PacketTree;
import comm.langsin.createadd.MsgAddGroupResp;
import comm.langsin.protocol.Protocol;

public class AddGroupResp {
	private JFrame frame = null;
	private JLabel namelabel;
	private JLabel packetnamelabel;
	private int NUM;
	private String NAME;
	private int DESTNUM;
	private String DESTNAME;
	private int PACKETNUM;
	private JTextArea text;
	private JButton agree;
	private JButton refuse;
	private JLabel exit;
	private JLabel min;
	private ClientConnection Conn;

	public AddGroupResp(int Mainnum, String Mainname, int Destnum,
			String Destname, int Packetnum, String Packetname, String Content,
			ClientConnection CONN) {
		this.NUM = Mainnum;
		this.NAME = Mainname;
		this.DESTNUM = Destnum;
		this.PACKETNUM = Packetnum;
		this.DESTNAME = Destname;
		this.Conn = CONN;
		frame = new JFrame();
		frame.setUndecorated(true);
		new Drag(frame).setDragable();
		frame.setSize(300, 240);
		namelabel = new JLabel(Destname + "请求加入:");
		namelabel.setForeground(Color.BLUE);
		namelabel.setFont(new Font("楷体", Font.BOLD, 20));
		packetnamelabel = new JLabel(Packetname);
		packetnamelabel.setForeground(Color.GREEN);
		packetnamelabel.setFont(new Font("楷体", Font.BOLD, 20));
		text = new JTextArea(80, 50);
		agree = new JButton("同意");
		refuse = new JButton("拒绝");
		text.setText(Content);
		text.setFont(new Font("楷体", Font.BOLD, 15));
		text.setBackground(new Color(210, 240, 255));
		text.setEditable(false);
		frame.setLayout(null);
		exit = new JLabel("X");
		exit.setFont(new Font("楷体", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("楷体", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		namelabel.setBounds(20, 20, 200, 30);
		packetnamelabel.setBounds(20, 60, 200, 30);
		agree.setBounds(240, 200, 60, 30);
		refuse.setBounds(180, 200, 60, 30);
		text.setBounds(20, 100, 260, 60);
		exit.setBounds(280, 0, 20, 20);
		min.setBounds(260, 0, 20, 20);
		frame.add(namelabel);
		frame.add(exit);
		frame.add(min);
		frame.add(packetnamelabel);
		frame.add(agree);
		frame.add(refuse);
		frame.add(text);
		frame.setVisible(true);
		exit.addMouseListener(new Event());
		min.addMouseListener(new Event());
		agree.addMouseListener(new Event());
		refuse.addMouseListener(new Event());
	}

	class Event extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				frame.dispose();
			} else if (e.getComponent().equals(min)) {
				frame.setExtendedState(JFrame.ICONIFIED);
			} else if (e.getComponent().equals(agree)) {// 同意添加为好友
				if (PacketTree.CHAT.containsKey(PACKETNUM)) {
					PacketTree.CHAT.get(PACKETNUM).root
							.add(new DefaultMutableTreeNode(DESTNUM + " "
									+ DESTNAME));
					javax.swing.SwingUtilities
							.updateComponentTreeUI(ChatGroup.friend);// 刷新界面
				}
				MsgAddGroupResp Magr = new MsgAddGroupResp();
				Magr.setTotalLength(4 + 1 + 4 + 4 + 4 + 10 + 10 + 1);
				Magr.setType(Protocol.common_addgroupresp);
				Magr.setSrcNum(NUM);
				Magr.setDestNum(DESTNUM);
				Magr.setPacketnum(PACKETNUM);
				Magr.setState((byte) 0);
				Magr.setName(NAME);
				Magr.setPacketname(packetnamelabel.getText().trim());
				frame.dispose();
				try {
					Conn.SendOrdinaryMsg(Magr);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (e.getComponent().equals(refuse)) {// 拒绝添加为好友
				MsgAddGroupResp Magr = new MsgAddGroupResp();
				Magr.setTotalLength(4 + 1 + 4 + 4 + 4 + 10 + 10 + 1);
				Magr.setType(Protocol.common_addgroupresp);
				Magr.setSrcNum(NUM);
				Magr.setState((byte) 1);
				Magr.setDestNum(DESTNUM);
				Magr.setName(NAME);
				Magr.setPacketnum(PACKETNUM);
				Magr.setPacketname(packetnamelabel.getText());
				frame.dispose();
				try {
					Conn.SendOrdinaryMsg(Magr);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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

	public static void main(String[] args) {
		new AddGroupResp(1000, "", 1234, "uuuuu", 5555, "rrrrrrrrrrr",
				"tttttttttttttttt", null);
	}
}
