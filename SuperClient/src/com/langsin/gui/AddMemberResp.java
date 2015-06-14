package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.langsin.client.ClientConnection;
import com.langsin.remove.MsgAddMemberResp;

import comm.langsin.protocol.Protocol;

public class AddMemberResp {
	private JFrame frame = null;
	private JButton agree = null;
	private JButton refuse = null;
	private JLabel name = null;
	private JLabel packetname = null;
	private int Mainnum;
	private String Mainname;
	private int Friendnum;
	private String Packetname;
	private int Packetnum;
	private ClientConnection Conn;
	private JLabel exit;
	private JLabel min;

	public AddMemberResp(int Mainnum, String Mainname, int Friendnum,
			String Name, int Packetnum, String Packetname, ClientConnection conn) {
		this.Mainnum = Mainnum;
		this.Mainname = Mainname;
		this.Friendnum = Friendnum;
		this.Packetname = Packetname;
		this.Packetnum = Packetnum;
		this.Conn = conn;
		frame = new JFrame();
		frame.setUndecorated(true);
		new Drag(frame).setDragable();
		exit = new JLabel("X");
		exit.setFont(new Font("楷体", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("楷体", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		agree = new JButton("同意");
		refuse = new JButton("拒绝");
		name = new JLabel(Name + "请求你加入该群:");
		packetname = new JLabel(Packetname);
		frame.setSize(300, 200);
		frame.setLayout(null);
		name.setBounds(30, 30, 300, 30);
		name.setFont(new Font("楷体", Font.BOLD, 20));
		name.setForeground(Color.blue);
		packetname.setBounds(30, 60, 300, 30);
		packetname.setFont(new Font("楷体", Font.BOLD, 20));
		packetname.setForeground(Color.BLUE);
		agree.setBounds(100, 100, 60, 30);
		refuse.setBounds(170, 100, 60, 30);
		exit.setBounds(280, 0, 20, 20);
		min.setBounds(260, 0, 20, 20);
		frame.add(exit);
		frame.add(min);
		frame.add(agree);
		frame.add(refuse);
		frame.add(name);
		frame.add(packetname);
		frame.setVisible(true);
		agree.addMouseListener(new Event());
		refuse.addMouseListener(new Event());

	}

	class Event extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent().equals(agree)) {
				MsgAddMemberResp Mamr = new MsgAddMemberResp();
				Mamr.setTotalLength(4 + 1 + 4 + 4 + 4 + 1 + 10 + 10);
				Mamr.setType(Protocol.common_addmemberresp);
				Mamr.setSrcNum(Mainnum);
				Mamr.setDestNum(Friendnum);
				Mamr.setPacketnum(Packetnum);
				Mamr.setState((byte) 0);
				Mamr.setName(Mainname);
				Mamr.setPacketname(Packetname);
				try {
					Conn.SendOrdinaryMsg(Mamr);
					frame.dispose();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (e.getComponent().equals(refuse)) {
				MsgAddMemberResp Mamr = new MsgAddMemberResp();
				Mamr.setTotalLength(4 + 1 + 4 + 4 + 4 + 1 + 10 + 10);
				Mamr.setType(Protocol.common_addmemberresp);
				Mamr.setSrcNum(Mainnum);
				Mamr.setDestNum(Friendnum);
				Mamr.setPacketnum(Packetnum);
				Mamr.setState((byte) 1);
				Mamr.setName(Mainname);
				Mamr.setPacketname(Packetname);
				try {
					Conn.SendOrdinaryMsg(Mamr);
					frame.dispose();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
