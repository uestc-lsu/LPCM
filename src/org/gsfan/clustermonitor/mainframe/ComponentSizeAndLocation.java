package org.gsfan.clustermonitor.mainframe;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class ComponentSizeAndLocation {
	private int screenWidth = 0;
	private int screenHeight = 0;
	
	private static ComponentSizeAndLocation instance = new ComponentSizeAndLocation();
	
	private ComponentSizeAndLocation() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension dimension = kit.getScreenSize();
		screenWidth = dimension.width;
		screenHeight = dimension.height;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public static ComponentSizeAndLocation getInstance() {
		return instance;
	}

	public void setBounds(JFrame frame, int width, int height) {
		frame.setBounds((screenWidth-width)/2, (screenHeight-height)/2, width, height);
	}
}
