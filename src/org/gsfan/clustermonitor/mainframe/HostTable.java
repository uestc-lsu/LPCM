package org.gsfan.clustermonitor.mainframe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.gsfan.clustermonitor.dbconnector.ClusterInfoFromMysql;

//hosts table at left of main frame
@SuppressWarnings("serial")
public class HostTable extends JTable {

	public HostTable(DefaultTableModel dm) {
		super(dm);

		TableColumn column = this.getColumn("NodeName");
		column.setPreferredWidth(80);
		column.setMaxWidth(90);		
		column.setCellRenderer(new CheckBoxRenderer());
		column.setCellEditor(new CheckBoxEditor(new JCheckBox()));
		
		
		column = this.getColumn("IP");
		column.setCellRenderer(new IPCellRenderer());
		column.setCellEditor(new IPCellEditor(new JTextField()));
		column.setPreferredWidth(120);
		column.setMaxWidth(120);
		
		this.setShowGrid(false);
	}

	public static void main(String args[]) {

	}
	
}

class CheckBoxRenderer implements TableCellRenderer {
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value == null)
			return null;
		
		Font font = new Font("微软雅黑", Font.PLAIN, 14);
		table.getTableHeader().setFont(font);
		
		return (Component) value;
	}
}

@SuppressWarnings("serial")
class IPCellEditor extends DefaultCellEditor {//implements TableCellEditor{
	private JTextField textField = null;
	public IPCellEditor(JTextField textField) {
		super(textField);
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (value == null)
			return null;
		
		Font font = new Font("微软雅黑", Font.PLAIN, 14);
		table.getTableHeader().setFont(font);
		
		textField = (JTextField)getComponent();
		textField.setFont(font);
		textField.setEditable(false);
		return textField;
	}

}

class IPCellRenderer implements TableCellRenderer {
	
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
//	String[] head = {"节点名" , "IP"};
	String[] head = {"NodeName" , "IP"};
	// 定义表的内容数据
	private Object[][] hosts = null;
	
	private int rowCountOfContainData = 0;

	public HostTableModel(){
		
		//获取当前集群中的主机信息
		ClusterInfoFromMysql clusterInfo = new ClusterInfoFromMysql();
//		clusterInfo.get
		MainFrame.hostsOfCurCluster = clusterInfo.getClusterHosts(MainFrame.currentCluster);
		
		Iterator<String> iter = MainFrame.hostsOfCurCluster.keySet().iterator();
		rowCountOfContainData = MainFrame.hostsOfCurCluster.size();
		int rowCount = rowCountOfContainData > 35 ? rowCountOfContainData : 35;
		this.hosts = new Object[rowCount][2];
//		Font font = new Font("黑体", Font.PLAIN, 14);
		Font font = new Font("微软雅黑", Font.PLAIN, 12);
		for(int i=0; iter.hasNext(); i++) {
			String hostName = (String)iter.next();
			String hostIP = MainFrame.hostsOfCurCluster.get(hostName);
			JCheckBox box = new JCheckBox(hostName+"号机");
			box.setFont(font);
			box.addItemListener(this);
			hosts[i][0] = box;
			
			JTextField text = new JTextField();
			text.setText(hostIP);
			text.setFont(font);
			text.setBorder(null);
			hosts[i][1] = text;
//			hosts[i][1] = new String(hostIP);
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
		for(int i=0; i<this.rowCountOfContainData; i++){
			if(item.equals(hosts[i][0])) {
				if(MainFrame.hostsOfShowList.containsKey(((JCheckBox)hosts[i][0]).getText()))
//					MainFrame.hostsOfShowList.remove(((JCheckBox)hosts[i][0]).getText(), hosts[i][1].toString());
					MainFrame.hostsOfShowList.remove(((JCheckBox)hosts[i][0]).getText());
				else{
//					MainFrame.hostsOfShowList.put(((JCheckBox)hosts[i][0]).getText(), hosts[i][1].toString());
					MainFrame.hostsOfShowList.put(((JCheckBox)hosts[i][0]).getText(), ((JTextField)hosts[i][1]).getText());
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