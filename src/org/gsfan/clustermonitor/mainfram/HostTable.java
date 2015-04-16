package org.gsfan.clustermonitor.mainfram;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.gsfan.clustermonitor.dbconnector.ClusterInfoFromMysql;

//hosts table at left of main frame
@SuppressWarnings("serial")
public class HostTable extends JTable {

//	private DefaultTableModel dm = null;
//	private int rowCount = 0;
//	private int rowCountOfContainData = 0;
	
//	private Object[][] hosts = null;
	public HostTable(DefaultTableModel dm) {
		super(dm);
		//获取当前集群中的主机信息
//		ClusterInfoFromMysql clusterInfo = new ClusterInfoFromMysql();
//		MainFrame.hostsOfCurCluster = clusterInfo.getClusterHosts("cluster1");
		
//		this.dm = dm;
//		Iterator<String> iter = MainFrame.hostsOfCurCluster.keySet().iterator();
//		rowCountOfContainData = MainFrame.hostsOfCurCluster.size();
//		rowCount = rowCountOfContainData > 20 ? rowCountOfContainData : 20;
//		this.hosts = new Object[rowCount][2];
//		Font font = new Font("黑体", Font.PLAIN, 14);
//		for(int i=0; iter.hasNext(); i++) {
//			String hostName = (String)iter.next();
//			String hostIP = MainFrame.hostsOfCurCluster.get(hostName);
//			JCheckBox box = new JCheckBox(hostName+"号机");
////			JCheckBox box = new JCheckBox(hostName);
//			box.setFont(font);
//			box.addItemListener(this);
//			hosts[i][0] = box;
//			hosts[i][1] = new String(hostIP);
//		}
//		
//		this.dm.setDataVector(hosts,new Object[] { "节点名" , "IP"});
		this.getColumn("节点名").setCellEditor(new CheckBoxEditor(new JCheckBox()));

//		this.setEnabled(false);
//		this.setEditingColumn(1);
		
//		table.getColumn("节点名").setCellRenderer(new CheckBoxRenderer());
		TableColumn column = this.getColumn("节点名");
		column.setPreferredWidth(90);
//		column.setMinWidth(40);
		column.setMaxWidth(100);
		column.setCellRenderer(new CheckBoxRenderer());
		
		
		column = this.getColumn("IP");
		column.setPreferredWidth(100);
		column.setMaxWidth(100);
		
	}

	public static void main(String args[]) {

	}
}

class CheckBoxRenderer implements TableCellRenderer {
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value == null)
			return null;
		
		return (Component) value;
	}
}

@SuppressWarnings("serial")
class CheckBoxEditor extends DefaultCellEditor implements ItemListener {
	private JCheckBox checkBox;

	public CheckBoxEditor(JCheckBox checkBox) {
		super(checkBox);
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (value == null)
			return null;
		
		checkBox = (JCheckBox) value;
		
		checkBox.setOpaque(true);
		checkBox.setBackground(Color.WHITE);
		
		checkBox.addItemListener(this);
		
		return (Component) value;
	}

	public Object getCellEditorValue() {
		checkBox.removeItemListener(this);
		return checkBox;
	}

	public void itemStateChanged(ItemEvent e) {
		super.fireEditingStopped();
	}
}

@SuppressWarnings("serial")
class HostTableModel extends DefaultTableModel implements ItemListener {
	// 定义表头数据
	String[] head = {"节点名" , "IP"};
	// 创建类型数组
	// Class[]
	// typeArray={Object.class,Object.class,Boolean.class,int.class,Object.class,Object.class};

	// 定义表的内容数据
	private Object[][] hosts = null;
	
	private int rowCountOfContainData = 0;
	// 定义表格每一列的数据类型
//	Class[] typeArray = { Object.class, Object.class, Boolean.class,
//			Integer.class, JComboBox.class, Object.class };

	public HostTableModel(){
		
		//获取当前集群中的主机信息
		ClusterInfoFromMysql clusterInfo = new ClusterInfoFromMysql();
		MainFrame.hostsOfCurCluster = clusterInfo.getClusterHosts(MainFrame.currentCluster);
		
		Iterator<String> iter = MainFrame.hostsOfCurCluster.keySet().iterator();
		rowCountOfContainData = MainFrame.hostsOfCurCluster.size();
		int rowCount = rowCountOfContainData > 20 ? rowCountOfContainData : 20;
		this.hosts = new Object[rowCount][2];
		Font font = new Font("黑体", Font.PLAIN, 14);
		for(int i=0; iter.hasNext(); i++) {
			String hostName = (String)iter.next();
			String hostIP = MainFrame.hostsOfCurCluster.get(hostName);
			JCheckBox box = new JCheckBox(hostName+"号机");
			box.setFont(font);
			box.addItemListener(this);
			hosts[i][0] = box;
			hosts[i][1] = new String(hostIP);
		}
		
		this.setDataVector(hosts, head);
		
	}
	// 使表格具有可编辑性
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex==0) {
			return true;
		}
		return false;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object item = e.getSource();
//		for(int i=0; i<MainFrame.hostsOfCurCluster.size(); i++){
		for(int i=0; i<this.rowCountOfContainData; i++){
			if(item.equals(hosts[i][0])) {
				if(MainFrame.hostsOfShowList.containsKey(((JCheckBox)hosts[i][0]).getText()))
					MainFrame.hostsOfShowList.remove(((JCheckBox)hosts[i][0]).getText(), hosts[i][1].toString());
				else{
					MainFrame.hostsOfShowList.put(((JCheckBox)hosts[i][0]).getText(), hosts[i][1].toString());
				}
			}
		}
		
		MainFrameSplitPane.showChart();
		if(MainFrame.hostsOfShowList.size()==0)	{	
			MainFrame.showListIsEmpty = true;
		} else {
			MainFrame.showListIsEmpty = false;
		}
		MainFrame.setDataDataGenerator();
	}
}
/*
 @SuppressWarnings("serial")
public class HostTable extends JTable implements ItemListener {

	private DefaultTableModel dm = new DefaultTableModel();
	private int rowCount = 0;
	private int rowCountOfContainData = 0;
	
	private Object[][] hosts = null;
	public HostTable(DefaultTableModel dm) {
		super(dm);
		//获取当前集群中的主机信息
//		ClusterInfoFromMysql clusterInfo = new ClusterInfoFromMysql();
//		MainFrame.hostsOfCurCluster = clusterInfo.getClusterHosts("cluster1");
		
		this.dm = dm;
		Iterator<String> iter = MainFrame.hostsOfCurCluster.keySet().iterator();
		rowCountOfContainData = MainFrame.hostsOfCurCluster.size();
		rowCount = rowCountOfContainData > 20 ? rowCountOfContainData : 20;
		this.hosts = new Object[rowCount][2];
		Font font = new Font("黑体", Font.PLAIN, 14);
		for(int i=0; iter.hasNext(); i++) {
			String hostName = (String)iter.next();
			String hostIP = MainFrame.hostsOfCurCluster.get(hostName);
			JCheckBox box = new JCheckBox(hostName+"号机");
//			JCheckBox box = new JCheckBox(hostName);
			box.setFont(font);
			box.addItemListener(this);
			hosts[i][0] = box;
			hosts[i][1] = new String(hostIP);
		}
		
//		MainFrame.hostsOfCurCluster.clear();
		
		this.dm.setDataVector(hosts,new Object[] { "节点名" , "IP"});
		this.getColumn("节点名").setCellEditor(new CheckBoxEditor(new JCheckBox()));

//		table.getColumn("节点名").setCellRenderer(new CheckBoxRenderer());
		TableColumn column = this.getColumn("节点名");
		column.setPreferredWidth(90);
//		column.setMinWidth(40);
		column.setMaxWidth(100);
		column.setCellRenderer(new CheckBoxRenderer());
		
		column = this.getColumn("IP");
		column.setPreferredWidth(100);
		column.setMaxWidth(100);
		
//		column.
		
//		this.addMouseListener(this);
//		this.setBounds(0, 0, 200, 800);
//		JFrame frame = new JFrame("sjh");
//		frame.setLayout(null);

//		this = gettable();
//		table = this.gettable();
//		table.addMouseListener(this);
//		JScrollPane src = new JScrollPane(table);
//		src.setBounds(0, 0, 400, 200);
//		frame.setSize(new Dimension(400, 200));
//		frame.add(src);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String args[]) {
//		new HostTable();
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		Object item = e.getSource();
//		for(int i=0; i<MainFrame.hostsOfCurCluster.size(); i++){
		for(int i=0; i<this.rowCountOfContainData; i++){
			if(item.equals(hosts[i][0])) {
				if(MainFrame.hostsOfShowList.containsKey(((JCheckBox)hosts[i][0]).getText()))
					MainFrame.hostsOfShowList.remove(((JCheckBox)hosts[i][0]).getText(), hosts[i][1].toString());
				else{
					MainFrame.hostsOfShowList.put(((JCheckBox)hosts[i][0]).getText(), hosts[i][1].toString());
				}
			}
		}
		MainFrameSplitPane.showChart();
		if(MainFrame.hostsOfShowList.size()==0)	{	
			MainFrame.showListIsEmpty = true;
		} else {
			MainFrame.showListIsEmpty = false;
		}
		MainFrame.setDataDataGenerator();
	}
}
 * */