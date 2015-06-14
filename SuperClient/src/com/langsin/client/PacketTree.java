package com.langsin.client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.langsin.gui.AddGroupResp;
import com.langsin.gui.AddGroupResult;
import com.langsin.gui.AddMember;
import com.langsin.gui.AddMemberResp;
import com.langsin.gui.ChatGroup;
import com.langsin.remove.MsgAddMember;
import com.langsin.remove.MsgAddMemberResp;
import com.langsin.remove.MsgFindMember;
import com.langsin.remove.MsgRemoveGroup;
import com.langsin.remove.MsgRemoveMember;
import comm.langsin.createadd.MsgAddGroup;
import comm.langsin.createadd.MsgAddGroupResp;
import comm.langsin.createadd.MsgCreatePacketResp;
import comm.langsin.model.PacketObject;
import comm.langsin.model.UserObject;
import comm.langsin.msg_file.MsgGetMember;
import comm.langsin.msg_file.MsgGetMemberResp;
import comm.langsin.msg_file.MsgGroupMsg;
import comm.langsin.msg_file.MsgReceiveFile;
import comm.langsin.msg_log_reg.MsgHead;
import comm.langsin.msgoffline.MsgGroupList;
import comm.langsin.protocol.Protocol;

public class PacketTree extends JTree {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Map<Integer, ChatGroup> CHAT = new HashMap<Integer, ChatGroup>();
	private int Mainnum;
	private String Mainname;
	public DefaultMutableTreeNode root;
	private ClientConnection Conn;
	private DefaultTreeCellRenderer model;// 树模型对象
	public DefaultTreeModel TM;
	private int Packetnum;
	private String Packetname;
	private ChatGroup Group;

	public PacketTree(int NUM, String NAME, ClientConnection CONN) {
		this.Mainnum = NUM;
		this.Mainname = NAME;
		this.Conn = CONN;
		root = new DefaultMutableTreeNode("我的群组");
		model = new DefaultTreeCellRenderer();
		this.setCellRenderer(model);
		TM = new DefaultTreeModel(root);
		this.setModel(TM);
		this.setRowHeight(40);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					ShowSendChat();
				}
			}
		});
	}

	public void onMsgReceive(MsgHead m) {
		if (m.getType() == Protocol.common_createpacket_resp) {
			MsgCreatePacketResp Mcpr = (MsgCreatePacketResp) m;
			model.setLeafIcon(new ImageIcon(PacketTree.class
					.getResource("/com/langsin/image/group.png")));
			DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(
					Mcpr.getPacketnum() + " " + Mcpr.getPacketname());
			root.add(newnode);
		} else if (m.getType() == Protocol.common_addgroup) {
			MsgAddGroup Mag = (MsgAddGroup) m;
			int Packetnum = Mag.getPacketnum();
			String Pascketname = Mag.getPascketname();
			String NMAE = Mag.getName();
			String Msg = Mag.getContent();
			new AddGroupResp(Mainnum, Mainname.trim(), Mag.getSrcNum(),
					NMAE.trim(), Packetnum, Pascketname.trim(), Msg.trim(),
					Conn);
		} else if (m.getType() == Protocol.common_grouplist) {
			MsgGroupList Mgl = (MsgGroupList) m;
			List<PacketObject> Packetlist = Mgl.getPacketlist();
			ImageIcon head = new ImageIcon(Packetlist.get(0).getPackethead());
			model.setLeafIcon(head);
			for (PacketObject packetObject : Packetlist) {
				DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(
						packetObject.getPacketnum() + " "
								+ packetObject.getPacketname().trim() + " "
								+ packetObject.getPacketsign().trim());
				root.add(newnode);
			}
		} else if (m.getType() == Protocol.common_addgroupresp) {// 添加群组请求消息
			MsgAddGroupResp Magr = (MsgAddGroupResp) m;
			byte State = Magr.getState();
			if (State == 0) {// 表示同意你加入该群
				DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(
						Magr.getPacketnum() + " " + Magr.getPacketname().trim());
				this.root.add(newnode);
				model.setLeafIcon(new ImageIcon(PacketTree.class
						.getResource("/com/langsin/image/group.png")));
				new AddGroupResult(Magr.getName().trim(), Magr.getPacketname()
						.trim(), Magr.getState());
			} else if(State == 1){
				new AddGroupResult(Magr.getName().trim(), Magr.getPacketname()
						.trim(), Magr.getState());
			}
		} else if (m.getType() == Protocol.common_getmember_resp) {
			MsgGetMemberResp Mgmr = (MsgGetMemberResp) m;
			List<UserObject> Member = Mgmr.getList();
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("群成员");
			for (UserObject userObject : Member) {
				DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(
						userObject.getUsernum() + " "
								+ userObject.getUsername());
				root.add(newnode);
			}
			DefaultMutableTreeNode fileroot = new DefaultMutableTreeNode("群文件");
			List<Object> File = Mgmr.getFile();
			for (Object object : File) {
				DefaultMutableTreeNode file = new DefaultMutableTreeNode(object);
				fileroot.add(file);
			}
			if (!CHAT.containsKey(Packetnum)) {
				Group = new ChatGroup(Mainnum, Mainname, Packetnum, null,
						Packetname, Conn, root, Mgmr.getState(), fileroot);
				CHAT.put(Packetnum, Group);
			}
		} else if (m.getType() == Protocol.common_chatgroupmsg) {// 群聊
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
			MsgGroupMsg Mgm = (MsgGroupMsg) m;
			int Packetnum = Mgm.getPacketnum();
			byte State = Mgm.getState();
			String NAME = Mgm.getName();
			String MSG = Mgm.getMsg();
			List<byte[]> bq = Mgm.getBq();
			if (!CHAT.containsKey(Packetnum)) {
				List<UserObject> Member = Mgm.getMemberlist();
				DefaultMutableTreeNode muo = new DefaultMutableTreeNode("群成员");
				for (UserObject userObject : Member) {
					DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(
							userObject.getUsernum() + " "
									+ userObject.getUsername());
					muo.add(newnode);
				}
				List<Object> File = Mgm.getFile();
				DefaultMutableTreeNode fileroot = new DefaultMutableTreeNode(
						"群文件");
				for (Object object : File) {
					DefaultMutableTreeNode filename = new DefaultMutableTreeNode(
							object.toString());
					fileroot.add(filename);
				}
				Group = new ChatGroup(Mainnum, Mainname, Packetnum,
						NAME.trim(), Mgm.getPacketname().trim(), Conn, muo,
						State, fileroot);
				Group.ReceiveMsg(NAME.trim(), MSG.trim(), bq);
				CHAT.put(Packetnum, Group);
			} else {
				CHAT.get(Packetnum).ReceiveMsg(NAME.trim(), MSG.trim(), bq);
			}
		} else if (m.getType() == Protocol.commpn_remove_group) {// 推出群组
			MsgRemoveGroup Mrg = (MsgRemoveGroup) m;
			JOptionPane.showMessageDialog(this,
					Mrg.getName() + "狠心退出" + Mrg.getGroupName(), "",
					JOptionPane.ERROR_MESSAGE);
		} else if (m.getType() == Protocol.common_receivefile) {// 接受文件
			MsgReceiveFile Mrf = (MsgReceiveFile) m;
			JFrame f = new JFrame();
			JFileChooser jf = new JFileChooser();
			jf.showSaveDialog(f);
			File file = jf.getSelectedFile();// 得到被选中的文件
			if (file != null) {
				String path = file.getPath();
				try {
					FileOutputStream ou = new FileOutputStream(path);
					ou.write(Mrf.getFiledata());
					ou.flush();
					ou.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (m.getType() == Protocol.common_removemeber) {// 被删除群组
			MsgRemoveMember Mrm = (MsgRemoveMember) m;
			int size = root.getChildCount();
			for (int i = 0; i < size; i++) {
				String node = String.valueOf(root.getChildAt(i));
				if (node.contains(String.valueOf(Mrm.getPacketnum()))) {
					root.remove(i);
				}
			}
			JOptionPane.showMessageDialog(null, Mrm.getMainname() + "将你移除"
					+ Mrm.getPascketname() + "该群", "",
					JOptionPane.ERROR_MESSAGE);
		} else if (m.getType() == Protocol.common_findmember) {
			MsgFindMember Mfm = (MsgFindMember) m;
			List<UserObject> List = Mfm.getUser();
			new AddMember(Mainnum, Mainname, Packetnum, Packetname, List, Conn);
		} else if (m.getType() == Protocol.common_addmember) {// 添加群成员
			MsgAddMember Mam = (MsgAddMember) m;
			new AddMemberResp(Mainnum, Mainname, Mam.getSrcNum(),
					Mam.getName().trim(), Mam.getPacketnum(), Mam.getPacketname().trim(),
					Conn);
		} else if (m.getType() == Protocol.common_addmemberresp) {// 添加群成员应答消息
			MsgAddMemberResp Mamr = (MsgAddMemberResp) m;
			byte State = Mamr.getState();
			if(State == 0){
				JOptionPane.showMessageDialog(null, Mamr.getName() .trim()+ "同意加入"
						+ Mamr.getPacketname()+ "该群", "",
						JOptionPane.ERROR_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, Mamr.getName().trim() + "拒绝加入"
						+ Mamr.getPacketname()+ "该群", "",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		javax.swing.SwingUtilities.updateComponentTreeUI(this);// 刷新界面
	}

	public void ShowSendChat() {
		TreePath Path = this.getSelectionPath();
		if (Path == null) {
			return;
		}
		Object obj = Path.getLastPathComponent();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
		if (!node.isRoot()) {// 如果该节点是叶子节点
			String TotalMsg = String.valueOf(node);
			String[] UserMsg = TotalMsg.split(" ");
			int Packetnum = Integer.parseInt(UserMsg[0].trim());
			String Packetname = UserMsg[1];
			// String Packetsign = UserMsg[2];
			this.Packetnum = Packetnum;
			this.Packetname = Packetname;
			MsgGetMember Mgm = new MsgGetMember();
			Mgm.setTotalLength(4 + 1 + 4 + 4 + 4);
			Mgm.setType(Protocol.common_getmember);
			Mgm.setSrcNum(Mainnum);
			Mgm.setDestNum(Protocol.ServerNUMBER);
			Mgm.setPacketnum(Packetnum);
			try {
				Conn.SendOrdinaryMsg(Mgm);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
