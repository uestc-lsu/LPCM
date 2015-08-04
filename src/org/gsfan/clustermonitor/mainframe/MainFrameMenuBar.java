package org.gsfan.clustermonitor.mainframe;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class MainFrameMenuBar extends JMenuBar implements ActionListener {
//public class MainFrameMenuBar extends JMenuBar {
	
	private Font menuFont = null;
//	private Dimension menuSize = null;
//	private Dimension menuItemSize = null;

	private JMenu fileMenu = new JMenu("文件(F)");	//文件菜单
	private JMenu manageMenu = new JMenu("管理(M)");	//管理菜单
	private JMenu viewMenu = new JMenu("视图(V)");	//视图菜单
	private JMenu appearanceMenu = new JMenu("外观(C)");
	private JMenu helpMenu = new JMenu("帮助(H)");	//帮助菜单
	
	private JMenuItem refreshMenuItem = new JMenuItem("刷新(R)");		//文件菜单下的菜单项
	private JMenuItem exitMenuItem = new JMenuItem("退出(X)");		//文件菜单下的菜单项

	private JMenu addMenu = new JMenu("添加(A)");		//“管理”菜单下的“添加”菜单
	private JMenu delMenu = new JMenu("删除(D)");		//“管理”菜单下的“删除”菜单

	private JMenuItem addNode = new JMenuItem("添加节点");		//“添加”菜单下的“添加节点”菜单项
	private JMenuItem addCluster = new JMenuItem("添加集群");	//“添加”菜单下的“添加集群”菜单项
	
	private JMenuItem delNode = new JMenuItem("删除节点");		//“删除”菜单下的“删除节点”菜单项
	private JMenuItem delCluster = new JMenuItem("删除集群");	//“删除”菜单下的“删除集群”菜单项
	
	private JMenuItem changeThemeMenuItem = new JMenuItem("更改主题");
	private JMenuItem changeSkinMenuItem = new JMenuItem("更改皮肤");
	
	private JMenuItem helpMenuItem = new JMenuItem("帮助");		//"帮助"菜单下的"帮助"菜单项
	private JMenuItem userGuidMenuItem = new JMenuItem("用户手册");
	private JMenuItem aboutMenuItem = new JMenuItem("关于...");	//"帮助"菜单下的"关于"菜单项
	
	private Vector<JMenuItem> menuItemEventVect = new Vector<JMenuItem>();	//用于存储需要添加事件的按钮
	
	private String shutcutPattern = "[A-Z]+";
	
	public MainFrameMenuBar() {
		
		this.menuFont = new Font("微软雅黑", Font.PLAIN, 14);
//		this.menuSize = new Dimension(60, 20);
//		this.menuItemSize = new Dimension(80, 20);
		
//		addCluster.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				addClusterEventHandle();				
//			}
//		});
//		
//		addNode.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				addNodeEventHandle();				
//			}
//		});
//		
//		delCluster.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				delClusterEventHandle();				
//			}
//		});
//		
//		delNode.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				delNodeEventHandle();				
//			}
//		});
		menuItemEventVect.addElement(addCluster);
		menuItemEventVect.addElement(addNode);
		menuItemEventVect.addElement(delCluster);
		menuItemEventVect.addElement(delNode);
		
		JComponent items1[] = {addCluster, addNode};
		addItemForMenu(addMenu, items1);
		
		JComponent items2[] = {delCluster, delNode};
		addItemForMenu(delMenu, items2);
		
		JComponent items3[] = {addMenu, delMenu};
		addItemForMenu(manageMenu, items3);
		
//		this.add(fileMenu);		//给菜单条添加“文件”菜单
		JComponent items4[] = {refreshMenuItem, exitMenuItem};
//		fileMenu.add(exitMenuItem);
//		fileMenu.setPreferredSize(this.menuSize);
//		this.add(fileMenu);
		this.addMenu(fileMenu, items4);
		refreshMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
//		menuItemEventVect.addElement(refreshMenuItem);
		menuItemEventVect.addElement(exitMenuItem);
		
//		this.add(manageMenu);	//给菜单条添加“管理”菜单
		JComponent items5[] = {addMenu, delMenu};
		this.addMenu(manageMenu, items5);
//		this.add(helpMenu);		//给菜单条添加“帮助”菜单
		
		this.addMenu(viewMenu, null);
		
		JComponent items6[] = {changeThemeMenuItem, changeSkinMenuItem};
		this.addMenu(appearanceMenu, items6);
		menuItemEventVect.addElement(changeThemeMenuItem);
		menuItemEventVect.addElement(changeSkinMenuItem);
		
		JComponent items7[] = {helpMenuItem, userGuidMenuItem, aboutMenuItem};
		this.addMenu(helpMenu, items7);
		
		menuItemEventVect.addElement(helpMenuItem);
		menuItemEventVect.addElement(userGuidMenuItem);
		menuItemEventVect.addElement(aboutMenuItem);
		
//		this.setBounds(0, 0, 1300, 30);
		addActionListerForAllEvents();
	}
	
	//给菜单条添加菜单
	public void addMenu(JMenu menu, JComponent items[]) {
		if(menu==null) {
			return;
		}
		
		Pattern pattern = Pattern.compile(shutcutPattern);
		Matcher match = null;
		if(items!=null) {
			for(int i = 0; i < items.length; i++) {
//				items[i].setPreferredSize(this.menuSize);	//设定大小
				items[i].setFont(this.menuFont);
				
				//JMenu extends JMenuItem
				match = pattern.matcher(((JMenuItem)items[i]).getText());
				while(match.find()) {
					String shutcut = match.group();
					if(items[i] instanceof JMenuItem) {
						((JMenuItem)items[i]).setMnemonic(shutcut.toCharArray()[0]);
					} else if(items[i] instanceof JMenu){
						((JMenu)items[i]).setAccelerator(KeyStroke.getKeyStroke(shutcut.toCharArray()[0], KeyEvent.VK_S, false));
					}
				}
				
				menu.add(items[i]);
				menu.addSeparator();			
			}
		}
		
		pattern = Pattern.compile(shutcutPattern);
		match = pattern.matcher(menu.getText());
		while(match.find()) {
			String shutcut = match.group();
			menu.setMnemonic(shutcut.toCharArray()[0]);
		}
		
//		menu.setPreferredSize(this.menuSize);
		menu.setFont(this.menuFont);
		
		this.add(menu);
	}
	
	//给菜单添加菜单项
	private void addItemForMenu(JMenu menu, JComponent items[]) {
//		menu.setPreferredSize(this.menuSize);
		menu.setFont(this.menuFont);
		for(int i = 0; i < items.length; i++) {
//			items[i].setPreferredSize(this.menuItemSize);
			items[i].setFont(this.menuFont);
			menu.add(items[i]);
		}
	}
	
	private void addActionListerForAllEvents() {
		
		for(int i=0; i<menuItemEventVect.size(); i++) {
			JMenuItem component = menuItemEventVect.get(i);
			component.addActionListener(this);
		}
		
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==this.addCluster) {
			addClusterEventHandle();
		} else if(ae.getSource()==this.addNode) {
			addNodeEventHandle();
		} else if(ae.getSource()==this.delCluster) {
			delClusterEventHandle();
		} else if(ae.getSource()==this.delNode) {
			delNodeEventHandle();
		} else if(ae.getSource()==this.exitMenuItem){
			System.exit(0);
		}
	}
	
	private void addClusterEventHandle() {
		new ClusterManageDialog("添加", 1);
	}
	
	private void delClusterEventHandle() {
		new ClusterManageDialog("删除", 1);
	}
	
	private void addNodeEventHandle() {
		new ClusterManageDialog("添加", 2);
	}
	
	private void delNodeEventHandle() {
		new ClusterManageDialog("删除", 2);
	}
}