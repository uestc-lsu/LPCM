package org.gsfan.clustermonitor.mainfram;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.Socket;

import org.gsfan.clustermonitor.datatransmission.DiskMessage;
import org.gsfan.clustermonitor.datatransmission.FrameMsgByDelimiter;
import org.gsfan.clustermonitor.datatransmission.Message;
import org.gsfan.clustermonitor.datatransmission.MessageCodec;
import org.gsfan.clustermonitor.datatransmission.MessageFramer;
import org.gsfan.clustermonitor.datatransmission.MessageTextCodec;
import org.gsfan.clustermonitor.datatransmission.TCPClient;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PieChart{
	
	// create a dataset...
	private DefaultPieDataset dataset = null;
			
	private JFreeChart chart = null;
	private String title = null;
	private String hostIP = null;
	
	public PieChart(DefaultPieDataset dataset, String title, String hostIP){
		Font font = new Font("宋体", Font.PLAIN, 16);
		
		this.dataset = dataset;
		this.title = title;
		this.hostIP = hostIP;
		// create a chart...
		chart = ChartFactory.createPieChart(this.title,dataset,true, // legend?
				true, // tooltips?
				false // URLs?
		);
		
		chart.getLegend().setItemFont(font);//设置图例字体
		chart.getTitle().setFont(font);//设置标题字体
		
		PiePlot plot = (PiePlot)chart.getPlot();//设置字体
		plot.setLabelFont(font);
		
//		plot.setExplodePercent("羊肉", 0.1);//爆炸模式,使Pie的一块分离出去,不支持3D
	}
	
	public String getHostIP() {
		return hostIP;
	}


	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}


	public DefaultPieDataset getDataset() {
		return dataset;
	}

	public void setDataset(DefaultPieDataset dataset) {
		this.dataset = dataset;
	}

	public ChartPanel getChartPanel(){
		return new ChartPanel(chart);
	}
	
	public JFreeChart getJFreeChart(){
		return chart;
	}
	
	public void firstCommunication(Socket client) {
		if(client==null)
			return;
		MessageCodec codec = new MessageTextCodec();
		MessageFramer framer;
		try {
			framer = new FrameMsgByDelimiter(client.getInputStream());
			DiskMessage diskMsg = new DiskMessage();
			
			byte[] encodeMsg = codec.messageEncode(diskMsg);
			
			System.out.println("First communication:Sending disk message ("+ encodeMsg.length + ") bytes:");
			System.out.println(diskMsg);
			framer.frameMessage(encodeMsg, client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally {
//			try {
//				client.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public void subsequentCommunication(TCPClient client) {
		if(client==null)
			return;
		Message message = null;
		DiskMessage diskMsg = null;
		try{
			
			MessageCodec codec = new MessageTextCodec();
			FrameMsgByDelimiter framer = new FrameMsgByDelimiter(client.getInputStream());
			byte[] req;
			while((req=framer.nextMessage())!=null){
				System.out.println("Received disk message ("+req.length+") bytes" );
				message = codec.messageDecode(req);
				if(message instanceof DiskMessage){
					diskMsg = (DiskMessage)message;
				}
			}
			
			addDiskUsageObservation(diskMsg.getUsedSize(), diskMsg.getAvailableSize());
		}catch (IOException ioe){
			System.out.println("Error handling client:"+ioe.getMessage());
		} finally {
			System.out.println("************Client closing connection!*************");
			client.disconnectToServer();
		}
	}
	private void addDiskUsageObservation(long usedSize, long availableSize) {
		String usedLabel = "已使用"+usedSize/(1024*1024)+"GB";
		String avaiLabel = "未使用"+availableSize/(1024*1024)+"GB";
		dataset.clear();
		dataset.setValue(avaiLabel, availableSize);
		dataset.setValue(usedLabel, usedSize);

		PiePlot plot = (PiePlot)chart.getPlot();
		plot.setExplodePercent(avaiLabel, 0.05);
		plot.setExplodePercent(usedLabel, 0.05);
		plot.setSectionPaint(avaiLabel, Color.BLUE);
		plot.setSectionPaint(usedLabel, Color.RED);
	}
}
