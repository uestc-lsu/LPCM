package org.gsfan.clustermonitor.datatransmission;

public abstract class Message {
	public static final String DELIMITER = " ";
//	protected String delimiter = " ";
	protected String label = null;
//	public Message(){}
	public Message(String label){
		this.label = label;
	}
	public abstract String getLabel();
	public abstract void setLabel(String label);
	public String toString(){
		String str = this.label;
		return str;
	}
}