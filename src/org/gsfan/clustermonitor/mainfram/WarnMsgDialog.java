package org.gsfan.clustermonitor.mainfram;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class WarnMsgDialog extends JFrame implements Runnable {
	
	private JLabel msgjl = null;
	private Color bg = null;
	private int time = 0;	//warn message dialog exit time 
	public WarnMsgDialog(String text, int time) {
		
		this.setTitle("Error!");
		this.setBounds(500, 400, 400, 50);
		msgjl = new JLabel(text, JLabel.CENTER);
//		this.bg = new Color(96, 204, 0);	// 66CC00
//		this.bg = new Color(51, 153, 51);	// 339933
		this.bg = new Color(255, 204, 51);	// FFCC33
		this.time = time;
	}
	public void run() {
		msgjl.setOpaque(true);	//not opaque
		msgjl.setBackground(this.bg);

//		Font warn = new Font("Times New Roman", Font.PLAIN, 14);
//		msgjl.setFont(warn);
		msgjl.setVisible(true);
		
		this.add(msgjl);
		this.setUndecorated(true);
		this.setVisible(true);
		
		try {
			Thread.currentThread().sleep(this.time*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.dispose();
	}

}
