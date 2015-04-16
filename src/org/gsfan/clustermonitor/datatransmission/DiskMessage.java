package org.gsfan.clustermonitor.datatransmission;

public class DiskMessage extends Message {

	private long totalSize = 0L;
	private long usedSize = 0L;
	private long availableSize = 0L;
	
	public DiskMessage(){
		super("DiskMsg");//设置标签为MemoryMsg
	}
	
	public DiskMessage(long totalSize, long usedSize, long availableSize){
		super("DiskMsg");//设置标签为MemoryMsg
		this.totalSize = totalSize;
		this.usedSize = usedSize;
		this.availableSize = availableSize;
	}
	
	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public long getUsedSize() {
		return usedSize;
	}

	public void setUsedSize(long usedSize) {
		this.usedSize = usedSize;
	}

	public long getAvailableSize() {
		return availableSize;
	}

	public void setAvailableSize(long availableSize) {
		this.availableSize = availableSize;
	}

	public String getLabel(){
		return this.label;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	public String toString(){
		String str = this.label + Message.DELIMITER 
				+ Long.toString(totalSize)+ Message.DELIMITER + Long.toString(usedSize)
				+ Message.DELIMITER + Long.toString(availableSize);
		return str;
	}
}