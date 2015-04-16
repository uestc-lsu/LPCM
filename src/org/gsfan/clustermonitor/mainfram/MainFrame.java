package org.gsfan.clustermonitor.mainfram;

import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ChangeListener {
	public static String currentCluster = null;
	public static Hashtable<String, String> hostsOfCurCluster = new Hashtable<String, String>();//当前集群拥有的主机
	
//	public static LinkedList<String> hostsOfShowList = new LinkedList<String>();//当前显示区要显示信息的节点
	public static Hashtable<String, String> hostsOfShowList = new Hashtable<String, String>();//当前显示区要显示信息的节点
	public static boolean showListIsEmpty = true;
	
	private LeftPanel leftPanel = new LeftPanel();
	private static JTabbedPane tabPane = new JTabbedPane();
	//定义几个数据产生定时器
	private static ChartDataGenerator memoryDataDataGenerator = null;
	private static ChartDataGenerator netDataDataGenerator = null;
	private static ChartDataGenerator cpuDataDataGenerator = null;
	private static ChartDataGenerator diskDataDataGenerator = null;
	
	public MainFrame(){
		
		MainFrameSplitPane mainFrameSplitPane = new MainFrameSplitPane(leftPanel, tabPane);
		tabPane.addChangeListener(this);//添加监听事件
		this.setTitle("Cluster Monitor");
		this.getContentPane().add(mainFrameSplitPane);
		this.setBounds(300, 150, 900, 700);
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//退出系统

//		this.diskDataDataGenerator.start();
	}	
	
	public static void setDataDataGenerator() {
		cpuDataDataGenerator = MainFrameSplitPane.cpuChart.getChartDataGenerator();
		memoryDataDataGenerator = MainFrameSplitPane.memoryChart.getChartDataGenerator();
		netDataDataGenerator = MainFrameSplitPane.networkChart.getChartDataGenerator();
		diskDataDataGenerator = MainFrameSplitPane.diskMultiPieChart.getChartDataGenerator();
//		if(cpuDataDataGenerator!=null)
//		cpuDataDataGenerator.start();
		int index = tabPane.getSelectedIndex();
		String tabName = tabPane.getTitleAt(index);
		
		if(tabName.equals("CPU")) {
//			MainFrameSplitPane.cpuChart.setPerCharPosition();
			cpuDataDataGenerator.start();
		} else if (tabName.equals("内存")) {
//			MainFrameSplitPane.memoryChart.setPerCharPosition();
			memoryDataDataGenerator.start();
		} else if (tabName.equals("网络")) {
//			MainFrameSplitPane.networkChart.setPerCharPosition();
			netDataDataGenerator.start();
		} else if (tabName.equals("磁盘")) {
//			MainFrameSplitPane.diskMultiPieChart.setPerCharPosition();
			diskDataDataGenerator.start();			
		} 
//		diskDataDataGenerator.start();
	}
	
	public void stateChanged(ChangeEvent event){
		//若没有要显示的节点信息，则直接退出
		if(showListIsEmpty)
			return;
		
		int index = tabPane.getSelectedIndex();
		String tabName = tabPane.getTitleAt(index);

		if(tabName.equals("CPU")) {
//			MainFrameSplitPane.cpuChart.setPerCharPosition();
			memoryDataDataGenerator.stop();
			netDataDataGenerator.stop();
			diskDataDataGenerator.stop();
			cpuDataDataGenerator.start();
//			cpuDataDataGenerator.setDelay(1000);
		} else if (tabName.equals("内存")) {
			cpuDataDataGenerator.stop();
			netDataDataGenerator.stop();
			diskDataDataGenerator.stop();
			memoryDataDataGenerator.start();
//			MainFrameSplitPane.memoryChart.setPerCharPosition();
//			memoryDataDataGenerator.setDelay(1000);
		} else if (tabName.equals("磁盘")) {
			cpuDataDataGenerator.stop();
			memoryDataDataGenerator.stop();
			netDataDataGenerator.stop();
			diskDataDataGenerator.start();	
//			MainFrameSplitPane.diskMultiPieChart.setPerCharPosition();
//			diskDataDataGenerator.setDelay(30000);
		} else if (tabName.equals("网络")) {
			cpuDataDataGenerator.stop();
			memoryDataDataGenerator.stop();
			diskDataDataGenerator.stop();
			netDataDataGenerator.start();
//			MainFrameSplitPane.networkChart.setPerCharPosition();
//			netDataDataGenerator.setDelay(1000);
		}
	}
	
//	public static void main(String[] args){
//		new MainFrame();
//	}
}

/*
 package org.gsfan.clustermonitor.mainfram;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gsfan.clustermonitor.dbconnector.ClusterInfoFromMysql;
import org.jfree.data.general.DefaultPieDataset;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ChangeListener {
	public static String currentCluster = null;
	public static Hashtable<String, String> hostsOfCurCluster = new Hashtable<String, String>();//当前集群拥有的主机
	private LeftPanel leftPanel = new LeftPanel();
	private JTabbedPane tabPane = new JTabbedPane();
	//定义几个数据产生定时器
	private ChartDataGenerator memoryDataDataGenerator = null;
	private ChartDataGenerator netDataDataGenerator = null;
	private ChartDataGenerator cpuDataDataGenerator = null;
	private ChartDataGenerator diskDataDataGenerator = null;
	
	public MainFrame(){
		
		MainFrameSplitPane mainFrameSplitPane = new MainFrameSplitPane(leftPanel, tabPane);
		tabPane.addChangeListener(this);//添加监听事件
		this.setTitle("Cluster Monitor");
		this.getContentPane().add(mainFrameSplitPane);
		this.setBounds(300, 150, 900, 700);
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//退出系统
		
		this.cpuDataDataGenerator = MainFrameSplitPane.cpuChart.getChartDataGenerator();
		this.memoryDataDataGenerator = MainFrameSplitPane.memoryChart.getChartDataGenerator();
		this.netDataDataGenerator = MainFrameSplitPane.networkChart.getChartDataGenerator();
		this.diskDataDataGenerator = MainFrameSplitPane.diskMultiPieChart.getChartDataGenerator();
		this.cpuDataDataGenerator.start();
//		this.diskDataDataGenerator.start();
	}	
		
	public void stateChanged(ChangeEvent event){
		int index = tabPane.getSelectedIndex();
		String tabName = tabPane.getTitleAt(index);

		if(tabName.equals("CPU")) {
			memoryDataDataGenerator.stop();
			netDataDataGenerator.stop();
			diskDataDataGenerator.stop();
			cpuDataDataGenerator.start();
			this.cpuDataDataGenerator.setDelay(1000);
		} else if (tabName.equals("内存")) {
			cpuDataDataGenerator.stop();
			netDataDataGenerator.stop();
			diskDataDataGenerator.stop();
			memoryDataDataGenerator.start();
			this.memoryDataDataGenerator.setDelay(1000);
		} else if (tabName.equals("磁盘")) {
			cpuDataDataGenerator.stop();
			memoryDataDataGenerator.stop();
			netDataDataGenerator.stop();
			diskDataDataGenerator.start();			
			this.diskDataDataGenerator.setDelay(30000);
		} else if (tabName.equals("网络")) {
			cpuDataDataGenerator.stop();
			memoryDataDataGenerator.stop();
			diskDataDataGenerator.stop();
			netDataDataGenerator.start();
			this.netDataDataGenerator.setDelay(1000);
		}
	}
	
	public static void main(String[] args){
		new MainFrame();
	}
}

@SuppressWarnings("serial")
class MainFrameSplitPane extends JSplitPane {
	private JPanel leftPanel = null;
	private JTabbedPane tabPane = null;
	
	private Hashtable<String, String> hosts = new Hashtable<String, String>();//单个集群拥有的主机
	private Hashtable<String, Hashtable<String, String>> clusterAndHost = new Hashtable<String, Hashtable<String, String>>();//所有的集群
	private Hashtable<String, String> clusters = new Hashtable<String, String>();//所有的集群
//	private String currentCluster = null;//当前集群
	private ClusterInfoFromMysql clusterInfo = null;
	private Object[] comboBoxItems = null; //下拉列表项
	
	public static MultiDynamicLineChart cpuChart = new MultiDynamicLineChart();
	public static MultiDynamicLineChart memoryChart = new MultiDynamicLineChart();
	public static MultiDynamicLineChart networkChart = new MultiDynamicLineChart();
	public static DiskMultiplePieChart diskMultiPieChart = new DiskMultiplePieChart();

	
	public MainFrameSplitPane(LeftPanel leftPanel, JTabbedPane tabPane){
		super(JSplitPane.HORIZONTAL_SPLIT, leftPanel, tabPane);
		this.leftPanel = leftPanel;
		leftPanel.setLayout(null);
		
		this.tabPane = tabPane;

		this.setDividerSize(1);
		this.setDividerLocation(200);
		this.setEnabled(false);

		//从mysql获取集群信息
		clusterInfo = new ClusterInfoFromMysql();
//		clusterInfo = new ClusterInfoFromMysql(MainFrame.currentCluster);
		clusters = clusterInfo.getClusters();
		Iterator<String> iter = clusters.keySet().iterator();
		
		//获取当前集群的主机信息
		MainFrame.hostsOfCurCluster = clusterInfo.getClusterHosts(MainFrame.currentCluster);
		
		int count = clusterInfo.getClusterCount();
		comboBoxItems = new Object[count];
		int i = 0;
		while(iter.hasNext()){
			String clusterName = (String)iter.next();
			comboBoxItems[i] = clusterName;
			hosts = clusterInfo.getClusterHosts(clusterName);
			if(hosts!=null){//设置当前集群
//				this.currentCluster = clusterName;
			}
			clusterAndHost.put(clusterName, hosts);
			i++;
		}

		leftComponentInitialization();
		rightComponentInitialization();
	}

	public JPanel getLeftPanel() {
		return leftPanel;
	}
	public void setLeftPanel(JPanel panel) {
		this.leftPanel = panel;
	}
	
	public void leftComponentInitialization(){

		int blankAreaHeight = 20;
		int blankAreaCount = 0;
		int widgetHeight = 30;
		int widgetCount = 0;
		int extraBlankHeight = 3;
		int extraBlankCount = 0;
		
		Font font = new Font("黑体", Font.PLAIN, 16);//instantiation Font
		Font buttonFont = new Font("黑体", Font.PLAIN, 14);//instantiation Font
		
		JLabel label = new JLabel("Select Cluster");
		label.setText("选择集群");
		label.setFont(font);
		int yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight;
		Rectangle rect = new Rectangle(5, yStartPos, 200, widgetHeight);
		this.addComponentAtLeftSide(label, rect);
		widgetCount++;		
		
		yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
		JComboBox<Object> clusterNameComboBox = new JComboBox<Object>(comboBoxItems);
		clusterNameComboBox.setEditable(true);//可编辑
//		clusterNameComboBox.setSelectedItem(this.currentCluster);
		clusterNameComboBox.setSelectedItem(MainFrame.currentCluster);
		clusterNameComboBox.setFont(font);
		rect.setRect(0, yStartPos, 200, widgetHeight);
		this.addComponentAtLeftSide(clusterNameComboBox, rect);
		widgetCount++;
		blankAreaCount++;
		
		JLabel ipLabel = new JLabel("ip");
		ipLabel.setText("节点IP");
		ipLabel.setFont(font);
		yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
		rect.setRect(5, yStartPos, 50, widgetHeight);
		this.addComponentAtLeftSide(ipLabel, rect);
		JTextField ipField = new JTextField();
		rect.setRect(55, yStartPos, 135, widgetHeight);
		this.addComponentAtLeftSide(ipField, rect);
		widgetCount++;
		extraBlankCount++;
		
		JLabel hostLabel = new JLabel("节点名称");
		hostLabel.setText("节点名");
		hostLabel.setFont(font);
		yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
		rect.setRect(5, yStartPos, 50, widgetHeight);
		this.addComponentAtLeftSide(hostLabel, rect);
		JTextField hostField = new JTextField();
		rect.setRect(55, yStartPos, 135, widgetHeight);
		this.addComponentAtLeftSide(hostField, rect);
		widgetCount++;
		extraBlankCount++;
		
		JButton addNodeButton = new JButton("添加节点");
		addNodeButton.setFont(buttonFont);
		yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
		rect.setRect(5, yStartPos, 90, widgetHeight);
		this.addComponentAtLeftSide(addNodeButton, rect);
		JButton updateButton = new JButton("修改节点");
		updateButton.setFont(buttonFont);
		yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
		rect.setRect(100, yStartPos, 90, widgetHeight);
		this.addComponentAtLeftSide(updateButton, rect);
		widgetCount++;
		blankAreaCount++;
		
		
		JLabel userLabel = new JLabel("用户");
		userLabel.setText("账 号");
		userLabel.setFont(font);
		yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
		rect.setRect(5, yStartPos, 40, widgetHeight);
		this.addComponentAtLeftSide(userLabel, rect);
		JTextField userField = new JTextField();
		rect.setRect(55, yStartPos, 135, widgetHeight);
		this.addComponentAtLeftSide(userField, rect);
		widgetCount++;
		extraBlankCount++;
		
		JLabel passLabel = new JLabel("密码");
		passLabel.setText("密 码");
		passLabel.setFont(font);
		yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
		rect.setRect(5, yStartPos, 40, widgetHeight);
		this.addComponentAtLeftSide(passLabel, rect);
		JPasswordField passField = new JPasswordField();
		rect.setRect(55, yStartPos, 135, widgetHeight);
		this.addComponentAtLeftSide(passField, rect);
		widgetCount++;
		extraBlankCount++;
		
		//在这下面添加控件
		JButton modifyButton = new JButton("修改用户信息");
		modifyButton.setFont(buttonFont);
		yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
		rect.setRect(5, yStartPos, 185, widgetHeight);
		this.addComponentAtLeftSide(modifyButton, rect);
		widgetCount++;
		
	}
	
	public void rightComponentInitialization(){

//		Hashtable<String, String> hosts = clusterAndHost.get(this.currentCluster);
		Hashtable<String, String> hosts = clusterAndHost.get(MainFrame.currentCluster);
		Iterator<String> iter = hosts.keySet().iterator();
		
		while(iter.hasNext()){
			String chartTitle = iter.next();
			String hostIP = hosts.get(chartTitle);
			DefaultPieDataset dataset = new DefaultPieDataset();
			
			MemoryDynamicLineChart memoryLineChart = new MemoryDynamicLineChart(3000, chartTitle+"节点内存使用情况", hostIP);
			CpuDynamicLineChart cpuLineChart = new CpuDynamicLineChart(30000, chartTitle+"节点CPU使用率", hostIP);
			NetworkDynamicLineChart networkLineChart = new NetworkDynamicLineChart(60000, chartTitle+"节点已使用带宽", hostIP);
			PieChart pieChart = new PieChart(dataset, chartTitle+"节点磁盘使用情况", hostIP);

			cpuChart.addDynamicLineChart(cpuLineChart);
			ChartDataGenerator dataGenerator = new ChartDataGenerator(100, cpuChart);
			cpuChart.setChartDataGenerator(dataGenerator);
			
			memoryChart.addDynamicLineChart(memoryLineChart);
			dataGenerator = new ChartDataGenerator(100, memoryChart);
			memoryChart.setChartDataGenerator(dataGenerator);
			
			networkChart.addDynamicLineChart(networkLineChart);
			dataGenerator = new ChartDataGenerator(100, networkChart);
			networkChart.setChartDataGenerator(dataGenerator);
			
			
			diskMultiPieChart.addPieChart(pieChart);
			dataGenerator = new ChartDataGenerator(100, diskMultiPieChart);
			diskMultiPieChart.setChartDataGenerator(dataGenerator);
			
		}
		
		cpuChart.setPerCharPosition();
		addTabsAtRightSide("CPU", cpuChart);
		
		memoryChart.setPerCharPosition();
		addTabsAtRightSide("内存",memoryChart);
		
		diskMultiPieChart.setPerCharPosition();
		addTabsAtRightSide("磁盘", diskMultiPieChart);
		networkChart.setPerCharPosition();
		addTabsAtRightSide("网络", networkChart);
	}
	
	public void addTabsAtRightSide(String tabsName,JComponent component){
		
		TabsContent option = new TabsContent(tabsName, component);
		tabPane.setFont(new Font("黑体", Font.PLAIN, 14));
		tabPane.addTab(option.getTabsName(), option.getScrollPane());
	}
	
	public void addComponentAtLeftSide(JComponent component,Rectangle rect){
		component.setBounds(rect);
		this.getLeftPanel().add(component);
	}

}

@SuppressWarnings("serial")
class LeftPanel extends JPanel {
	public LeftPanel(){
		
	}
	@Override
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2D = (Graphics2D)g;
		g2D.setColor(Color.GRAY);
		g2D.setStroke(new BasicStroke(2.0f));
		g2D.drawLine(0, 70, 207, 70);
		g2D.drawLine(0, 185, 207, 185);
		g2D.drawLine(0, 300, 207, 300);
	}
}
 */