package org.gsfan.clustermonitor.mainfram;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

@SuppressWarnings("serial")
public class MultiDynamicLineChart extends JPanel implements MultiChart {
	
	private LinkedList<DynamicLineChart> chartList = null;
	private static int chartCount = 0;
	
	private ChartDataGenerator chartDataGenerator = null;
	
	public MultiDynamicLineChart() {
		super.setLayout(null);
		this.chartList = new LinkedList<DynamicLineChart>();
		//…Ë÷√±≥æ∞—’…´
//		this.setBackground(Color.WHITE);
	}

	public MultiDynamicLineChart(ChartDataGenerator chartDataGenerator) {
		super.setLayout(null);
		this.chartList = new LinkedList<DynamicLineChart>();
		this.chartDataGenerator = chartDataGenerator;
		//…Ë÷√±≥æ∞—’…´
//		this.setBackground(Color.WHITE);
	}
	
	public LinkedList<DynamicLineChart> getChartList() {
		return chartList;
	}

	public ChartDataGenerator getChartDataGenerator() {
		return chartDataGenerator;
	}

	public void setChartDataGenerator(ChartDataGenerator chartDataGenerator) {
		this.chartDataGenerator = chartDataGenerator;
	}

	public void addDynamicLineChart(DynamicLineChart lineChart){
		chartList.add(lineChart);
		chartCount++;
	}
	
	public void removeDynamicLineChart(DynamicLineChart lineChart){
		chartList.remove(lineChart);
		chartCount--;
	}
	
	public void clearDynamicLineChart() {
		chartCount = 0;
		chartList.clear();
	}
	
	public void setPerCharPosition(){

		this.removeAll();
		this.repaint();
		
		int count = 0;
		for(ListIterator<DynamicLineChart> iter = chartList.listIterator(); iter.hasNext();){
			
			int temp = (int)(count%3);
			int xStartPos = (temp+1)*5 + 400*temp;
			temp = (int)(count/3);
			int yStartPos = (temp+1)*15 + 300*temp;
			DynamicLineChart dynamicLineChart = iter.next();
			ChartPanel chartPanel = dynamicLineChart.getChartPanel();
			chartPanel.setBounds(xStartPos, yStartPos, 400, 300);
			this.add(chartPanel);
			count++;
		}
		count = (int)(chartCount/3);
		this.setPreferredSize(new Dimension(1220, (count+1)*15 + 300*(count+1)));
	}
}
