package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.langsin.client.ClientConnection;

//注册界面
public class Regeister {
	private ClientConnection Conn = null;
	private JFrame Reg;
	private JLabel namelabel;
	private JLabel background;
	private JLabel pwd1label;
	private JLabel pwd2label;
	private JLabel sexlabel;
	private JLabel signature;
	private JLabel button;
	private JTextField name;// 输入内容文本框
	private JPasswordField pwd1;
	private JPasswordField pwd2;
	private ButtonGroup group;
	private JRadioButton man;
	private JRadioButton woman;
	private JTextArea sign;
	private JLabel min;
	private JLabel exit;
	private JLabel err1;
	private JLabel err2;
	private JLabel err3;
	private JLabel err4;

	public static void main(String[] args) {
		new Regeister();
	}

	public Regeister() {
		Conn = new ClientConnection();
		Reg = new JFrame();
		background = new JLabel(new ImageIcon(
				Regeister.class.getResource("/com/langsin/image/regbc.jpg")));
		namelabel = new JLabel("请输入昵称:");
		namelabel.setFont(new Font("楷体", Font.PLAIN, 17));
		namelabel.setForeground(new Color(0, 0, 255));
		pwd1label = new JLabel("请输入密码:");
		pwd1label.setFont(new Font("楷体", Font.PLAIN, 17));
		pwd1label.setForeground(new Color(0, 0, 255));
		pwd2label = new JLabel("请确认密码");
		pwd2label.setFont(new Font("楷体", Font.PLAIN, 17));
		pwd2label.setForeground(new Color(0, 0, 255));
		sexlabel = new JLabel("请选择性别:");
		sexlabel.setFont(new Font("楷体", Font.PLAIN, 17));
		sexlabel.setForeground(new Color(0, 0, 255));
		signature = new JLabel("个性签名:");
		signature.setFont(new Font("楷体", Font.PLAIN, 17));
		signature.setForeground(new Color(0, 0, 255));
		err1 = new JLabel();
		err2 = new JLabel();
		err3 = new JLabel();
		err4 = new JLabel();
		sign = new JTextArea();
		sign.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 255)));
		sign.setLineWrap(true);
		button = new JLabel("立即注册");
		button.setFont(new Font("楷体", Font.PLAIN, 25));
		button.setForeground(new Color(0, 0, 255));
		name = new JTextField();
		name.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 255)));
		pwd1 = new JPasswordField();
		pwd1.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 255)));
		pwd2 = new JPasswordField();
		pwd2.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 255)));
		man = new JRadioButton("男");
		woman = new JRadioButton("女");
		woman.setSelected(true);
		group = new ButtonGroup();
		group.add(man);
		group.add(woman);
		exit = new JLabel("X");
		exit.setFont(new Font("楷体", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("楷体", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		Reg.setSize(400, 450);
		Reg.setLayout(null);
		Reg.setUndecorated(true);
		background.setBounds(0, 0, 400, 450);
		Reg.add(background);
		background.setLayout(null);// 让背景实现绝对定位
		namelabel.setBounds(40, 70, 100, 30);
		name.setBounds(140, 70, 160, 30);
		err1.setBounds(310, 70, 100, 30);
		pwd1label.setBounds(40, 120, 100, 30);
		pwd1.setBounds(140, 120, 160, 30);
		err2.setBounds(310, 120, 100, 30);
		pwd2label.setBounds(40, 170, 100, 30);
		pwd2.setBounds(140, 170, 160, 30);
		err3.setBounds(310, 170, 100, 30);
		sexlabel.setBounds(40, 220, 100, 30);
		man.setBounds(160, 225, 40, 20);
		man.setForeground(new Color(0, 150, 0));
		woman.setBounds(230, 225, 40, 20);
		woman.setForeground(new Color(200, 0, 0));
		signature.setBounds(40, 270, 100, 30);
		sign.setBounds(140, 270, 160, 60);
		err4.setBounds(310, 270, 100, 30);
		button.setBounds(150, 350, 100, 40);
		exit.setBounds(380, 0, 20, 20);
		min.setBounds(360, 0, 20, 20);
		background.add(namelabel);
		background.add(name);
		background.add(err1);
		background.add(err2);
		background.add(err3);
		background.add(err4);
		background.add(pwd1label);
		background.add(pwd1);
		background.add(pwd2label);
		background.add(pwd2);
		background.add(sexlabel);
		background.add(man);
		background.add(woman);
		background.add(signature);
		background.add(sign);
		background.add(button);
		background.add(exit);
		background.add(min);
		new Drag(Reg).setDragable();
		exit.addMouseListener(new Event());
		min.addMouseListener(new Event());
		button.addMouseListener(new Event());
		name.addMouseListener(new Event());
		pwd1.addMouseListener(new Event());
		pwd2.addMouseListener(new Event());
		sign.addMouseListener(new Event());
		Reg.setVisible(true);
	}

	// 移动类

	// 事件类
	class Event extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				Reg.dispose();
			} else if (e.getComponent().equals(min)) {
				Reg.setExtendedState(JFrame.ICONIFIED);
			}
		}

		public void mousePressed(MouseEvent e) {
			int length1 = name.getText().getBytes().length;
			int length2 = pwd1.getPassword().length;
			String pw1 = String.valueOf(pwd1.getPassword());
			String pw2 = String.valueOf(pwd2.getPassword());
			int length3 = sign.getText().getBytes().length;
			if (e.getComponent().equals(pwd1)) {
				if (length1 == 0) {
					err1.setForeground(new Color(255, 0, 0));
					err1.setText("*用户名不能为空！");
				} else if (length1 > 10) {
					err1.setForeground(new Color(255, 0, 0));
					err1.setText("*用户名过长！");
				} else {
					err1.setText("");
				}
			} else if (e.getComponent().equals(pwd2)) {
				if (length1 == 0) {
					err1.setForeground(new Color(255, 0, 0));
					err1.setText("*用户名不能为空！");
				} else if (length1 > 10) {
					err1.setForeground(new Color(255, 0, 0));
					err1.setText("*用户名过长！");
				} else if (length2 == 0) {
					err2.setText("*密码不能为空！");
					err2.setForeground(new Color(255, 0, 0));
				} else if (length2 > 10) {
					err2.setText("*密码过长！");
					err2.setForeground(new Color(255, 0, 0));
				} else if (!(pw1.equals(pw2))) {
					err3.setForeground(new Color(255, 0, 0));
					err3.setText("密码不一致！");
				} else {
					err1.setText("");
					err2.setText("");
					err3.setText("");
				}
			} else if (e.getComponent().equals(sign)) {
				if (length1 == 0) {
					err1.setForeground(new Color(255, 0, 0));
					err1.setText("*用户名不能为空！");
				} else if (length1 > 10) {
					err1.setForeground(new Color(255, 0, 0));
					err1.setText("*用户名过长！");
				} else if (length2 == 0) {
					err2.setText("*密码不能为空！");
					err2.setForeground(new Color(255, 0, 0));
				} else if (length2 > 10) {
					err2.setText("*密码过长！");
					err2.setForeground(new Color(255, 0, 0));
				} else if (!(pw1.equals(pw2))) {
					err3.setForeground(new Color(255, 0, 0));
					err3.setText("密码不一致！");
				} else {
					err1.setText("");
					err2.setText("");
					err3.setText("");
				}
			} else if (e.getComponent().equals(exit)) {
				Reg.dispose();
			} else if (e.getComponent().equals(min)) {
				Reg.setExtendedState(JFrame.ICONIFIED);
			} else if (e.getComponent().equals(button)) {
				if (length1 == 0) {
					err1.setForeground(new Color(255, 0, 0));
					err1.setText("*用户名不能为空！");
				} else if (length1 > 10) {
					err1.setForeground(new Color(255, 0, 0));
					err1.setText("*用户名过长！");
				} else if (length2 == 0) {
					err2.setText("*密码不能为空！");
					err2.setForeground(new Color(255, 0, 0));
				} else if (length2 > 10) {
					err2.setText("*密码过长！");
					err2.setForeground(new Color(255, 0, 0));
				} else if (!(pw1.equals(pw2))) {
					err3.setForeground(new Color(255, 0, 0));
					err3.setText("密码不一致！");
				} else if (length3 > 100) {
					err4.setForeground(new Color(255, 0, 0));
					err4.setText("*个性签名过长！");
				} else if (length3 == 0) {
					err4.setForeground(new Color(255, 0, 0));
					err4.setText("*个性签名不能为空！");
				} else {
					err1.setText("");
					err2.setText("");
					err3.setText("");
					err4.setText("");
					String nickname = name.getText().trim();
					String pw = String.valueOf(pw1).trim();
					String sig = sign.getText().trim();
					Reg.dispose();
					if (Conn.ConnServer()) {
						int NUM = Conn.Reg(nickname, pw, sig);
						if (NUM != -1) {
							new Number(NUM);
						}
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

}
