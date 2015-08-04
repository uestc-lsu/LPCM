package org.gsfan.clustermonitor.mainframe;

import java.awt.BasicStroke;
import java.awt.Color;
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
		
		this.totalMemory = new TimeSeries("总内存");
		this.totalMemory.setMaximumItemAge(maxAge);
		this.freeMemory = new TimeSeries("剩余内存");
		this.freeMemory.setMaximumItemAge(maxAge);
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(totalMemory);
		dataset.addSeries(freeMemory);

		DateAxis domain = new DateAxis("时间");
		NumberAxis range = new NumberAxis("内存使用情况/GB");
		
		domain.setTickLabelFont(this.tickFont);
		range.setTickUnit(new NumberTickUnit(0.3f));//设置刻度
		
		range.setTickLabelFont(this.tickFont);
		domain.setLabelFont(this.labelFont);
		range.setLabelFont(this.labelFont);
		
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
		renderer.setSeriesPaint(0, Color.red);
		renderer.setSeriesPaint(1, Color.blue);
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
		chart = new JFreeChart(this.titleName, this.titleFont, plot, true);
		
		chart.getLegend().setItemFont(this.legendFont);	//设置图例字体
		
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

		DecimalFormat format = new DecimalFormat("##0.00");
		String usage = format.format(y);
		chart.setTitle(this.titleName + "("+ usage +"%)");	
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
			System.out.println("In " + this + "firstCommunication error!");
		}

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
}
