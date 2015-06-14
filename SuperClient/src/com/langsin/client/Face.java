
package com.langsin.client;

import javax.swing.ImageIcon;


public class Face extends ImageIcon{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Face() {
		this(1);
	}
	
	public Face(int num) {
		super(Face.class.getResource("/client/images/face/"+num+".gif"));
			}
	
	
}
