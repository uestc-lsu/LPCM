package org.gsfan.clustermonitor.mainfram;

import java.net.Socket;

import org.gsfan.clustermonitor.datatransmission.TCPClient;
import org.jfree.chart.ChartPanel;

//@SuppressWarnings("serial")
public abstract class DynamicLineChart{
	
	protected String titleName = null;
	protected String hostIP = null;

	public DynamicLineChart(String titleName, String hostIP){
		this.titleName = titleName;
		this.hostIP = hostIP;
		
//		this.showWarnMsg = show;
//		this.showWarnMsgCount = count;//count=0 do not show message dialog, or show message dialog
	}
	
	public String getTitleName(){
		return this.titleName;
	}
	
	public String getHostIP(){
		return this.hostIP;
	}
	
//	public int getShowWarnMsgCount() {
//		return showWarnMsgCount;
//	}
//
//	public void increaseShowWarnMsgCount() {
//		this.showWarnMsgCount++;
//	}
	
//	public boolean isShowWarnMsg() {
//		return showWarnMsg;
//	}
//
//	public void setShowWarnMsg(boolean showWarnMsg) {
//		this.showWarnMsg = showWarnMsg;
//	}
	
	public abstract ChartPanel getChartPanel();
	public abstract void firstCommunication(Socket client);
	public abstract void subsequentCommunication(TCPClient client);
//	public abstract void increaseShowWarnMsgCount();
//	public abstract int getShowWarnMsgCount();
}