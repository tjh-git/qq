package com.langsin.client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.langsin.gui.AddFriendResp;
import com.langsin.gui.AddFriendResult;
import com.langsin.gui.ChatMsg;
import com.langsin.gui.ReceiveFile;
import com.langsin.remove.MsgRemove;
import comm.langsin.createadd.MsgAddFriend;
import comm.langsin.createadd.MsgAddFriendResp;
import comm.langsin.model.UserObject;
import comm.langsin.msg_file.MsgSendChat;
import comm.langsin.msg_file.MsgSendFile;
import comm.langsin.msg_file.MsgShake;
import comm.langsin.msg_log_reg.MsgHead;
import comm.langsin.msgoffline.MsgFriendList;
import comm.langsin.protocol.Protocol;

public class FriendTree extends JTree {// �����б���
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Map<Integer, ChatMsg> CHAT = new HashMap<Integer, ChatMsg>();
	private ClientConnection conn = null; // ȡ���������ĵ�������
	public DefaultMutableTreeNode root = null; // ���ϵĸ��ڵ�,���ҵĺ���
	private DefaultMutableTreeNode me = null;
	private int Mainnum;
	private String Mainname = null;
	private DefaultTreeCellRenderer model = null;// ��ģ�Ͷ���
	public DefaultTreeModel TM = null;
	private ChatMsg ChatUI = null;

	public FriendTree(int NUM, String NAME, String SIGN, ImageIcon HEAD,
			ClientConnection CONN) {
		this.conn = CONN;
		this.Mainnum = NUM;
		this.Mainname = NAME;
		root = new DefaultMutableTreeNode("�ҵĺ���");
		me = new DefaultMutableTreeNode(NUM + " " + NAME + " " + SIGN);
		root.add(me);
		model = new DefaultTreeCellRenderer();// ��Ļ���
		this.setCellRenderer(model);
		TM = new DefaultTreeModel(root);
		this.setModel(TM);
		this.setRowHeight(40);
		HEAD.setImage(HEAD.getImage().getScaledInstance(40, 40,
				Image.SCALE_DEFAULT));
		model.setLeafIcon(HEAD);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {// ˫��������
					ShowSendChat();
				}
			}
		});
		this.addMouseListener(new Event());
	}

	public void onMsgReceive(MsgHead m) {// ���յ�����Ϣ
		if (m.getType() == Protocol.common_chatmsg) {// ������Ϣ
			URL cb = AudioClip.class.getResource("/com/langsin/sound/msg.wav");
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
			MsgSendChat Msc = (MsgSendChat) m;
			int Friendnum = Msc.getSrcNum();
			String NAME = Msc.getName();
			String MSG = Msc.getMsg();
			List<byte[]> bq = Msc.getBq();
			if (!CHAT.containsKey(Friendnum)) {
				ChatUI = new ChatMsg(Mainnum, Mainname, Friendnum, NAME.trim(),
						conn);
				ChatUI.ReceiveMsg(NAME, MSG, bq);
				CHAT.put(Friendnum, ChatUI);
			} else {
				CHAT.get(Friendnum).ReceiveMsg(NAME, MSG, bq);
			}
		} else if (m.getType() == Protocol.common_chatfile) {// �����ļ�
			MsgSendFile Msf = (MsgSendFile) m;
			new ReceiveFile(Msf.getName(), Msf.getFileName(), Msf.getFile());
		} else if (m.getType() == Protocol.common_addfriend) {// ��Ӻ���
			URL cb = AudioClip.class
					.getResource("/com/langsin/sound/system.wav");
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
			MsgAddFriend Maf = (MsgAddFriend) m;
			new AddFriendResp(Mainnum, Mainname, conn, Maf.getSrcNum(), Maf
					.getMyName().trim(), Maf.getMymag().trim(), Maf.getSign()
					.trim(), root, this);
		} else if (m.getType() == Protocol.common_addfriend_resp) {
			URL cb = AudioClip.class
					.getResource("/com/langsin/sound/system.wav");
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
			MsgAddFriendResp Mafr = (MsgAddFriendResp) m;
			String Friendname = Mafr.getFriendname().trim();
			int Friendnum = Mafr.getSrcNum();
			String FriendSign = Mafr.getFriendsign();
			if (Mafr.getState() == 0) {// ��ʾͬ��
				new AddFriendResult(Friendname, Friendnum, (byte) 0);
				DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(
						Friendnum + " " + Friendname + " " + FriendSign.trim());
				root.add(newnode);
			} else {
				new AddFriendResult(Friendname, Friendnum, (byte) 1);
			}
		} else if (m.getType() == Protocol.common_friendlist) {// �û������б�
			MsgFriendList Mfl = (MsgFriendList) m;
			List<UserObject> Friendlist = Mfl.getFriendlist();
			for (UserObject userObject : Friendlist) {
				DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(
						userObject.getUsernum() + " "
								+ userObject.getUsername().trim() + " "
								+ userObject.getSignature().trim());
				root.add(newnode);
			}
		} else if (m.getType() == Protocol.common_nostatic) {// ��������
			MsgShake Msh = (MsgShake) m;
			int NUM = Msh.getSrcNum();
			String Name = Msh.getName();
			if (!CHAT.containsKey(NUM)) {
				ChatUI = new ChatMsg(Mainnum, Mainname, NUM, Name.trim(), conn);
				ChatUI.change();
				CHAT.put(NUM, ChatUI);
			} else {
				ChatMsg U = CHAT.get(NUM);
				U.change();
			}
		} else if (m.getType() == Protocol.common_remove) {// ��ɾ������
			MsgRemove Mrv = (MsgRemove) m;
			JOptionPane.showMessageDialog(this, Mrv.getName() + "���İ���ɾ������", "",
					JOptionPane.ERROR_MESSAGE);
			int size = root.getChildCount();
			for (int i = 0; i < size; i++) {
				String node = String.valueOf(root.getChildAt(i))
						.substring(0, 4);
				if (node.equals(String.valueOf(Mrv.getSrcNum()))) {
					root.remove(i);
				}
			}
		}
		javax.swing.SwingUtilities.updateComponentTreeUI(this);// ˢ�½���
	}

	public void ShowSendChat() {
		TreePath Path = this.getSelectionPath();
		if (Path == null) {
			return;
		}
		Object obj = Path.getLastPathComponent();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
		if (node.isLeaf()) {// ����ýڵ���Ҷ�ӽڵ�
			String TotalMsg = String.valueOf(node);
			String[] UserMsg = TotalMsg.split(" ");
			int Friendnum = Integer.parseInt(UserMsg[0]);
			String Friendname = UserMsg[1];
			if (!CHAT.containsKey(Friendnum)) {
				ChatUI = new ChatMsg(Mainnum, Mainname, Friendnum, Friendname,
						conn);
				CHAT.put(Friendnum, ChatUI);
			}
		}
	}

	// class Action implements
	class Event extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {
			if (e.getComponent().equals(FriendTree.this)) {

			}
		}

		public void mouseExited(MouseEvent e) {

		}

	}
}
