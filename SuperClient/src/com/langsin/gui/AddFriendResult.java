package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class AddFriendResult {// 添加好友应答时间
	private JFrame result = null;
	private JLabel label = null;
	private JLabel name = null;
	private JLabel msg = null;
	private JLabel min;
	private JLabel exit;

	public AddFriendResult(String Name, int NUMBER, byte State) {
		result = new JFrame();
		result.setUndecorated(true);
		new Drag(result).setDragable();
		label = new JLabel(
				new ImageIcon(
						AddFriendResult.class
								.getResource("/com/langsin/image/num.jpg")));
		result.setSize(340, 250);
		result.setLayout(null);
		label.setBounds(0, 0, 340, 250);
		result.add(label);
		name = new JLabel(Name);
		name.setFont(new Font("楷体", Font.BOLD, 30));
		msg = new JLabel();
		if (State == 0) {
			msg.setText("同意添加你为好友！！");
		} else {
			msg.setText("拒绝添加你为好友！！");
		}
		msg.setFont(new Font("楷体", Font.BOLD, 20));
		msg.setForeground(Color.BLUE);
		label.setLayout(null);
		name.setBounds(30, 30, 120, 30);
		msg.setBounds(30, 70, 220, 30);
		exit = new JLabel("X");
		exit.setFont(new Font("楷体", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("楷体", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		exit.setBounds(320, 0, 20, 20);
		min.setBounds(300, 0, 20, 20);
		label.add(exit);
		label.add(min);
		label.add(msg);
		label.add(name);
		result.setVisible(true);
		min.addMouseListener(new Event());
		exit.addMouseListener(new Event());
	}

	class Event extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				result.dispose();
			} else if (e.getComponent().equals(min)) {
				result.setExtendedState(JFrame.ICONIFIED);
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
