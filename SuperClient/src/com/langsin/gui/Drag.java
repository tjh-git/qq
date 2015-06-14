package com.langsin.gui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

class Drag {
	// AWT�е�POINT ��
	JFrame ff = null;

	public Drag(JFrame frame) {
		this.ff = frame;
	}

	Point loc = null;
	Point tmp = null;
	boolean isDragged = false;

	// дһ����������������¼�
	public void setDragable() {
		// ��label�������¼�
		ff.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				isDragged = false;
				ff.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			// ��갴�´���
			public void mousePressed(MouseEvent e) {
				tmp = new Point(e.getX(), e.getY());
				isDragged = true;
				ff.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
		});
		// �������¼�
		ff.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			//
			public void mouseDragged(MouseEvent e) {
				if (isDragged) {
					loc = new Point(ff.getLocation().x + e.getX() - tmp.x, ff
							.getLocation().y + e.getY() - tmp.y);
					ff.setLocation(loc);
				}
			}
		});
	}
}