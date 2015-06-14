package com.langsin.gui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

class Drag {
	// AWT中的POINT 类
	JFrame ff = null;

	public Drag(JFrame frame) {
		this.ff = frame;
	}

	Point loc = null;
	Point tmp = null;
	boolean isDragged = false;

	// 写一个方法里面存放鼠标事件
	public void setDragable() {
		// 给label添加鼠标事件
		ff.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				isDragged = false;
				ff.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			// 鼠标按下触发
			public void mousePressed(MouseEvent e) {
				tmp = new Point(e.getX(), e.getY());
				isDragged = true;
				ff.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
		});
		// 添加鼠标事件
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