package org.gsfan.clustermonitor.mainframe;

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
	
	private int width = 900;
	private int height = 700;
	
	//用于设置窗体大小和位置
	private ComponentSizeAndLocation sizeAndLocation = ComponentSizeAndLocation.getInstance();		
			
	public MainFrame(){
				
		MainFrameSplitPane mainFrameSplitPane = new MainFrameSplitPane(leftPanel, tabPane);
		tabPane.addChangeListener(this);//添加监听事件
		this.setTitle("Cluster Monitor");
		this.getContentPane().add(mainFrameSplitPane);
		
		sizeAndLocation.setBounds(this, width, height);
//		setLocationRelativeTo(null);
//		this.setBounds((screenWidth-width)/2, (screenHeight-height)/2, width, height);
		
		//add menu bar
		MainFrameMenuBar menuBar = new MainFrameMenuBar();
		this.setJMenuBar(menuBar);

//		pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//退出系统
	}	
	
	public static void setDataDataGenerator() {
		cpuDataDataGenerator = MainFrameSplitPane.cpuChart.getChartDataGenerator();
		memoryDataDataGenerator = MainFrameSplitPane.memoryChart.getChartDataGenerator();
		netDataDataGenerator = MainFrameSplitPane.networkChart.getChartDataGenerator();
		diskDataDataGenerator = MainFrameSplitPane.diskMultiPieChart.getChartDataGenerator();

		int index = tabPane.getSelectedIndex();
		String tabName = tabPane.getTitleAt(index);
		
		if(tabName.equals("CPU")) {
//			MainFrameSplitPane.cpuChart.setPerCharPosition();
			cpuDataDataGenerator.start();
		} else if (tabName.equals("内存 ")) {
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