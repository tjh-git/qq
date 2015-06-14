package com.langsin.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.langsin.client.ClientConnection;
import com.langsin.remove.MsgAddMember;
import comm.langsin.model.UserObject;
import comm.langsin.protocol.Protocol;

public class AddMember {// 得到所有成员对想想
	private JFrame frame = null;
	private JScrollPane pan = null;
	private JTable table = null;
	private DefaultTableModel model = null;
	private int Mainnum;
	private String Mainname;
	private int Packetnum;
	private String Packetname;
	private ClientConnection conn;

	public AddMember(int Mainnum, String Mainname, int Packetnum,
			String Packetname, List<UserObject> User, ClientConnection conn) {
		this.Mainnum = Mainnum;
		this.Mainname = Mainname;
		this.Packetnum = Packetnum;
		this.Packetname = Packetname;
		this.conn = conn;
		frame = new JFrame();
		frame.setLayout(null);
		Object[] numColumns = { "账号:", "昵称:" };
		int size = User.size();
		Object[][] numRows = new Object[size][2];
		for (int i = 0; i < size; i++) {
			numRows[i][0] = User.get(i).getUsernum();
			numRows[i][1] = User.get(i).getUsername();
		}
		table = new JTable(numRows, numColumns);
		model = new DefaultTableModel(numRows, new String[] { "帐号:", "昵称:" });
		table.setModel(model);
		pan = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pan.setViewportView(table);
		pan.setSize(400, 400);
		frame.add(pan);
		frame.setSize(400, 400);
		frame.setVisible(true);
		table.addMouseListener(new Event());
	}

	class Event extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int[] index = table.getSelectedRows();
			String u = Arrays.toString(index).substring(1, 2);
			MsgAddMember Mam = new MsgAddMember();
			Mam.setTotalLength(4 + 1 + 4 + 4 + 4 + 10 + 10);
			Mam.setType(Protocol.common_addmember);
			Mam.setSrcNum(Mainnum);
			Mam.setDestNum(Integer.parseInt(u));
			Mam.setPacketnum(Packetnum);
			Mam.setName(Mainname);
			Mam.setPacketname(Packetname);
			try {
				conn.SendOrdinaryMsg(Mam);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		List<UserObject> list = new ArrayList<UserObject>();
		list.add(new UserObject(1001, null, "ffff", "", null));
		list.add(new UserObject(1002, null, "ddddd", null, null));
		new AddMember(1000, "", 1200, "", list, null);
	}
}
