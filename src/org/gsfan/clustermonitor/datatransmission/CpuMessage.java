package org.gsfan.clustermonitor.datatransmission;

public class CpuMessage extends Message {
	
	private float cpuUsage = 0f;
	public CpuMessage() {
		super("CpuMsg");//设置标签为MemoryMsg
	}
	public CpuMessage(float cpuUsage) {
		super("CpuMsg");//设置标签为MemoryMsg
		this.cpuUsage = cpuUsage;
	}
	
	public void setCpuUsage(float cpuUsage){
		this.cpuUsage = cpuUsage;
	}
	
	public float getCpuUsage() {
		return this.cpuUsage;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	public String toString(){
		String str = this.label + Message.DELIMITER + Float.toString(cpuUsage);
		return str;
	}
//	public static void main(String[] args) {
//		
//	}

}
