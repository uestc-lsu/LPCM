package org.gsfan.clustermonitor.mainfram;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;

import org.gsfan.clustermonitor.datatransmission.CpuMessage;
import org.gsfan.clustermonitor.datatransmission.FrameMsgByDelimiter;
import org.gsfan.clustermonitor.datatransmission.Message;
import org.gsfan.clustermonitor.datatransmission.MessageCodec;
import org.gsfan.clustermonitor.datatransmission.MessageFramer;
import org.gsfan.clustermonitor.datatransmission.MessageTextCodec;
import org.gsfan.clustermonitor.datatransmission.TCPClient;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class CpuDynamicLineChart extends DynamicLineChart{

	private TimeSeries cpuUsage = null;
	
	private ChartPanel chartPanel = null;
	
	@SuppressWarnings("deprecation")
	public CpuDynamicLineChart(int maxAge, String titleName, String hostIP){
//		super.setLayout(null);//使用空布局
		super(titleName, hostIP);
//		super(titleName, hostIP, false);
//		super(titleName, hostIP, 0);
		
		this.cpuUsage = new TimeSeries("cpu使用率");
		this.cpuUsage.setMaximumItemAge(maxAge);
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(cpuUsage);
		
		DateAxis domain = new DateAxis("时间");
		NumberAxis range = new NumberAxis("CPU使用率/%");
		Font tickFont = new Font("SansSerif", Font.PLAIN, 10);
		Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
		domain.setTickLabelFont(tickFont);
		range.setTickLabelFont(tickFont);
		domain.setLabelFont(labelFont);
		range.setLabelFont(labelFont);
		range.setRange(0.0, 100.0);
		range.setTickUnit(new NumberTickUnit(5));//设置刻度线间隔
		
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
		renderer.setSeriesPaint(0, Color.red);
		renderer.setSeriesPaint(1, Color.blue);
//		renderer.setSeriesShape(WIDTH, 20);
		renderer.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
		
		XYPlot plot = new XYPlot(dataset, domain, range, renderer);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		domain.setAutoRange(true);
		domain.setLowerMargin(0.0);
		domain.setUpperMargin(0.0);
		domain.setTickLabelsVisible(true);
		
		range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		Font titleFont = new Font("SansSerif", Font.BOLD, 14);
		JFreeChart chart = new JFreeChart(this.titleName, titleFont, plot, true);
		chart.setBackgroundPaint(Color.white);
		chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 4, 4),
				BorderFactory.createLineBorder(Color.black)));
	}
	public String getTitleName(){
		return this.titleName;
	}
	
	public ChartPanel getChartPanel(){
		return this.chartPanel;
	}
	
	/**
	* Adds an observation to the 'cpu usage' time series.
	* @param y the total memory used.
	*/
	private void addCpuUsageObservation(float y) {
		this.cpuUsage.add(new Millisecond(), y);
	}
	
	public void firstCommunication(Socket client) {
		if(client==null)
			return;
		MessageCodec codec = new MessageTextCodec();
		MessageFramer framer;
		try {
			framer = new FrameMsgByDelimiter(client.getInputStream());
			CpuMessage cpuMsg = new CpuMessage();
			
			byte[] encodeMsg = codec.messageEncode(cpuMsg);
			
			System.out.println("First communication:Sending cpu message ("+ encodeMsg.length + ") bytes:");
			System.out.println(cpuMsg);
			framer.frameMessage(encodeMsg, client.getOutputStream());
		} catch (IOException e) {
			System.out.println("In " + this + " firstCommunication error! " + e.getMessage());
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
		CpuMessage cpuMsg = null;
		try{
			
			MessageCodec codec = new MessageTextCodec();
			FrameMsgByDelimiter framer = new FrameMsgByDelimiter(client.getInputStream());
			byte[] req;
			while((req=framer.nextMessage())!=null){
				System.out.println("Received message ("+req.length+") bytes" );
				message = codec.messageDecode(req);
				if(message instanceof CpuMessage){
					cpuMsg = (CpuMessage)message;
				}
			} 
			
//			System.out.println("************Client closing connection!*************");
			
		}catch (IOException ioe){
			System.out.println("Error handling client:"+ioe.getMessage());
		} finally {
			client.disconnectToServer();
		}
		addCpuUsageObservation(100*cpuMsg.getCpuUsage());
	}
	
//	@SuppressWarnings("serial")
//	class DataGenerator extends Timer implements ActionListener{
//		DataGenerator(int interval){
//			super(interval,null);
//			addActionListener(this);
//		}
//		
//		public void actionPerformed(ActionEvent e){
//			
//			TCPClient cli = new TCPClient(CpuDynamicLineChart.this.hostIP,9000);
//			System.out.println("************Client connected!*************");
//			firstCommunication(cli.getClient());
//			subsequentCommunication(cli);	
//		}
//	}
}
