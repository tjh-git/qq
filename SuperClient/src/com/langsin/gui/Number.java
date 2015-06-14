package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Number {
	private JFrame num = null;
	private JLabel label = null;
	private JLabel numlabel = null;
	private JLabel number = null;
	private JLabel exit = null;
	private JLabel min = null;

	public Number(int NUM) {
		num = new JFrame();
		num.setUndecorated(true);
		new Drag(num).setDragable();
		num.setSize(340, 250);
		num.setLayout(null);
		label = new JLabel(new ImageIcon(
				Number.class.getResource("/com/langsin/image/num.jpg")));
		numlabel = new JLabel("账号:");
		numlabel.setFont(new Font("楷体", Font.BOLD, 30));
		numlabel.setForeground(Color.BLUE);
		number = new JLabel(String.valueOf(NUM));
		number.setFont(new Font("楷体", Font.BOLD, 25));
		label.setLayout(null);
		label.setBounds(0, 0, 340, 250);
		num.add(label);
		exit = new JLabel("X");
		exit.setFont(new Font("楷体", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("楷体", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		numlabel.setBounds(20, 50, 100, 30);
		number.setBounds(120, 50, 60, 30);
		min.setBounds(300, 0, 20, 20);
		exit.setBounds(320, 0, 20, 20);
		label.add(numlabel);
		label.add(number);
		label.add(exit);
		label.add(min);
		num.setVisible(true);
		exit.addMouseListener(new Event());
		min.addMouseListener(new Event());
	}

	// 移动类
	class Event extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				num.dispose();
			} else if (e.getComponent().equals(min)) {
				num.setExtendedState(JFrame.ICONIFIED);
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
}
