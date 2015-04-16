package org.gsfan.clustermonitor.datatransmission;

public class NetworkMessage extends Message{
	private float curRate = 0.0f;
	
	public NetworkMessage(){
		super("NetworkMsg");//设置标签为MemoryMsg
	}
	
	public NetworkMessage(float curRate){
		super("NetworkMsg");//设置标签为MemoryMsg
		this.curRate = curRate;
	}
	
	public float getCurRate() {
		return curRate;
	}

	public void setCurRate(float curRate) {
		this.curRate = curRate;
	}

	public void setLabel(String label){
		this.label = label;
	}
	public String getLabel(){
		return this.label;
	}
	
	public String toString(){
		String str = this.label + Message.DELIMITER + Float.toString(curRate);
		return str;
	}
}
