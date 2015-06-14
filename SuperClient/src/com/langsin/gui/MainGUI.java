package com.langsin.gui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.langsin.client.ClientConnection;
import com.langsin.client.ClientMsgListener;
import com.langsin.client.FriendTree;
import com.langsin.client.PacketTree;
import com.langsin.onoff.MsgOffline;
import com.langsin.onoff.MsgOnline;
import com.langsin.remove.MsgRemove;
import com.langsin.remove.MsgRemoveGroup;
import comm.langsin.find.MsgFindPacketResp;
import comm.langsin.find.MsgFindUserResp;
import comm.langsin.model.UserObject;
import comm.langsin.msg_log_reg.MsgHead;
import comm.langsin.protocol.Protocol;

public class MainGUI implements ClientMsgListener {
	private JFrame main = null;
	private int Mainnum;
	private String Mainname = null;
	private String MainSign = null;
	private ImageIcon Mainhead = null;
	private JTabbedPane tab;// �л����
	private ScrollPane friends;// �����б����
	private ScrollPane groups;// Ⱥ���б����
	private ScrollPane recently;// �����ϵ�����
	private JPanel tool;// ������湤�����Ĵ���
	private JLabel find;// ���Ұ�ť
	private JLabel create;// ����Ⱥ�鰴ť
	private JLabel remove;// ɾ������
	private JLabel removepacket;// �˳�Ⱥ��
	private JLabel head;
	private JLabel name;
	private JLabel sign;
	private JLabel exit;
	private JLabel min;
	private FriendTree FriendList;
	private PacketTree Packetlist;
	private FindUserPacket FUP = null;
	private ClientConnection Conn;
	private JLabel background;
	private JLabel seek;
	private JLabel fenzu;
	private JLabel select;

	public MainGUI(UserObject User, ClientConnection Conn) {
		this.Mainnum = User.getUsernum();// �û��˺�
		this.Mainname = User.getUsername().trim();// �û��ǳ�
		this.MainSign = User.getSignature().trim();// �û�����ǩ��
		this.Mainhead = new ImageIcon(User.getUserhead());// �û�ͷ��
		this.Conn = Conn;
		main = new JFrame();
		main.setLayout(null);
		background = new JLabel(new ImageIcon(
				MainGUI.class.getResource("/com/langsin/image/main1.jpg")));
		background.setBounds(0, 0, 300, 150);
		background.setLayout(null);
		seek = new JLabel(new ImageIcon(
				MainGUI.class.getResource("/com/langsin/image/seek.png")));
		seek.setBounds(0, 120, 300, 30);
		fenzu = new JLabel(new ImageIcon(
				MainGUI.class.getResource("/com/langsin/image/fenzu.png")));
		fenzu.setBounds(0, 150, 300, 38);
		background.add(seek);
		main.add(fenzu);
		main.add(background);
		main.setUndecorated(true);
		new Drag(main).setDragable();
		head = new JLabel(new ImageIcon(
				MainGUI.class.getResource("/com/langsin/image/head2.jpg")));
		select = new JLabel(new ImageIcon(
				MainGUI.class.getResource("/com/langsin/image/tool.png")));
		name = new JLabel(Mainname);
		sign = new JLabel(MainSign);
		head.setBounds(12, 30, 70, 70);
		select.setBounds(85, 90, 215, 30);
		// background.add(select);
		name.setBounds(100, 30, 120, 30);
		sign.setBounds(100, 50, 200, 30);
		name.setFont(new Font("����", Font.BOLD, 20));
		name.setForeground(new Color(0, 0, 255));
		sign.setFont(new Font("����", Font.ITALIC, 15));
		tab = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		// ��������ʼ��
		friends = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		groups = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		recently = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		FriendList = new FriendTree(Mainnum, Mainname, MainSign, Mainhead, Conn);
		Packetlist = new PacketTree(Mainnum, Mainname, Conn);
		friends.add(FriendList);
		groups.add(Packetlist);
		// �����¼�
		tab.addTab("�����б�", friends);
		tab.addTab("OOȺ��", groups);
		tab.addTab("�����ϵ��", recently);
		tab.setFont(new Font("����", Font.PLAIN, 19));
		tab.setForeground(new Color(100, 100, 100));

		tab.setBounds(0, 150, 300, 500);
		tool = new JPanel();
		tool.setLayout(null);
		find = new JLabel("����");
		find.setFont(new Font("����", Font.BOLD, 15));
		create = new JLabel("����Ⱥ");
		create.setFont(new Font("����", Font.BOLD, 15));
		remove = new JLabel("ɾ������");
		remove.setFont(new Font("����", Font.BOLD, 15));
		removepacket = new JLabel("�˳�Ⱥ��");
		removepacket.setFont(new Font("����", Font.BOLD, 15));
		removepacket.setBounds(190, 170, 90, 30);
		remove.setBounds(110, 170, 90, 30);
		find.setBounds(10, 170, 50, 30);
		create.setBounds(50, 170, 70, 30);
		exit = new JLabel("X");
		exit.setFont(new Font("����", Font.PLAIN, 20));
		exit.setForeground(new Color(0, 0, 0));
		min = new JLabel("-");
		min.setFont(new Font("����", Font.PLAIN, 30));
		min.setForeground(new Color(0, 0, 0));
		exit.setBounds(280, 0, 20, 20);
		min.setBounds(260, 0, 20, 20);
		background.add(head);
		background.add(sign);
		background.add(name);
		background.add(exit);
		background.add(min);
		tool.add(find);
		tool.add(create);
		tool.add(remove);
		tool.add(removepacket);
		tool.setBounds(0, 500, 300, 200);
		main.add(tab);
		main.add(tool);
		main.setSize(300, 700);
		main.setVisible(true);
		exit.addMouseListener(new Event());
		min.addMouseListener(new Event());
		find.addMouseListener(new Event());
		create.addMouseListener(new Event());
		remove.addMouseListener(new Event());
		removepacket.addMouseListener(new Event());
		head.addMouseListener(new Event());
	}

	public void FirstMsg(MsgHead msg) {
		if (msg.getType() == Protocol.common_finduser_resp) {// ���Һ���Ӧ����Ϣ
			MsgFindUserResp Mfu = (MsgFindUserResp) msg;
			ImageIcon head = new ImageIcon(Mfu.getFriendhead());
			FUP.showmsguser(head, Mfu.getFriendnum(), Mfu.getFriendname());
		} else if (msg.getType() == Protocol.common_findpacket_resp) {// ����Ⱥ��Ӧ����Ϣ
			MsgFindPacketResp Mfpr = (MsgFindPacketResp) msg;
			ImageIcon Packethead = new ImageIcon(Mfpr.getPackethead());
			FUP.showmsgpacket(Packethead, Mfpr.getPacketnum(),
					Mfpr.getPacketname(), Mfpr.getMainnum());
		} else if (msg.getType() == Protocol.common_createpacket_resp
				|| msg.getType() == Protocol.common_addgroup
				|| msg.getType() == Protocol.common_grouplist
				|| msg.getType() == Protocol.common_addgroupresp
				|| msg.getType() == Protocol.common_getmember_resp
				|| msg.getType() == Protocol.common_chatgroupmsg
				|| msg.getType() == Protocol.commpn_remove_group
				|| msg.getType() == Protocol.common_receivefile
				|| msg.getType() == Protocol.common_removemeber
				|| msg.getType() == Protocol.common_findmember
				|| msg.getType() == Protocol.common_addmember
				|| msg.getType() == Protocol.common_addmemberresp) {
			Packetlist.onMsgReceive(msg);
		} else if (msg.getType() == Protocol.common_addfriend
				|| msg.getType() == Protocol.common_addfriend_resp
				|| msg.getType() == Protocol.common_friendlist
				|| msg.getType() == Protocol.common_chatmsg
				|| msg.getType() == Protocol.common_nostatic
				|| msg.getType() == Protocol.common_chatfile
				|| msg.getType() == Protocol.common_voice
				|| msg.getType() == Protocol.common_voice_resp
				|| msg.getType() == Protocol.common_remove) {
			FriendList.onMsgReceive(msg);
		} else if (msg.getType() == Protocol.common_online) {// ����֪ͨ
			URL cb = AudioClip.class
					.getResource("/com/langsin/sound/Global.wav");
			AudioClip aau;
			aau = Applet.newAudioClip(cb);
			aau.loop();
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			aau.stop();
			MsgOnline Mol = (MsgOnline) msg;
			JOptionPane.showMessageDialog(main, Mol.getName() + "�����ˣ���", "",
					JOptionPane.ERROR_MESSAGE);
		} else if (msg.getType() == Protocol.common_offline) {
			URL cb = AudioClip.class.getResource("/com/langsin/sound/��ŷ.wav");
			AudioClip aau;
			aau = Applet.newAudioClip(cb);
			aau.loop();
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			aau.stop();
			MsgOffline Mol = (MsgOffline) msg;
			JOptionPane.showMessageDialog(main, Mol.getName() + "�����ˣ���", "",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	class Event extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				System.exit(0);
			} else if (e.getComponent().equals(min)) {
				main.setVisible(false);
			} else if (e.getComponent().equals(find)) {// ���Һ��Ѻ�Ⱥ��
				FUP = new FindUserPacket(Mainnum, Mainname, Conn);
			} else if (e.getComponent().equals(create)) {// ����Ⱥ��
				new CreatePacket(Mainnum, Mainname, Conn);
			} else if (e.getComponent().equals(remove)) {// ɾ������
				DefaultMutableTreeNode selectnode = (DefaultMutableTreeNode) FriendList
						.getLastSelectedPathComponent();
				if ((selectnode == null) || (selectnode.isRoot())) {// �ж�ѡ��Ľڵ��ǲ��Ǹ��ڵ����Ϊnull
					JOptionPane.showMessageDialog(FriendList, "��ѡ��һ�����Ѷ���",
							"�Ƿ�����", JOptionPane.ERROR_MESSAGE);
					return;
				} else if (FriendList.TM.getChildCount(FriendList.root) == 1) {
					JOptionPane.showMessageDialog(FriendList, "���ٱ���һ�����ѣ�",
							"�Ƿ�����", JOptionPane.ERROR_MESSAGE);
				} else {// ��������������Ϊ����
					MsgRemove Mrv = new MsgRemove();
					String allmsg = selectnode.toString();
					String[] str = allmsg.split(" ");
					Mrv.setTotalLength(4 + 1 + 4 + 4 + 10);
					Mrv.setType(Protocol.common_remove);// ɾ������
					Mrv.setSrcNum(Mainnum);
					Mrv.setDestNum(Integer.parseInt(str[0]));
					Mrv.setName(Mainname);
					FriendList.TM.removeNodeFromParent(selectnode);
					try {
						Conn.SendOrdinaryMsg(Mrv);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} else if (e.getComponent().equals(removepacket)) {// �����˳�Ⱥ��
				DefaultMutableTreeNode selectnode = (DefaultMutableTreeNode) Packetlist
						.getLastSelectedPathComponent();
				if ((selectnode == null) || (selectnode.isRoot())) {
					JOptionPane.showMessageDialog(Packetlist, "��ѡ��һ��Ⱥ�����",
							"�Ƿ�����", JOptionPane.ERROR_MESSAGE);
				} else if (Packetlist.TM.getChildCount(Packetlist.root) == 1) {
					JOptionPane.showMessageDialog(Packetlist, "���ٱ���һ��Ⱥ�����",
							"�Ƿ�����", JOptionPane.ERROR_MESSAGE);
				} else {
					String allmsg = selectnode.toString();
					String[] str = allmsg.split(" ");
					MsgRemoveGroup Mrg = new MsgRemoveGroup();
					Mrg.setTotalLength(4 + 1 + 4 + 4 + 4 + 10 + 10);
					Mrg.setType(Protocol.commpn_remove_group);
					Mrg.setSrcNum(Mainnum);
					Mrg.setDestNum(Protocol.ServerNUMBER);
					Mrg.setGroupNum(Integer.parseInt(str[0]));
					Mrg.setGroupName(str[1]);
					Mrg.setName(Mainname);
					Packetlist.TM.removeNodeFromParent(selectnode);
					try {
						Conn.SendOrdinaryMsg(Mrg);
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
				exit.setFont(new Font("����", Font.PLAIN, 27));
			} else if (e.getComponent().equals(min)) {
				min.setForeground(new Color(0, 255, 0));
				min.setFont(new Font("����", Font.PLAIN, 37));
			} else if (e.getComponent().equals(find)) {
				find.setForeground(new Color(0, 0, 255));
				find.setFont(new Font("����", Font.BOLD, 20));
			} else if (e.getComponent().equals(create)) {
				create.setForeground(new Color(0, 0, 255));
				create.setFont(new Font("����", Font.BOLD, 20));
			} else if (e.getComponent().equals(remove)) {
				remove.setForeground(new Color(0, 0, 255));
				remove.setFont(new Font("����", Font.BOLD, 20));
			} else if (e.getComponent().equals(removepacket)) {
				removepacket.setForeground(new Color(0, 0, 255));
				removepacket.setFont(new Font("����", Font.BOLD, 20));
			} else if (e.getComponent().equals(head)) {
				int x = main.getLocation().x;
				int y = main.getLocation().y + 30;
				new UserMsg(Mainname, Mainnum, MainSign, x, y);
			}
		}

		public void mouseExited(MouseEvent e) {
			if (e.getComponent().equals(exit)) {
				exit.setForeground(new Color(0, 0, 0));
				exit.setFont(new Font("����", Font.PLAIN, 20));
			} else if (e.getComponent().equals(min)) {
				min.setForeground(new Color(0, 0, 0));
				min.setFont(new Font("����", Font.PLAIN, 30));
			} else if (e.getComponent().equals(find)) {
				find.setForeground(new Color(0, 0, 0));
				find.setFont(new Font("����", Font.BOLD, 15));
			} else if (e.getComponent().equals(create)) {
				create.setForeground(new Color(0, 0, 0));
				create.setFont(new Font("����", Font.BOLD, 15));
			} else if (e.getComponent().equals(remove)) {
				remove.setForeground(new Color(0, 0, 0));
				remove.setFont(new Font("����", Font.BOLD, 15));
			} else if (e.getComponent().equals(removepacket)) {
				removepacket.setForeground(new Color(0, 0, 0));
				removepacket.setFont(new Font("����", Font.BOLD, 15));
			} else if (e.getComponent().equals(head)) {
				try {
					Thread.sleep(700);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				UserMsg.frame.setVisible(false);
			}
		}
	}
}
