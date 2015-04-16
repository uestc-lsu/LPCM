package org.gsfan.clustermonitor.datatransmission;

public class MemoryMessage extends Message{

	private long totalMemory = 0L;
	private long freeMemory = 0L;
	private float memoryUsage = 0.0f;
	
	public MemoryMessage(){
		super("MemoryMsg");//设置标签为MemoryMsg
	}
	
	public MemoryMessage(long totalMemory, long freeMemory, float memoryUsage){
		super("MemoryMsg");//设置标签为MemoryMsg
		this.totalMemory = totalMemory;
		this.freeMemory = freeMemory;
		this.memoryUsage = memoryUsage;
	}
	
	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public float getMemoryUsage() {
		return memoryUsage;
	}

	public void setMemoryUsage(float memoryUsage) {
		this.memoryUsage = memoryUsage;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return this.label;
	}
	@Override
	public String toString() {
		String str = this.label+Message.DELIMITER+Long.toString(this.totalMemory)+Message.DELIMITER
				+Long.toString(this.freeMemory)+Message.DELIMITER
				+Float.toString(this.memoryUsage);
		return str;
	}
	
//	public static void main(String[] args) {
//		Message msg = new MemoryMessage(20, 20, 20.0f);
//		System.out.println(msg.toString());
////		Message msg = new CpuMessage(20.0f);
////		System.out.println(msg.getLabel());
//	}

}
