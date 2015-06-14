package com.langsin.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
//登录界面
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.langsin.client.ClientConnection;

import comm.langsin.model.UserObject;

public class LogIn {
	private ClientConnection conn = new ClientConnection();
	private JFrame log;
	private JPanel mainpanel;
	private JPanel panel1;
	private JPanel panel2;
	private CardLayout cl;// 卡片布局管理器
	private JComboBox<Integer> NUM;
	private JPasswordField PWD;
	private JCheckBox box1;
	private JCheckBox box2;
	private JLabel logon;
	private JLabel background;
	private JLabel label;
	private JLabel cancel;
	private JLabel min;
	private JLabel exit;
	private JLabel reg;
	private JLabel want;
	private JLabel head;
	private JLabel head1;
	private Toolkit kit = Toolkit.getDefaultToolkit();

	public LogIn() {
		Dimension src = kit.getScreenSize();
		log = new JFrame();
		background = new JLabel(new ImageIcon(
				LogIn.class.getResource("/com/langsin/image/log.jpg")));
		mainpanel = new JPanel();
		panel1 = new JPanel();
		panel1.setSize(430, 146);
		NUM = new JComboBox<Integer>();
		NUM.setEditable(true);
		PWD = new JPasswordField();
		PWD.setFont(new Font("楷体", Font.PLAIN, 15));
		panel1.setLayout(null);
		NUM.setBounds(130, 10, 180, 30);
		PWD.setBounds(130, 40, 180, 30);
		logon = new JLabel("安全登录");
		logon.setFont(new Font("楷体", Font.PLAIN, 25));
		logon.setForeground(new Color(0, 0, 255));
		logon.setBounds(165, 100, 100, 30);
		box1 = new JCheckBox("记住密码");
		box2 = new JCheckBox("自动登录");
		box1.setBounds(130, 70, 80, 30);
		box2.setBounds(230, 70, 80, 30);
		reg = new JLabel("注册账号");
		reg.setForeground(new Color(0, 0, 100));
		reg.setFont(new Font("楷体", Font.PLAIN, 15));
		reg.setBounds(320, 10, 100, 30);
		want = new JLabel("忘记密码");
		want.setBounds(320, 40, 100, 30);
		want.setForeground(new Color(0, 0, 100));
		want.setFont(new Font("楷体", Font.PLAIN, 15));
		head1 = new JLabel(new ImageIcon(
				LogIn.class.getResource("/com/langsin/image/head.png")));
		head1.setBounds(30, 12, 75, 75);
		panel1.add(head1);
		panel1.add(reg);
		panel1.add(want);
		panel1.add(box1);
		panel1.add(box2);
		panel1.add(NUM);
		panel1.add(PWD);
		panel1.add(logon);
		panel2 = new JPanel();
		panel2.setSize(430, 146);
		panel2.setLayout(null);
		label = new JLabel("正在登录！！！");
		cancel = new JLabel("取消登录");
		label.setFont(new Font("楷体", Font.PLAIN, 20));
		cancel.setFont(new Font("楷体", Font.PLAIN, 25));
		label.setForeground(new Color(0, 100, 250));
		label.setBounds(150, 5, 160, 20);
		cancel.setForeground(new Color(0, 50, 255));
		cancel.setBounds(160, 100, 100, 30);
		head = new JLabel(new ImageIcon(
				LogIn.class.getResource("/com/langsin/image/head.png")));
		head.setBounds(170, 25, 75, 75);
		panel2.add(label);
		panel2.add(head);
		panel2.add(cancel);
		log.setSize(430, 330);
		log.setUndecorated(true);
		new Drag(log).setDragable();
		log.setLayout(null);
		background.setBounds(0, 0, 430, 184);
		mainpanel.setBounds(0, 184, 430, 146);
		exit = new JLabel("X");
		exit.setFont(new Font("楷体", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("楷体", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		exit.setBounds(410, 0, 20, 20);
		min.setBounds(390, 0, 20, 20);
		background.setLayout(null);
		background.add(exit);
		background.add(min);
		cl = new CardLayout();
		mainpanel.setLayout(cl);
		mainpanel.add(panel1, "1");
		mainpanel.add(panel2, "2");
		log.add(background);
		log.add(mainpanel);
		log.setLocation((src.width - 430) / 2, (src.height - 330) / 2);
		log.setVisible(true);
		logon.addMouseListener(new Event());
		cancel.addMouseListener(new Event());
		exit.addMouseListener(new Event());
		min.addMouseListener(new Event());
		reg.addMouseListener(new Event());
	}

	// 事件类
	class Event extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getComponent().equals(logon)) {
				cl.show(mainpanel, "2");
			} else if (e.getComponent().equals(cancel)) {
				cl.show(mainpanel, "1");
				conn.CloseClient();
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (e.getComponent().equals(logon)) {
				int Usernum = Integer.parseInt((String) NUM.getSelectedItem());
				String Userpwd = String.valueOf(PWD.getPassword());
				if (conn.ConnServer()) {
					UserObject User = conn.Log(Usernum, Userpwd);
					if (User != null) {
						MainGUI Main = new MainGUI(User, conn);
						conn.start();
						conn.AddMsgListener(Main);
						log.dispose();
					} else if(User ==null){
						conn.CloseClient();
						cl.first(mainpanel);
						JOptionPane.showMessageDialog(log,"该用户不存在！！", "",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if (e.getComponent().equals(cancel)) {
				cl.first(mainpanel);
				conn.CloseClient();
			} else if (e.getComponent().equals(exit)) {
				log.dispose();
			} else if (e.getComponent().equals(min)) {
				log.setExtendedState(JFrame.ICONIFIED);
			} else if (e.getComponent().equals(reg)) {
				new Regeister();
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
			if (e.getComponent().equals(min)) {
				min.setForeground(new Color(0, 0, 0));
				min.setFont(new Font("楷体", Font.PLAIN, 30));
			} else if (e.getComponent().equals(exit)) {
				exit.setForeground(new Color(0, 0, 0));
				exit.setFont(new Font("楷体", Font.PLAIN, 20));
			}
		}
	}

	public static void main(String[] args) {
		new LogIn();
	}
}
