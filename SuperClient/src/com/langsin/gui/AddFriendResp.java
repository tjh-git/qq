package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.langsin.client.ClientConnection;

import comm.langsin.createadd.MsgAddFriendResp;
import comm.langsin.protocol.Protocol;

public class AddFriendResp {// 添加好友应答界面
	private JFrame frame;
	private JLabel label;
	private int Mainnum;
	private String Mainname;
	private ClientConnection Conn;
	private int Friendnum;
	private String Friendname;
	private JLabel nalabel;
	private JLabel nulabel;
	private JLabel name;
	private JLabel num;
	private JTextArea text;
	private JLabel exit;
	private JLabel min;
	private JButton agree;
	private JButton refuse;
	private DefaultMutableTreeNode root;
	private JTree tree;
	private String Sign;

	public AddFriendResp(int NUM, String NAME, ClientConnection CONN,
			int FRIENDNUM, String FRIENDNAME, String Msg, String Sign,
			DefaultMutableTreeNode root, JTree tree) {
		this.Mainnum = NUM;
		this.Mainname = NAME;
		this.Conn = CONN;
		this.Friendnum = FRIENDNUM;
		this.Friendname = FRIENDNAME;
		this.Sign = Sign;
		this.root = root;
		this.tree = tree;
		frame = new JFrame();
		frame.setSize(340, 250);
		frame.setUndecorated(true);
		new Drag(frame).setDragable();
		frame.setLayout(null);
		label = new JLabel(new ImageIcon(
				AddFriendResp.class.getResource("/com/langsin/image/num.jpg")));
		label.setBounds(0, 0, 340, 250);
		nalabel = new JLabel("昵称:");
		nalabel.setFont(new Font("楷体", Font.BOLD, 30));
		nulabel = new JLabel("账号:");
		nulabel.setFont(new Font("楷体", Font.BOLD, 30));
		name = new JLabel(FRIENDNAME);
		name.setFont(new Font("楷体", Font.BOLD, 20));
		name.setForeground(Color.darkGray);
		num = new JLabel(String.valueOf(FRIENDNUM));
		num.setFont(new Font("楷体", Font.BOLD, 20));
		num.setForeground(Color.darkGray);
		text = new JTextArea(Msg);
		text.setBackground(new Color(210, 240, 255));
		text.setFont(new Font("楷体", Font.BOLD, 15));
		text.setLineWrap(true);
		text.setEditable(false);
		exit = new JLabel("X");
		exit.setFont(new Font("楷体", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("楷体", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		agree = new JButton("同意");
		refuse = new JButton("拒绝");
		name.setBounds(130, 20, 100, 30);
		num.setBounds(130, 60, 60, 30);
		nalabel.setBounds(20, 20, 100, 30);
		nulabel.setBounds(20, 60, 100, 30);
		text.setBounds(20, 90, 150, 60);
		min.setBounds(300, 0, 20, 20);
		exit.setBounds(320, 0, 20, 20);
		agree.setBounds(240, 200, 60, 30);
		refuse.setBounds(170, 200, 60, 30);
		label.add(agree);
		label.add(refuse);
		label.add(exit);
		label.add(min);
		label.add(nalabel);
		label.add(text);
		label.add(nulabel);
		label.add(name);
		label.add(num);
		frame.add(label);
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
			} else if (e.getComponent().equals(agree)) {// 同意1丶先到数据库去查找有没有
				DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(
						Friendnum + " " + Friendname + " " + Sign);
				root.add(newnode);
				javax.swing.SwingUtilities.updateComponentTreeUI(tree);
				frame.dispose();
				MsgAddFriendResp Mafr = new MsgAddFriendResp();
				Mafr.setTotalLength(4 + 1 + 4 + 4 + 10 + 1);
				Mafr.setType(Protocol.common_addfriend_resp);
				Mafr.setSrcNum(Mainnum);
				Mafr.setDestNum(Friendnum);
				Mafr.setFriendname(Mainname);
				Mafr.setState((byte) 0);
				try {
					Conn.SendOrdinaryMsg(Mafr);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (e.getComponent().equals(refuse)) {// 拒绝
				frame.dispose();
				MsgAddFriendResp Mafr = new MsgAddFriendResp();
				Mafr.setTotalLength(4 + 1 + 4 + 4 + 10 + 1);
				Mafr.setType(Protocol.common_addfriend_resp);
				Mafr.setSrcNum(Mainnum);
				Mafr.setDestNum(Friendnum);
				Mafr.setFriendname(Mainname);
				Mafr.setState((byte) 1);
				try {
					Conn.SendOrdinaryMsg(Mafr);
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
		new AddFriendResp(1000, "", null, 1000, "rrrr",
				"rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr", null, null, null);
	}
}
