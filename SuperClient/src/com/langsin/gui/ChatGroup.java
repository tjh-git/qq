package com.langsin.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.langsin.client.ClientConnection;
import com.langsin.client.Face;
import com.langsin.client.FriendTree;
import com.langsin.client.PacketTree;
import com.langsin.remove.MsgFindMember;
import com.langsin.remove.MsgRemoveMember;
import comm.langsin.msg_file.MsgGroupFile;
import comm.langsin.msg_file.MsgGroupMsg;
import comm.langsin.msg_file.MsgReceiveFile;
import comm.langsin.msg_file.MsgShake;
import comm.langsin.protocol.Protocol;

public class ChatGroup {// ���Ľ���
	public static List<byte[]> list = new ArrayList<byte[]>();
	public static JFrame Chat = null;
	private FaceWindow faceWindow = null;
	private ClientConnection Conn;
	private JScrollPane top = null;
	private JScrollPane but = null;
	private JTextPane buttext = null;
	private int Mainnum;
	private String Mainname;
	private int Friendnum;
	private String Friendname;
	private JLabel min;
	private JLabel exit;
	private JLabel nlabel;
	private JLabel mlabel;
	private JLabel head;
	private JLabel video;
	private JLabel sound;
	private JLabel tool;
	private JLabel file;
	private JLabel remote;
	private JButton send;
	private JButton close;
	private JLabel pict;
	private JLabel shade;
	private Box box;
	private JScrollPane friendlist;
	public static JTree friend;
	public static JTree filelist;
	public DefaultMutableTreeNode root;
	public DefaultMutableTreeNode filename;
	private DefaultTreeModel model1;
	public DefaultTreeCellRenderer model;
	private JLabel delete = null;
	private JLabel add = null;
	private JLabel background1;
	private int chat = 1;
	private JScrollPane pan;

	public ChatGroup(int Mnum, String Mname, int Fnum, String Fname,
			String Packetname, final ClientConnection Conn,
			DefaultMutableTreeNode root, byte State,
			DefaultMutableTreeNode filename) {
		this.Mainnum = Mnum;
		this.Mainname = Mname;
		this.Friendnum = Fnum;
		this.Friendname = Packetname;
		this.Conn = Conn;
		this.root = root;
		this.filename = filename;
		background1 = new JLabel(new ImageIcon(
				ChatMsg.class.getResource("/com/langsin/image/chat1.jpg")));
		background1.setLayout(null);
		delete = new JLabel("T��");
		add = new JLabel("���");
		delete.setFont(new Font("����", Font.BOLD, 20));
		delete.setForeground(Color.RED);
		add.setFont(new Font("����", Font.BOLD, 20));
		model = new DefaultTreeCellRenderer();
		friend = new JTree(root);
		friend.setBackground(new Color(210, 240, 255));
		friend.setCellRenderer(model);
		friend.setRowHeight(30);
		model1 = new DefaultTreeModel(root);
		model.setLeafIcon(new ImageIcon(ChatGroup.class
				.getResource("/com/langsin/image/member.png")));
		friendlist = new JScrollPane(friend,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		delete.setBounds(460, 300, 60, 30);
		add.setBounds(520, 300, 60, 30);
		friendlist.setForeground(new Color(210, 240, 255));
		friendlist.setBounds(460, 330, 150, 300);
		friend.setBounds(0, 0, 150, 100);
		friend.setModel(model1);
		filelist = new JTree(filename);
		filelist.setForeground(new Color(210, 240, 255));
		pan = new JScrollPane(filelist,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pan.setBounds(460, 80, 150, 180);
		Chat = new JFrame();
		if (State == 0) {
			Chat.add(delete);
			Chat.add(add);
		}
		box = Box.createVerticalBox();
		Chat.setSize(600, 500);
		Chat.setLayout(null);
		Chat.add(friendlist);
		Chat.add(pan);
		exit = new JLabel("X");
		exit.setFont(new Font("����", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("����", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		nlabel = new JLabel(String.valueOf(Fnum));
		mlabel = new JLabel(Packetname);
		nlabel.setFont(new Font("����", Font.PLAIN, 20));
		nlabel.setForeground(new Color(0, 0, 255));
		mlabel.setFont(new Font("����", Font.PLAIN, 20));
		mlabel.setForeground(new Color(0, 0, 255));
		head = new JLabel(new ImageIcon(
				LogIn.class.getResource("/com/langsin/image/group.png")));
		video = new JLabel(new ImageIcon(
				LogIn.class.getResource("/com/langsin/image/video.png")));
		sound = new JLabel(new ImageIcon(
				LogIn.class.getResource("/com/langsin/image/voice.png")));
		tool = new JLabel(new ImageIcon(
				LogIn.class.getResource("/com/langsin/image/moreFunc.png")));
		remote = new JLabel(new ImageIcon(
				LogIn.class.getResource("/com/langsin/image/remoteAss.png")));
		file = new JLabel(new ImageIcon(
				LogIn.class.getResource("/com/langsin/image/filesTo.png")));
		pict = new JLabel(
				new ImageIcon(ChatGroup.class
						.getResource("/com/langsin/tool/expression.png")));
		shade = new JLabel(new ImageIcon(
				ChatGroup.class.getResource("/com/langsin/tool/shade.png")));

		buttext = new JTextPane();
		JPanel panel = new JPanel();
		panel.add(box);
		buttext.setBackground(new Color(210, 240, 255));
		box.setBackground(new Color(210, 240, 255));
		box.setFont(new Font("����", Font.BOLD, 20));
		top = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		but = new JScrollPane(buttext,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		top.setWheelScrollingEnabled(true);
		send = new JButton("����");
		close = new JButton("�ر�");
		video.setBounds(150, 20, 200, 35);
		sound.setBounds(190, 20, 35, 35);
		send.setBounds(340, 470, 60, 30);
		tool.setBounds(230, 20, 35, 35);
		file.setBounds(270, 20, 35, 35);
		remote.setBounds(310, 20, 35, 35);
		close.setBounds(400, 470, 60, 30);
		exit.setBounds(580, 0, 20, 20);
		min.setBounds(560, 0, 20, 20);
		head.setBounds(20, 20, 60, 60);
		but.setBounds(10, 360, 450, 110);
		top.setBounds(10, 80, 450, 250);
		pict.setBounds(10, 330, 30, 30);
		shade.setBounds(40, 330, 30, 30);
		nlabel.setBounds(80, 20, 100, 30);
		mlabel.setBounds(80, 50, 100, 30);
		background1.add(pict);
		background1.add(nlabel);
		background1.add(mlabel);
		background1.add(top);
		background1.add(but);
		background1.add(video);
		background1.add(sound);
		background1.add(tool);
		background1.add(file);
		background1.add(remote);
		background1.add(head);
		background1.add(nlabel);
		background1.add(mlabel);
		background1.add(top);
		background1.add(but);
		background1.add(close);
		background1.add(send);
		background1.add(exit);
		background1.add(min);
		background1.add(shade);
		background1.setBounds(0, 0, 600, 500);
		Chat.add(background1);
		Chat.setUndecorated(true);
		new Drag(Chat).setDragable();
		Chat.setVisible(true);
		exit.addMouseListener(new Event());
		close.addMouseListener(new Event());
		min.addMouseListener(new Event());
		send.addMouseListener(new Event());
		file.addMouseListener(new Event());
		pict.addMouseListener(new Event());
		shade.addMouseListener(new Event());
		head.addMouseListener(new Event());
		delete.addMouseListener(new Event());
		add.addMouseListener(new Event());
		buttext.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					MsgGroupMsg Msg = new MsgGroupMsg();
					List<Integer> bqssize = new ArrayList<Integer>();
					String msg = buttext.getText();
					Box boxsend = Box.createHorizontalBox();
					JLabel label1 = new JLabel(Mainname.trim() + ":"
							+ msg.trim());
					label1.setFont(new Font("����", Font.CENTER_BASELINE, 20));
					label1.setForeground(new Color(0, 255, 0));
					boxsend.add(label1, BorderLayout.EAST);
					buttext.setText("");
					int bqsize = list.size();
					int bqf = bqsize;
					int length = 0;
					while (bqsize > 0) {
						bqsize--;
						byte[] bqtop = list.get(bqsize);
						ImageIcon bd = new ImageIcon(bqtop);
						JLabel label2 = new JLabel(bd);
						label2.setBackground(Color.BLACK);
						boxsend.add(label2, BorderLayout.WEST);
						bqssize.add(bqtop.length);
						length = length + bqtop.length;
					}
					box.add(boxsend, BorderLayout.WEST);
					javax.swing.SwingUtilities.updateComponentTreeUI(top);// ˢ�½���
					int Msgsize = msg.getBytes().length;
					Msg.setTotalLength(4 + 1 + 4 + 4 + 10 + 10 + 4 + Msgsize
							+ 4 + length + 4 * bqf + 4);
					Msg.setType(Protocol.common_chatgroupmsg);
					Msg.setSrcNum(Mainnum);
					Msg.setDestNum(Protocol.ServerNUMBER);
					Msg.setName(Mainname);
					Msg.setPacketnum(Friendnum);
					Msg.setPacketname(Friendname);
					Msg.setMsg(msg);
					Msg.setMsgsize(Msgsize);
					Msg.setBq(list);
					Msg.setBqsize(bqssize);
					Msg.setBqnum(bqf);
					try {
						Conn.SendOrdinaryMsg(Msg);
						list.removeAll(list);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		filelist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					TreePath path = filelist.getSelectionPath();
					if (path == null) {
						return;
					}
					DefaultMutableTreeNode selectnode = (DefaultMutableTreeNode) path
							.getLastPathComponent();
					if (selectnode != null) {
						MsgReceiveFile Mrf = new MsgReceiveFile();
						Mrf.setTotalLength(4 + 1 + 4 + 4 + 4 + 100);
						Mrf.setType(Protocol.common_receivefile);
						Mrf.setSrcNum(Mainnum);
						Mrf.setDestNum(Protocol.ServerNUMBER);
						Mrf.setPacketnum(Friendnum);
						Mrf.setFile(selectnode.toString());
						try {
							Conn.SendOrdinaryMsg(Mrf);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
	}

	public void ReceiveMsg(String name, String Content, List<byte[]> list) {
		Box receivebox = Box.createHorizontalBox();
		JLabel label = new JLabel(name.trim() + ":" + Content.trim());
		label.setFont(new Font("����", Font.BOLD, 20));
		label.setForeground(new Color(0, 0, 255));
		receivebox.add(label);
		for (byte[] bs : list) {
			JLabel bq = new JLabel(new ImageIcon(bs));
			receivebox.add(bq);
		}
		box.add(receivebox);
		javax.swing.SwingUtilities.updateComponentTreeUI(top);// ˢ�½���
		top.setWheelScrollingEnabled(true);
	}

	class Event extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if ((e.getComponent().equals(exit))
					|| (e.getComponent().equals(close))) {
				Chat.dispose();
				PacketTree.CHAT.remove(Friendnum);
				FriendTree.CHAT.remove(Friendnum);
			} else if (e.getComponent().equals(min)) {
				Chat.setExtendedState(JFrame.ICONIFIED);
			} else if (e.getComponent().equals(pict)) {
				if (faceWindow == null) {
					faceWindow = new FaceWindow(Chat);
				} else {
					Point point = Chat.getLocationOnScreen();
					faceWindow.setLocation(point.x + 20, point.y + 120);
					faceWindow.setVisible(true);
				}
			} else if (e.getComponent().equals(send)) {
				MsgGroupMsg Msg = new MsgGroupMsg();
				List<Integer> bqssize = new ArrayList<Integer>();
				String msg = buttext.getText();
				Box boxsend = Box.createHorizontalBox();
				JLabel label1 = new JLabel(Mainname.trim() + ":" + msg.trim());
				label1.setFont(new Font("����", Font.CENTER_BASELINE, 20));
				label1.setForeground(new Color(0, 255, 0));
				boxsend.add(label1, BorderLayout.EAST);
				buttext.setText("");
				int bqsize = list.size();
				int bqf = bqsize;
				int length = 0;
				while (bqsize > 0) {
					bqsize--;
					byte[] bqtop = list.get(bqsize);
					ImageIcon bd = new ImageIcon(bqtop);
					JLabel label2 = new JLabel(bd);
					label2.setBackground(Color.BLACK);
					boxsend.add(label2, BorderLayout.WEST);
					bqssize.add(bqtop.length);
					length = length + bqtop.length;
				}
				box.add(boxsend, BorderLayout.WEST);
				javax.swing.SwingUtilities.updateComponentTreeUI(top);// ˢ�½���
				int Msgsize = msg.getBytes().length;
				Msg.setTotalLength(4 + 1 + 4 + 4 + 10 + 10 + 4 + Msgsize + 4
						+ length + 4 * bqf + 4);
				Msg.setType(Protocol.common_chatgroupmsg);
				Msg.setSrcNum(Mainnum);
				Msg.setDestNum(Protocol.ServerNUMBER);
				Msg.setName(Mainname);
				Msg.setPacketnum(Friendnum);
				Msg.setPacketname(Friendname);
				Msg.setMsg(msg);
				Msg.setMsgsize(Msgsize);
				Msg.setBq(list);
				Msg.setBqsize(bqssize);
				Msg.setBqnum(bqf);
				try {
					Conn.SendOrdinaryMsg(Msg);
					list.removeAll(list);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (e.getComponent().equals(shade)) {
				MsgShake Msh = new MsgShake();
				Msh.setTotalLength(4 + 1 + 4 + 4 + 10);
				Msh.setType(Protocol.common_nostatic);
				Msh.setSrcNum(Mainnum);
				Msh.setDestNum(Friendnum);
				Msh.setName(Mainname);
				try {
					Conn.SendOrdinaryMsg(Msh);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (e.getComponent().equals(head)) {
				if (chat == 1) {
					background1.setIcon(new ImageIcon(ChatGroup.class
							.getResource("/com/langsin/image/chat2.jpg")));
					chat = 2;
				} else if (chat == 2) {
					background1.setIcon(new ImageIcon(ChatGroup.class
							.getResource("/com/langsin/image/chat3.jpg")));
					chat = 3;
				} else if (chat == 3) {
					background1.setIcon(new ImageIcon(ChatGroup.class
							.getResource("/com/langsin/image/chat1.jpg")));
					chat = 1;
				}
				javax.swing.SwingUtilities.updateComponentTreeUI(Chat);// ˢ�½���
			} else if (e.getComponent().equals(file)) {// Ⱥ�ļ�����
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("ѡ���ļ���"); // ����Ŷ...
				chooser.showDialog(new JFrame(), "ѡ���ļ�");
				if (chooser.getSelectedFile() != null) {
					String filepath = chooser.getSelectedFile().getPath();// �ļ�·��
					String filename = chooser.getSelectedFile().getName();// �ļ�����
					MsgGroupFile Mgf = new MsgGroupFile();
					Mgf.setTotalLength(4 + 1 + 4 + 4 + 4 + 100 + 100);
					Mgf.setType(Protocol.common_sendgroupfile);
					Mgf.setSrcNum(Mainnum);
					Mgf.setDestNum(Protocol.ServerNUMBER);
					Mgf.setGroupnum(Friendnum);
					Mgf.setFilename(filename);
					Mgf.setFilePath(filepath);
					try {
						Conn.SendOrdinaryMsg(Mgf);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} else if (e.getComponent().equals(delete)) {// ɾ����Ա
				DefaultMutableTreeNode selectnode = (DefaultMutableTreeNode) friend
						.getLastSelectedPathComponent();
				if (selectnode == null || selectnode.isRoot()) {
					JOptionPane.showMessageDialog(friend, "��ѡ��һ�����Ѷ���", "�Ƿ�����",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (selectnode.toString().contains(Mainname)) {
					JOptionPane.showMessageDialog(friend, "�ó�Ա��Ⱥ������ɾ��", "�Ƿ�����",
							JOptionPane.ERROR_MESSAGE);
				} else {
					model1.removeNodeFromParent(selectnode);
					String membernum = String.valueOf(selectnode).substring(0,
							4);
					MsgRemoveMember Mrm = new MsgRemoveMember();
					Mrm.setTotalLength(4 + 1 + 4 + 4 + 4 + 10 + 10);
					Mrm.setType(Protocol.common_removemeber);
					Mrm.setSrcNum(Mainnum);
					Mrm.setDestNum(Integer.parseInt(membernum));
					Mrm.setPacketnum(Friendnum);
					Mrm.setMainname(Mainname);
					Mrm.setPascketname(mlabel.getText().trim());
					try {
						Conn.SendOrdinaryMsg(Mrm);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} else if (e.getComponent().equals(add)) {// ���Ⱥ��Ա
				MsgFindMember Mfm = new MsgFindMember();
				Mfm.setTotalLength(4 + 1 + 4 + 4);
				Mfm.setType(Protocol.common_findmember);
				Mfm.setSrcNum(Mainnum);
				Mfm.setDestNum(Protocol.ServerNUMBER);
				try {
					Conn.SendOrdinaryMsg(Mfm);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				exit.setForeground(new Color(255, 0, 0));
				exit.setFont(new Font("����", Font.PLAIN, 27));
			} else if (e.getComponent().equals(min)) {
				min.setForeground(new Color(0, 255, 0));
				min.setFont(new Font("����", Font.PLAIN, 37));
			}
		}

		public void mouseExited(MouseEvent e) {
			if (e.getComponent().equals(min)) {
				min.setForeground(new Color(0, 0, 0));
				min.setFont(new Font("����", Font.PLAIN, 30));
			} else if (e.getComponent().equals(exit)) {
				exit.setForeground(new Color(0, 0, 0));
				exit.setFont(new Font("����", Font.PLAIN, 20));
			}
		}
	}

	class FaceWindow extends JWindow implements ActionListener, Runnable,
			WindowFocusListener, MouseListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static final int faceNum = 134;
		JPanel paneFace = new JPanel();
		JLabel[] btnFace = new JLabel[faceNum];

		public FaceWindow(Frame owner) {
			super(owner);
			setSize(395, 245);
			Point point = Chat.getLocationOnScreen();
			setLocation(point.x + 20, point.y + 120);

			paneFace.setLayout(new GridLayout(9, 15));
			for (int i = 0; i < faceNum; i++) {
				btnFace[i] = new JLabel();
				paneFace.add(btnFace[i]);
			}
			new Thread(this).start();

			JTabbedPane tabPane = new JTabbedPane();
			tabPane.add("����", paneFace);
			add(tabPane);

			addWindowFocusListener(this);
			setVisible(true);
		}

		public void run() {
			for (int i = 0; i < faceNum; i++) {
				btnFace[i].setIcon(new Face(i));
				btnFace[i].addMouseListener(this);
				btnFace[i].setBorder(new EmptyBorder(1, 1, 1, 1));
				repaint();
			}
		}

		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			if (obj instanceof JLabel) {
				JLabel btFace = (JLabel) obj;
				buttext.insertIcon(btFace.getIcon());
				this.dispose();
			}
		}

		public void windowLostFocus(WindowEvent e) {
			dispose();
		}

		public void windowGainedFocus(WindowEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			Object obj = e.getSource();
			String[] str = obj.toString().split(",");
			// Document docu = buttext.setDocument("");;

			for (int i = 0; i < str.length; i++) {
				if (str[i]
						.contains("/D:/Eclipse/SuperClient/bin/client/images/face/")) {
					String path = str[i].substring(17);
					FileInputStream fis;
					try {
						fis = new FileInputStream(new File(path));
						byte[] bq = new byte[fis.available()];
						fis.read(bq);
						list.add(bq);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
			if (obj instanceof JLabel) {
				JLabel lbl = (JLabel) e.getSource();
				buttext.insertIcon(lbl.getIcon());
				this.dispose();
			}
		}

		public void mouseEntered(MouseEvent e) {
			Object obj = e.getSource();
			if (obj instanceof JLabel) {
				JLabel lbl = (JLabel) e.getSource();
				lbl.setBorder(new LineBorder(Color.DARK_GRAY));
			}
		}

		public void mouseExited(MouseEvent e) {
			Object obj = e.getSource();
			if (obj instanceof JLabel) {
				JLabel lbl = (JLabel) e.getSource();
				lbl.setBorder(new EmptyBorder(1, 1, 1, 1));
			}
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}

	public static void main(String[] args) {
		new ChatGroup(1000, "С��", 1000, "���ӭ", null, null, null, (byte) 1, null);
	}
}
