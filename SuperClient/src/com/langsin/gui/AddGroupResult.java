package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class AddGroupResult {// 添加群组结果
	private JFrame frame = null;
	private JLabel name;
	private JLabel packname;
	private JLabel exit;
	private JLabel min;

	public AddGroupResult(String NAME, String PACKETNAME, byte State) {
		frame = new JFrame();
		frame.setSize(300, 220);
		frame.setUndecorated(true);
		new Drag(frame).setDragable();
		frame.setLayout(null);
		if (State == 0) {
			name = new JLabel(NAME + "同意你加入该群:");
		} else {
			name = new JLabel(NAME + "拒绝你加入该群:");
		}
		name.setFont(new Font("楷体", Font.BOLD, 20));
		name.setForeground(Color.BLUE);
		packname = new JLabel(PACKETNAME);
		packname.setFont(new Font("楷体", Font.BOLD, 20));
		packname.setForeground(Color.BLUE);
		name.setBounds(20, 20, 300, 30);
		packname.setBounds(30, 60, 100, 30);
		exit = new JLabel("X");
		exit.setFont(new Font("楷体", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("楷体", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		exit.setBounds(280, 0, 20, 20);
		min.setBounds(260, 0, 20, 20);
		frame.add(exit);
		frame.add(min);
		frame.add(name);
		frame.add(packname);
		frame.setVisible(true);
		exit.addMouseListener(new Event());
		min.addMouseListener(new Event());
	}

	class Event extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				frame.dispose();
			} else if (e.getComponent().equals(min)) {
				frame.setExtendedState(JFrame.ICONIFIED);
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
		new AddGroupResult("李国迎", "浪曦云团", (byte) 0);
	}
}
