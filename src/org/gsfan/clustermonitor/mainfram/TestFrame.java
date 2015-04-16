package org.gsfan.clustermonitor.mainfram;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class TestFrame extends JFrame {
	public TestFrame() {
//		JFrame frame = new JFrame("sjh");
		this.setLayout(null);

//		this = gettable();
//		table = this.gettable();
//		table.addMouseListener(this);
		JScrollPane src = new JScrollPane(new HostTable(new HostTableModel()));
		src.setBounds(0, 0, 400, 200);		
		this.add(src);
		this.setSize(new Dimension(400, 200));
//		this.getContentPane().add(new HostTable(new DefaultTableModel()));
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		new TestFrame();

	}

}
