package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class UserMsg {// 显示用户资料卡
	public static JFrame frame;
	private JLabel background;
	private JLabel name;
	private JLabel number;
	private JLabel sign;
	private JLabel head;
	private JLabel tubiao;

	public UserMsg(String NAME, int NUM, String SIGN, int x, int y) {
		frame = new JFrame();
		background = new JLabel(new ImageIcon(
				UserMsg.class.getResource("/com/langsin/image/msg.png")));
		head = new JLabel(new ImageIcon(
				UserMsg.class.getResource("/com/langsin/image/head2.jpg")));
		tubiao = new JLabel(new ImageIcon(
				UserMsg.class.getResource("/com/langsin/image/tubiao.png")));
		frame.setSize(280, 270);
		frame.setLayout(null);
		frame.setUndecorated(true);
		new Drag(frame).setDragable();
		background.setLayout(null);
		background.setBounds(0, 0, 280, 270);
		name = new JLabel(NAME);
		name.setFont(new Font("楷体", Font.ITALIC, 30));
		number = new JLabel(String.valueOf(NUM));
		number.setForeground(Color.BLUE);
		name.setForeground(Color.BLUE);
		number.setFont(new Font("楷体", Font.ITALIC, 30));
		sign = new JLabel(SIGN);
		sign.setForeground(Color.ORANGE);
		sign.setFont(new Font("楷体", Font.ITALIC, 15));
		head.setBounds(10, 20, 70, 80);
		name.setBounds(100, 25, 150, 30);
		number.setBounds(100, 70, 150, 30);
		sign.setBounds(0, 100, 270, 30);
		tubiao.setBounds(0, 222, 280, 48);
		background.add(name);
		background.add(number);
		background.add(sign);
		background.add(head);
		background.add(tubiao);
		frame.add(background);
		frame.setLocation(x - 280, y);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// new UserMsg("李国迎", 1000, "我是李国迎！！");
	}
}
