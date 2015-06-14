package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
//创建群组
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.langsin.client.ClientConnection;
import comm.langsin.createadd.MsgCreatePacket;
import comm.langsin.protocol.Protocol;

public class CreatePacket {
	private JFrame frame;
	private JLabel label;
	private JLabel desc;
	private JTextField text;
	private JButton button;
	private JLabel exit;
	private JLabel min;
	private JTextArea area;
	private int Mainnum;
	private ClientConnection conn;

	public CreatePacket(int OOnum, String Name, ClientConnection conn) {
		this.Mainnum = OOnum;
		this.conn = conn;
		frame = new JFrame();
		label = new JLabel("请输入群昵称:");
		desc = new JLabel("群描述:");
		label.setFont(new Font("楷体", Font.PLAIN, 20));
		label.setForeground(new Color(0, 0, 255));
		desc.setFont(new Font("楷体", Font.PLAIN, 20));
		desc.setForeground(new Color(0, 0, 255));
		text = new JTextField();
		area = new JTextArea();
		area.setBackground(new Color(210, 240, 255));
		button = new JButton("创建");
		exit = new JLabel("X");
		exit.setFont(new Font("楷体", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("楷体", Font.PLAIN, 30));
		frame.setLayout(null);
		frame.setUndecorated(true);
		new Drag(frame).setDragable();
		frame.setSize(300, 300);
		text.setBounds(20, 50, 160, 30);
		area.setBounds(20, 110, 160, 50);
		label.setBounds(20, 20, 170, 30);
		desc.setBounds(20, 80, 160, 30);
		exit.setBounds(280, 0, 20, 20);
		min.setBounds(260, 0, 20, 20);
		button.setBounds(120, 180, 60, 30);
		frame.add(label);
		frame.add(exit);
		frame.add(desc);
		frame.add(min);
		frame.add(text);
		frame.add(area);
		frame.add(button);
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
			} else if (e.getComponent().equals(button)) {// 点击创建
				String Packname = text.getText().trim();
				String packdesc = area.getText().trim();
				frame.dispose();
				if ((Packname.getBytes().length <= 10)
						&& (packdesc.getBytes().length < 100)) {
					MsgCreatePacket Mcp = new MsgCreatePacket();
					Mcp.setTotalLength(4 + 1 + 4 + 4 + 10 + 100);
					Mcp.setType(Protocol.common_createpacket);
					Mcp.setSrcNum(Mainnum);
					Mcp.setDestNum(Protocol.ServerNUMBER);
					Mcp.setPacketname(Packname);
					Mcp.setPacketsign(packdesc);
					try {
						conn.SendOrdinaryMsg(Mcp);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
		new CreatePacket(1000, "", null);
	}
}
