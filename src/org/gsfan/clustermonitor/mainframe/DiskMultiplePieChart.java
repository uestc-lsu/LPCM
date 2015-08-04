package org.gsfan.clustermonitor.mainframe;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

@SuppressWarnings("serial")
public class DiskMultiplePieChart  extends JPanel {
	
	private LinkedList<PieChart> chartList = null;
	private static int chartCount = 0;
	
	private ChartDataGenerator chartDataGenerator = null;
	
	public DiskMultiplePieChart() {
		super.setLayout(null);
		this.chartList = new LinkedList<PieChart>();
	}
	
	
	public DiskMultiplePieChart(ChartDataGenerator chartDataGenerator) {
		super.setLayout(null);
		this.chartList = new LinkedList<PieChart>();
		this.chartDataGenerator = chartDataGenerator;
	}
	
	
	public ChartDataGenerator getChartDataGenerator() {
		return chartDataGenerator;
	}


	public void setChartDataGenerator(ChartDataGenerator chartDataGenerator) {
		this.chartDataGenerator = chartDataGenerator;
	}


	public LinkedList<PieChart> getChartList() {
		return chartList;
	}


	public void addPieChart(PieChart lineChart){
		chartList.add(lineChart);
		chartCount++;
	}
	
	public void clearPieChart(){
		chartCount = 0;
		chartList.clear();
	}
	
	public void setPerCharPosition(){

		this.removeAll();
		this.repaint();
		
		int count = 0;
		for(ListIterator<PieChart> iter = chartList.listIterator(); iter.hasNext();){
			
			int temp = (int)(count%3);
			int xStartPos = (temp+1)*5 + 400*temp;
			temp = (int)(count/3);
			int yStartPos = (temp+1)*15 + 300*temp;
			PieChart pieChart = iter.next();
			ChartPanel chartPanel = pieChart.getChartPanel();
			chartPanel.setBounds(xStartPos, yStartPos, 400, 300);
			this.add(chartPanel);
			count++;
		}
		count = (int)(chartCount/3);
		this.setPreferredSize(new Dimension(1220, (count+1)*15 + 300*(count+1)));
	}
}