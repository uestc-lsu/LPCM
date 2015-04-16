package org.gsfan.clustermonitor.mainfram;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;

import org.gsfan.clustermonitor.datatransmission.FrameMsgByDelimiter;
import org.gsfan.clustermonitor.datatransmission.MemoryMessage;
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

public class MemoryDynamicLineChart extends DynamicLineChart{
	private TimeSeries totalMemory = null;
	private TimeSeries freeMemory = null;	
	
	private ChartPanel chartPanel = null;
	private JFreeChart chart = null;
	@SuppressWarnings("deprecation")
	public MemoryDynamicLineChart(int maxAge,String titleName, String hostIP){
		super(titleName, hostIP);
		
//		super(titleName, hostIP, false);
//		super(titleName, hostIP, 0);
		
		this.totalMemory = new TimeSeries("总内存");
		this.totalMemory.setMaximumItemAge(maxAge);
		this.freeMemory = new TimeSeries("剩余内存");
		this.freeMemory.setMaximumItemAge(maxAge);
		
//		this.memoryUsage = new TimeSeries("内存使用率");
//		this.memoryUsage.setMaximumItemAge(maxAge);
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(totalMemory);
		dataset.addSeries(freeMemory);
//--------------------------------------------------------------------------------------		
//		TimeSeriesCollection memoryUsageDataset = new TimeSeriesCollection();
//		memoryUsageDataset.addSeries(memoryUsage);
//--------------------------------------------------------------------------------------		
		DateAxis domain = new DateAxis("时间");
		NumberAxis range = new NumberAxis("内存使用情况/GB");
		Font tickFont = new Font("SansSerif", Font.PLAIN, 10);
		Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
		domain.setTickLabelFont(tickFont);
		range.setTickUnit(new NumberTickUnit(0.3f));//设置刻度
		
		range.setTickLabelFont(tickFont);
		domain.setLabelFont(labelFont);
		range.setLabelFont(labelFont);
//		range.setRange(0.0, 20.0);
		
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
		renderer.setSeriesPaint(0, Color.red);
		renderer.setSeriesPaint(1, Color.blue);
		renderer.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
		
		XYPlot plot = new XYPlot(dataset, domain, range, renderer);
		
		//添加坐标轴
//		NumberAxis cpuUsageAxis = new NumberAxis("内存使用率");
//		XYPlot plot2 = new XYPlot(memoryUsageDataset, domain, cpuUsageAxis, null);
//		plot2.setRangeAxis(1, cpuUsageAxis);
//		plot2.setRangeAxisLocation(1,AxisLocation.BOTTOM_OR_RIGHT);
//		plot2.setDataset(1, memoryUsageDataset);
		
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
//		JFreeChart chart = new JFreeChart(this.titleName, titleFont, plot, true);
		chart = new JFreeChart(this.titleName, titleFont, plot, true);
		
		chart.setBackgroundPaint(Color.white);
		chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 4, 4),
				BorderFactory.createLineBorder(Color.black)));
	}
	
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public String getTitleName(){
		return this.titleName;
	}
	
	public ChartPanel getChartPanel(){
		return this.chartPanel;
	}
	/**
	* Adds an observation to the 'total memory' time series.
	*
	* @param y the total memory used.
	*/
	private void addTotalObservation(float y) {
		this.totalMemory.add(new Millisecond(), y);
	}
	/**
	* Adds an observation to the 'free memory' time series.
	*
	* @param y the free memory.
	*/
	private void addFreeObservation(float y) {
		this.freeMemory.add(new Millisecond(), y);		
	}
	
	private void addMemoryUsageObservation(float y) {
//		this.setTitleName(this.titleName + "("+ Float.toString(y) +"%)");
		DecimalFormat format = new DecimalFormat("##0.00");
		String usage = format.format(y);
//		chart.setTitle(this.titleName + "("+ Float.toString(y) +"%)");
		chart.setTitle(this.titleName + "("+ usage +"%)");
//		this.memoryUsage.add(new Millisecond(), y);		
	}
	
	public void firstCommunication(Socket client) {
		if(client==null)
			return;
		MessageCodec codec = new MessageTextCodec();
		MessageFramer framer;
		try {
			framer = new FrameMsgByDelimiter(client.getInputStream());
			MemoryMessage memoryMsg = new MemoryMessage();
			
			byte[] encodeMsg = codec.messageEncode(memoryMsg);
			
			System.out.println("First communication: Sending memory message ("+ encodeMsg.length + ") bytes:");
			System.out.println(memoryMsg);
			framer.frameMessage(encodeMsg, client.getOutputStream());
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println("In " + this + "firstCommunication error!");
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
		MemoryMessage memMsg = null;
		try{
			
			MessageCodec codec = new MessageTextCodec();
//			QueryService service  = new QueryService();
			FrameMsgByDelimiter framer = new FrameMsgByDelimiter(client.getInputStream());
			byte[] req;
			while((req=framer.nextMessage())!=null){
				System.out.println("Received message ("+req.length+") bytes" );
//				message = service.handleRequest(codec.messageDecode(req));
				message = codec.messageDecode(req);
				if(message instanceof MemoryMessage){
					memMsg = (MemoryMessage)message;
				}
				System.out.println(memMsg);
			} 
			
		}catch (IOException ioe){
			System.out.println("In MemoryDynamicLine Chart. Error handling client:"+ioe.getMessage());
		}  finally {
			System.out.println("************Client closing connection!*************");
			client.disconnectToServer();
		}
		
		addFreeObservation((float)memMsg.getFreeMemory()/1000000);
		addTotalObservation((float)memMsg.getTotalMemory()/1000000);
		addMemoryUsageObservation(100*memMsg.getMemoryUsage());
	}
//	@SuppressWarnings("serial")
//	class DataGenerator extends Timer implements ActionListener{
//
//		DataGenerator(int interval){
//			super(interval,null);
//			addActionListener(this);
//		}
//		
////		DataGenerator(int interval, String hostIP){
////			super(interval,null);
////			this.hostIP = hostIP;
////			addActionListener(this);
////		}
//		
//		public void actionPerformed(ActionEvent e){
//			
//			TCPClient cli;
//			try {
//				cli = new TCPClient(MemoryDynamicLineChart.this.hostIP,9000);
//				System.out.println("************Client connected!*************");
//				firstCommunication(cli.getClient());
//				subsequentCommunication(cli);
//			} catch (Exception e1) {
//				System.out.println("give up data transmit!");
//			}
//		}
//	}
}
