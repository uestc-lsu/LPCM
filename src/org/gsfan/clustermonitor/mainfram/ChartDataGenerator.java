package org.gsfan.clustermonitor.mainfram;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.Timer;

import org.gsfan.clustermonitor.datatransmission.TCPClient;

@SuppressWarnings("serial")
public class ChartDataGenerator extends Timer implements ActionListener{
	
	private MultiDynamicLineChart dynameicLieChart = null;
	private DiskMultiplePieChart diskPieChart = null;
	
	public static int showCount = 0;
	
	//interval is timer interval, 
	ChartDataGenerator(int interval, MultiDynamicLineChart dynameicLieChart){
		super(interval,null);
		this.diskPieChart = null;
		this.dynameicLieChart = dynameicLieChart;
		addActionListener(this);
	}
	
	ChartDataGenerator(int interval, DiskMultiplePieChart diskPieChart){
		super(interval,null);
		this.dynameicLieChart = null;
		this.diskPieChart = diskPieChart;
		addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e){	
		if(this.dynameicLieChart!=null) {
			Iterator<DynamicLineChart> iter = this.dynameicLieChart.getChartList().iterator();
			while(iter.hasNext()) {
				DynamicLineChart lineChart = iter.next();
				TCPClient cli = null;
				try {
					cli = new TCPClient(lineChart.getHostIP(),9000);
					System.out.println("************Client connected!*************");
					lineChart.firstCommunication(cli.getClient());
					lineChart.subsequentCommunication(cli);
				} catch (Exception e1) {
					System.out.println("give up data transmit!");
//					WarnMsgDialog msgDialog = new WarnMsgDialog("Connect to " + lineChart.getHostIP() + " error!");
//					Thread thread = new Thread(msgDialog);
//					thread.start();
				} finally {
					if(cli!=null)
						cli.disconnectToServer();
				}
			}
		} else if(this.diskPieChart!=null) {
			Iterator<PieChart> iter = this.diskPieChart.getChartList().iterator();
			while(iter.hasNext()) {
				PieChart pieChart = iter.next();
				TCPClient cli = null;
				try {
					cli = new TCPClient(pieChart.getHostIP(),9000);
					System.out.println("***Pie Chart*******Client connected!*******"+pieChart.getHostIP()+"******");
					pieChart.firstCommunication(cli.getClient());
					pieChart.subsequentCommunication(cli);
				} catch (Exception e1) {
					System.out.println("give up data transmit!");
				} finally {
					if(cli!=null)
						cli.disconnectToServer();
				}
			}
		}
	}
}
