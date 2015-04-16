package org.gsfan.clustermonitor.mainfram;

import java.awt.BasicStroke;
import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class LineChart {
	
	@SuppressWarnings("unused")
	private XYSeriesCollection brokenLineDataset = null;
	private JFreeChart chart = null;
	private JPanel chartPanel = null;
	
	@SuppressWarnings("deprecation")
	public LineChart(XYSeriesCollection brokenLineDataset){
		
		this.brokenLineDataset = brokenLineDataset;
//		this.brokenLineDataset = creatDataset();
		chart = ChartFactory.createXYLineChart(
				"曲线图示例", // chart title
				"X/时间", // x axis label
				"Y/进度", // y axis label
				brokenLineDataset, // data
				PlotOrientation.VERTICAL,
				true, // include legend
				true, // tooltips
				false // urls
		);
		
		JFreeChart chart2 = ChartFactory.createXYLineChart(
				"曲线图示例", // chart title
				"X/时间", // x axis label
				"Y/进度", // y axis label
				brokenLineDataset, // data
				PlotOrientation.VERTICAL,
				true, // include legend
				true, // tooltips
				false // urls
		);
		
		Font font = new Font("宋体",Font.PLAIN,14);
		chart.getTitle().setFont(font);
		chart.getLegend().setItemFont(font);
		
		XYPlot xyPlot = chart.getXYPlot();
		xyPlot.setOutlineStroke(new BasicStroke(1.5f));//加粗边框
		//设置坐标轴与显示区的间距
		xyPlot.setAxisOffset(new RectangleInsets(0d, 0d, 0d, 5d));
		
		xyPlot.setDomainGridlinesVisible(true);// x轴分类轴网格是否可见
		xyPlot.setRangeGridlinesVisible(true);// y轴数据轴网格是否可见
		
		/*第二张表*/
		XYPlot xyPlot2 = chart2.getXYPlot();
		xyPlot2.setDomainGridlinesVisible(true);// x轴分类轴网格是否可见
		xyPlot2.setRangeGridlinesVisible(true);// y轴数据轴网格是否可见
		
		xyPlot.setDomainGridlineStroke(new BasicStroke(2.0f));
//		AxisSpace as = new AxisSpace();
//		as.setLeft(5.0d);
//		xyPlot.setFixedRangeAxisSpace(as);//导致图像右移
//		xyPlot.setRangeCrosshairStroke(new BasicStroke(20.0f));

		XYLineAndShapeRenderer lineAndShapeRenderer = (XYLineAndShapeRenderer)xyPlot.getRenderer();

		//设置坐标轴轴字体
		ValueAxis domainAxis = xyPlot.getDomainAxis();//获取X轴
		domainAxis.setLabelFont(font);
		domainAxis.setAutoRange(false);
//		domainAxis.setAxisLineStroke(new BasicStroke(2.0f));
//		domainAxis.setLowerMargin(0.1d);
		
		domainAxis.setLowerMargin(0.00d);
		domainAxis.setUpperMargin(0.00d);
		domainAxis.setRange(new Range(0.5, 4.0));
//		domainAxis.setTickMarkOutsideLength(2.0f);
//		domainAxis.setTickMarkInsideLength(2.0f);

		
//		if (domainAxis instanceof NumberAxis) {
//		      NumberAxis numberAxis = (NumberAxis) domainAxis;
////		      numberAxis.setTickUnit(new NumberTickUnit(0.4d));//间距0.4  
////		      numberAxis.setAxisLineStroke(new BasicStroke(5.0f));
////		      numberAxis.setLabelAngle(20.0d);//设置坐标单位标注的旋转角度
////		      numberAxis.setUpperBound(5d);//设置坐标上限
////		      numberAxis.setUpperMargin(0.3d);
//		          numberAxis.setTickMarkInsideLength(1.6f);
////		      numberAxis.setNumberFormatOverride(new DecimalFormat("00"));//整數
//		    }
		domainAxis.setTickMarkInsideLength(0.01f);

		
		ValueAxis rangeAxis = xyPlot.getRangeAxis();//获取Y轴
		rangeAxis.setLabelFont(font);
		
		
		rangeAxis.setTickMarkOutsideLength(5.2f);
		rangeAxis.setTickMarkInsideLength(5.2f);
		//设置折线粗细
		lineAndShapeRenderer.setStroke(new BasicStroke(2.0f));

//		ChartFrame frame = new ChartFrame("First", chart);
//		//ChartFrame frame2 = new ChartFrame("Second", chart2);
//		
//		frame.pack();
//		frame.setVisible(true);
//		//frame2.setVisible(true);
		
		ChartPanel chartPanel1 = new ChartPanel(chart);
		chartPanel1.setBounds(0, 20, 300, 215);
		ChartPanel chartPanel2 = new ChartPanel(chart2);
		chartPanel2.setBounds(310, 20, 300, 215);
		
		chartPanel = new JPanel();
		chartPanel.setLayout(null);
		chartPanel.add(chartPanel1);
		chartPanel.add(chartPanel2);

		chartPanel.setSize(800, 600);
//		JFrame myFrame = new JFrame();
//		myFrame.add(chartPanel1);
//		myFrame.add(chartPanel2);
//		myFrame.getContentPane().add(chartPanel1);
//		myFrame.getContentPane().add(chartPanel2);
//		myFrame.getContentPane().add(chartPanel);
//		myFrame.setBounds(200, 200, 700, 700);
//		myFrame.pack();
//		myFrame.setVisible(true);

	}
	public static XYSeriesCollection creatDataset(){
		XYSeries series1 = new XYSeries("线一");
		series1.add(1.0, 1.0);
		series1.add(2.0, 4.0);
		series1.add(3.0, 3.0);
		series1.add(4.0, 5.0);
		series1.add(5.0, 5.0);
		series1.add(6.0, 7.0);
		series1.add(7.0, 7.0);
		series1.add(8.0, 8.0);
		XYSeries series2 = new XYSeries("线二");
		series2.add(1.0, 3.0);
		series2.add(2.0, 4.0);
		series2.add(3.0, 1.0);
		series2.add(4.0, 2.0);
		series2.add(5.0, 4.0);
		series2.add(6.0, 4.0);
		series2.add(7.0, 2.0);
		series2.add(8.0, 1.0);
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		
		return dataset;
	}
	
	public JPanel  getChartPanel(){
		return chartPanel;
	}
	
//	public static void main(String[] args) {
//		
//		new LineChart(creatDataset());
//	}
}
