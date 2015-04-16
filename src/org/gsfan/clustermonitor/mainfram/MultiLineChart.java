package org.gsfan.clustermonitor.mainfram;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class MultiLineChart {
	
//	private XYSeriesCollection[] brokenLineDataset = null;
//	private JFreeChart[] chart = null;
	private JScrollPane scrollPanel = null;
	private JPanel panel = null;
//	private ChartPanel[] chartPanel = null;
//	private int subLineChartNum = 0;
	
	@SuppressWarnings("deprecation")
	public MultiLineChart(XYSeriesCollection[] brokenLineDataset){
		
//		scrollPanel = new JScrollPane();
//		scrollPanel.setLayout(new ScrollPaneLayout());
		panel = new JPanel();
		panel.setLayout(null);
//		panel.setLayout(new FlowLayout());
//		scrollPanel = new JScrollPane(panel);

		for(int i=0; i<brokenLineDataset.length; i++){
//			this.brokenLineDataset[i] = new XYSeriesCollection();
//			this.brokenLineDataset[i] = brokenLineDataset[i];
			JFreeChart chart = ChartFactory.createXYLineChart(
					i+"号机", // chart title
					"X/时间", // x axis label
					"Y/进度", // y axis label
					brokenLineDataset[i], // data
					PlotOrientation.VERTICAL,
					true, // include legend
					true, // tooltips
					false // urls
			);
			
			Font font = new Font("宋体",Font.PLAIN,14);
			chart.getTitle().setFont(font);
			chart.getLegend().setItemFont(font);
			
			XYPlot xyplot = chart.getXYPlot();
			xyplot.setOutlineStroke(new BasicStroke(1.5f));//加粗边框
			//设置坐标轴与显示区的间距
			xyplot.setAxisOffset(new RectangleInsets(0d, 0d, 0d, 5d));
			
			ValueAxis domainAxis = xyplot.getDomainAxis();//获取X轴
			domainAxis.setLabelFont(font);
			domainAxis.setAutoRange(false);
//			domainAxis.setAxisLineStroke(new BasicStroke(2.0f));
			//设置坐标轴区间范围
//			domainAxis.setRange(new Range(0.5, 4.0));
			
			ValueAxis rangeAxis = xyplot.getRangeAxis();//获取Y轴
			rangeAxis.setLabelFont(font);
			
			
			rangeAxis.setTickMarkOutsideLength(5.2f);
			rangeAxis.setTickMarkInsideLength(5.2f);
			
			//设置折线粗细
			XYLineAndShapeRenderer lineAndShapeRenderer = (XYLineAndShapeRenderer)xyplot.getRenderer();
			lineAndShapeRenderer.setStroke(new BasicStroke(2.0f));
			
			ChartPanel chartPanel = new ChartPanel(chart);
			int temp=(int)(i%4);
			int xStartPosition = (temp+1)*10 + 300*temp;
			temp=(int)(i/4);
			int yStartPosition = (temp+1)*20 + 215*temp;
			chartPanel.setBounds(xStartPosition, yStartPosition, 300, 215);
//			chartPanel.setSize(300,215);
			
			panel.add(chartPanel);
//			scrollPanel.add(chartPanel);
		}
		//JScrollPane是根据里面的子控件的preferredSize来确定是否显示滚动条的。
		panel.setPreferredSize(new Dimension(1200,800));
		
//		JFrame myFrame = new JFrame();
//
//		myFrame.getContentPane().add(panel);
////		myFrame.getContentPane().add(scrollPanel);
//		myFrame.setSize(600, 400);
////		myFrame.pack();
//		myFrame.setVisible(true);

	}
	public static XYSeriesCollection[] creatDataset(int num){
		XYSeries series1 = new XYSeries("核1");
		series1.add(1.0, 1.0);
		series1.add(2.0, 4.0);
		series1.add(3.0, 3.0);
		series1.add(4.0, 5.0);
		series1.add(5.0, 5.0);
		series1.add(6.0, 7.0);
		series1.add(7.0, 7.0);
		series1.add(8.0, 8.0);
		XYSeries series2 = new XYSeries("核2");
		series2.add(1.0, 3.0);
		series2.add(2.0, 4.0);
		series2.add(3.0, 1.0);
		series2.add(4.0, 2.0);
		series2.add(5.0, 4.0);
		series2.add(6.0, 4.0);
		series2.add(7.0, 2.0);
		series2.add(8.0, 1.0);
		
		XYSeriesCollection[] dataset = new XYSeriesCollection[num];
//		XYSeriesCollection[] dataset = null;
		for(int i=0; i<num; i++){
			dataset[i] = new XYSeriesCollection();
			dataset[i].addSeries(series1);
			dataset[i].addSeries(series2);
		}
		
		return dataset;
	}
	
	public JPanel getChartPanel(){
		return panel;
	}
	public JScrollPane getChartScrollPanel(){
		return scrollPanel;
	}
	
//	public static void main(String[] args) {
//		
//		new MultiLineChart(MultiLineChart.creatDataset(8));
//	}
}