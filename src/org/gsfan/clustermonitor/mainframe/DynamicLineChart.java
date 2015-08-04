package org.gsfan.clustermonitor.mainframe;

import java.awt.Font;
import java.net.Socket;

import org.gsfan.clustermonitor.datatransmission.TCPClient;
import org.jfree.chart.ChartPanel;

//@SuppressWarnings("serial")
public abstract class DynamicLineChart{
	
	protected String titleName = null;
	protected String hostIP = null;

	protected Font tickFont = new Font("微软雅黑", Font.PLAIN, 10);
	protected Font labelFont = new Font("微软雅黑", Font.PLAIN, 12);
	protected Font titleFont = new Font("微软雅黑", Font.BOLD, 14);
	protected Font legendFont = new Font("微软雅黑", Font.PLAIN, 12);//图例字体
	
	public DynamicLineChart(String titleName, String hostIP){
		this.titleName = titleName;
		this.hostIP = hostIP;
		
	}
	
	public String getTitleName(){
		return this.titleName;
	}
	
	public String getHostIP(){
		return this.hostIP;
	}
	
	public abstract ChartPanel getChartPanel();
	public abstract void firstCommunication(Socket client);
	public abstract void subsequentCommunication(TCPClient client);
}