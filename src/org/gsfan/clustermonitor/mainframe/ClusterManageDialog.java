package org.gsfan.clustermonitor.mainframe;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.gsfan.clustermonitor.dbconnector.ClusterInfoFromMysql;
import org.gsfan.clustermonitor.dbconnector.DatabaseStorage;

@SuppressWarnings("serial")
public class ClusterManageDialog extends JFrame implements ActionListener {

	//用于设置窗体大小和位置
	private ComponentSizeAndLocation sizeAndLocation = ComponentSizeAndLocation.getInstance();
	
	private DatabaseStorage storage = new DatabaseStorage();
	private Hashtable<String, String> cluster = null;
	private Hashtable<String, String> hosts = null;
	private DropDownList dropdownList = null;
	private ClusterInfoFromMysql clusterInfo = null;	//作为comBox的下拉项
	
	//添加集群的时候，选择使用JTextField手动输入集群名称
	//删除集群的时候，选择使用JCombox下拉菜单选择待删除的集群名称
	private JComponent firstComboxOrTextfield = null;
	
	//添加节点的时候，选择使用JTextField手动输入节点名称
	//删除节点的时候，选择使用JCombox下拉菜单选择待删除的节点名称
	private JComponent secondComboxOrTextfield = null;
	private DropDownList dropdownListOfIP = null;
	
	private JTextField clusterText = null;
	
//	private JTextField serverIP = new JTextField(20);
	private JTextField serverIP = null;
//	private JTextField serverIP = null;
	private JButton submitButton = null;
	private JButton cancelButton = new JButton();
	
	private JLabel clusterLabel = new JLabel("集群名称");
	private JLabel IPLabel = null;
	
	private Font font = new Font("微软雅黑", Font.PLAIN, 14);
	
	private Dimension textDim = new Dimension(200, 25);
	private Dimension labelDim = new Dimension(70, 25);
	private int xStart = 50;
	private int yStart = 30;
	
	private String text = null;	//text="添加"or"删除"
	private int flag = 0;		//flag=1表示管理集群，flag=2表示管理节点
	
	//text="添加"or"删除"
	public ClusterManageDialog(String text, int flag) {	//flag=1表示添加或删除集群，flag=2表示添加或删除节点
		
		this.text = text;
		
		if(text.equals("添加") && (flag==1)) {
			dropdownList = null;//添加集群时不用下拉列表
			clusterText = new JTextField(20);
			this.firstComboxOrTextfield = clusterText;
			
			this.dropdownListOfIP = null; //添加集群时不用下拉列表
			serverIP = new JTextField(20);
			this.secondComboxOrTextfield = serverIP;
			
			this.setTitle("添加集群");
			
		} else if( text.equals("删除") || (text.equals("添加") && (flag==2)) ){
			
			clusterText = null;
			clusterInfo = new ClusterInfoFromMysql();	//从数据库获取数据
			int count = clusterInfo.getClusterCount();
			Object[] comboBoxItems = new Object[count];
			
			cluster = clusterInfo.getClusters();
			Iterator<String> iter = cluster.keySet().iterator();
			int i=0;
			while(iter.hasNext()) {
				String clusterName = (String)iter.next();
				
				comboBoxItems[i] = clusterName;
				i++;
			}
			
			dropdownList = new DropDownList(comboBoxItems, 1);
			this.firstComboxOrTextfield = dropdownList;
			
			if(flag==1) {
				if(text.equals("删除")) {
					this.setTitle("删除集群");
					serverIP = new JTextField(20);
					serverIP.setEditable(false);
					serverIP.setText(cluster.get(dropdownList.getSelectedItemStr()));
					this.secondComboxOrTextfield = serverIP;
				}
			} else if(flag==2) {
				if(text.equals("删除")) {
					
					hosts = clusterInfo.getClusterHosts(dropdownList.getSelectedItemStr());
					int size = hosts.size();
					Object[] comboBoxItems2 = null;
					if(size==0) {
						comboBoxItems2 = new Object[1];	//dropdownListOfIP=new DropDownList(comboBoxItems2);中comboxBoxItems2不能为null
						comboBoxItems2[0] = " ";
					} else {
						comboBoxItems2 = new Object[size];
					}
					iter = hosts.keySet().iterator();
					i=0;
					while(iter.hasNext()) {
						String hostName = (String)iter.next();
						String hostIP = hosts.get(hostName);
						comboBoxItems2[i] = hostIP;
						i++;
					}
					this.dropdownListOfIP = new DropDownList(comboBoxItems2, 2);
					serverIP = null;
					this.secondComboxOrTextfield = dropdownListOfIP;
					
					this.setTitle("删除节点");
					
				} else if(text.equals("添加")) {
					
					this.dropdownListOfIP = null; //添加集群时不用下拉列表
					serverIP = new JTextField(20);
					this.secondComboxOrTextfield = serverIP;
					
					this.setTitle("添加节点");
				}
			}
		} else {
			
		}
		
		if(flag==1) {
			IPLabel = new JLabel("服务器IP");
		} else if(flag==2) {
			IPLabel = new JLabel("节  点  IP");
		}
		
		this.flag = flag;
		
		submitButton = new JButton(text);
		submitButton.setFont(font);
		submitButton.addActionListener(this);
		cancelButton.setText("关闭");
		cancelButton.setFont(font);
		cancelButton.addActionListener(this);
		
		clusterLabel.setFont(font);
		IPLabel.setFont(font);
		
		this.setLayout(null);
		setAllComponentPosition();
		
//		this.setTitle("Cluster Manger");
		int width = xStart + labelDim.width + textDim.width + xStart;
		int height = yStart + textDim.height + 60;
		sizeAndLocation.setBounds(this, width, height);
//		this.setBounds((ComponentSizeAndLocation.screenWidth-width)/2, (ComponentSizeAndLocation.screenHeight-height)/2, width, height);
		yStart = 30;//the value of yStart changed after setAllComponentPosition()
		
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	private void setAllComponentPosition() {
		this.yStart = 30;
		int interval = 15;
		clusterLabel.setBounds(xStart, yStart, labelDim.width, labelDim.height);
		this.add(clusterLabel);
		firstComboxOrTextfield.setBounds(xStart+labelDim.width, yStart, textDim.width, textDim.height);
		this.add(firstComboxOrTextfield);
		
		yStart = yStart + labelDim.height + interval;
		IPLabel.setBounds(xStart, yStart, labelDim.width, labelDim.height);
		this.add(IPLabel);
		secondComboxOrTextfield.setBounds(xStart+labelDim.width, yStart, textDim.width, textDim.height);
		this.add(secondComboxOrTextfield);
		
		yStart = yStart + labelDim.height + interval;
		submitButton.setBounds(xStart, yStart, labelDim.width, labelDim.height+5);
		this.add(submitButton);
		cancelButton.setBounds(xStart+textDim.width, yStart, labelDim.width, labelDim.height+5);
		this.add(cancelButton);
	}
	
	public void actionPerformed(ActionEvent event) {
		
		if(event.getSource()==submitButton) {
			this.submitEventHandle();
		} else if(event.getSource()==cancelButton) {
			this.cancelEventHandle();
		} else {
			
		}

	}

	private void submitEventHandle() {

		if(firstComboxOrTextfield instanceof JTextField ) {
			//添加集群
			String cluster = ((JTextField)firstComboxOrTextfield).getText();
			String server = serverIP.getText();
			int ret = storage.storeInfoToClusterTable(cluster, server);
			if(ret==1) {
				JOptionPane.showMessageDialog(null, "***添加成功： "+cluster+"集群已成功添加***", "Prompt Message", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "***添加失败： "+cluster+"集群未成功添加***", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(firstComboxOrTextfield instanceof DropDownList ) {
			String btnText = submitButton.getText();
			if(flag==1 && btnText.equals("删除")) {
				//删除集群
				String clusterName = ((DropDownList)firstComboxOrTextfield).getSelectedItemStr();
				int ret = JOptionPane.showConfirmDialog(null, "是否真的要删除集群信息", "确认", JOptionPane.YES_NO_OPTION);
				if(ret == JOptionPane.YES_OPTION) {
					ret = storage.removeInfoFromClusterTable(clusterName);
					if(ret==1) {
						JOptionPane.showMessageDialog(null, "***删除成功： "+clusterName+"集群所有数据已删除***", "Prompt Message", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "***删除失败： "+clusterName+"集群不存在***", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if(flag==2 && btnText.equals("删除")) {
				//删除节点
				String clusterName = ((DropDownList)firstComboxOrTextfield).getSelectedItemStr();
				String hostIP = ((DropDownList)secondComboxOrTextfield).getSelectedItemStr();
				int ret = JOptionPane.showConfirmDialog(null, "是否真的要节点删除信息", "确认", JOptionPane.YES_NO_OPTION);
				if(ret == JOptionPane.YES_OPTION) {
					ret = storage.removeInfoFromHostTable(clusterName, hostIP);
					if(ret==1) {
						JOptionPane.showMessageDialog(null, "***删除成功： 集群"+clusterName+"中"+hostIP+"节点所有数据已删除***", "Prompt Message", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "***删除失败： "+hostIP+"节点不存在***", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				
			} else if(flag==2 && btnText.equals("添加")) {
				
				//添加节点
				String clusterName = ((DropDownList)firstComboxOrTextfield).getSelectedItemStr();
				String hostIP = serverIP.getText();
//				int ret = JOptionPane.showConfirmDialog(null, "是否真的要节点删除信息", "确认", JOptionPane.YES_NO_OPTION);
//				if(ret == JOptionPane.YES_OPTION) {
				int ret = storage.storeInfoToHostTable(clusterName, hostIP);
				if(ret==1) {
					JOptionPane.showMessageDialog(null, "***添加成功： 已成功向集群"+clusterName+"中"+hostIP+"节点***", "Prompt Message", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "***添加失败： "+hostIP+"节点添加失败***", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			
		}
	}
	
	private void cancelEventHandle() {
		this.dispose();
	}

	class DropDownList extends JComboBox<Object> implements ItemListener {
		private Object[] comboBoxItems = null;
		private Font font = new Font("微软雅黑", Font.PLAIN, 12);
		private String selectedItemStr = null;
		private int serialNum = 0;//flag = n 表示第n个DropDownList
		public DropDownList(Object[] comboBoxItems, int serialNum) {
			super(comboBoxItems);
			this.comboBoxItems = comboBoxItems;
			this.serialNum = serialNum;
			this.setFont(font);

			selectedItemStr = (String)this.comboBoxItems[this.comboBoxItems.length-1];
			this.setSelectedItem(this.comboBoxItems[this.comboBoxItems.length-1]);

			this.addItemListener(this);
		}
		
		public String getSelectedItemStr() {
			return selectedItemStr;
		}
		
		public void itemStateChanged(ItemEvent event) {

			if(event.getStateChange()==ItemEvent.SELECTED) {
				int index = this.getSelectedIndex();
				selectedItemStr = (String)this.comboBoxItems[index];
				this.setSelectedItem(selectedItemStr);

				if( flag==1 ) {			//添加或删除集群
					if(text.equals("添加")) {
						serverIP.setEditable(true);
					} else if(text.equals("删除")) {
						serverIP.setText(cluster.get(dropdownList.getSelectedItemStr()));
						serverIP.setEditable(false);
					}
				} else if( flag==2 ) {	//添加或删除节点
					
					if(text.equals("添加")) {
						((JTextField)secondComboxOrTextfield).setEditable(true);
					} else if(text.equals("删除")) {

						if(serialNum==1) {
							ClusterManageDialog.this.remove(secondComboxOrTextfield);
							
							hosts = clusterInfo.getClusterHosts(dropdownList.getSelectedItemStr());
							int size = hosts.size();
							Object[] comboBoxItems = null;
							if(size==0) {
								comboBoxItems = new Object[1];	//dropdownListOfIP=new DropDownList(comboBoxItems2);中comboxBoxItems2不能为null
								comboBoxItems[0] = " ";
							} else {
								comboBoxItems = new Object[size];
							}
							Iterator<String> iter = hosts.keySet().iterator();
							int i=0;
							while(iter.hasNext()) {
								String hostName = (String)iter.next();
								String hostIP = hosts.get(hostName);
								comboBoxItems[i] = hostIP;
								i++;
							}

							dropdownListOfIP = new DropDownList(comboBoxItems, 2);
							secondComboxOrTextfield = dropdownListOfIP;
						
							ClusterManageDialog.this.setAllComponentPosition();
							ClusterManageDialog.this.setVisible(true);
						}
					}
				}
			}
		}
	}
	
	public static void main(String argv[]) {
		new ClusterManageDialog("添加",1);
		new ClusterManageDialog("删除",2);
	}
}