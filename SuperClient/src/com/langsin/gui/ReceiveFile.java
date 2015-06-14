package com.langsin.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ReceiveFile {
	private JFrame frame;
	private JLabel label;
	private JLabel filename;
	private JButton agree;
	private JButton refuse;
	private JLabel exit;
	private JLabel min;
	private byte[] filedata;

	public ReceiveFile(String NAME, String Filename, byte[] filedata) {
		this.filedata = filedata;
		frame = new JFrame();
		label = new JLabel(NAME.trim() + "发来了文件：");
		filename = new JLabel(Filename.trim());
		agree = new JButton("同意");
		refuse = new JButton("拒绝");
		frame.setLayout(null);
		frame.setUndecorated(true);
		new Drag(frame).setDragable();
		label.setBounds(20, 20, 300, 30);
		label.setFont(new Font("楷体", Font.BOLD, 20));
		label.setForeground(new Color(0, 0, 255));
		filename.setBounds(20, 55, 300, 30);
		filename.setFont(new Font("楷体", Font.BOLD, 20));
		filename.setForeground(new Color(0, 0, 255));
		frame.add(label);
		frame.setSize(300, 230);
		agree.setBounds(100, 150, 60, 30);
		refuse.setBounds(170, 150, 60, 30);
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
		frame.add(filename);
		frame.add(agree);
		frame.add(refuse);
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
			} else if (e.getComponent().equals(agree)) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("选择文件框"); // 标题哦...
				chooser.showSaveDialog(new JFrame("保存文件"));
				File file = chooser.getSelectedFile();// 得到被选中的文件
				if (file != null) {
					String path = file.getPath();
					FileOutputStream ou;
					try {
						ou = new FileOutputStream(path);
						ou.write(filedata);
						ou.flush();
						ou.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				frame.dispose();
			} else if (e.getComponent().equals(refuse)) {
				frame.dispose();
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
		 new ReceiveFile("eeeeeeee", "ttttttttttttttttttttttttttttt",null);
	}
}
