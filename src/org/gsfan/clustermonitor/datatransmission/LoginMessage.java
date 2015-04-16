package org.gsfan.clustermonitor.datatransmission;

public class LoginMessage extends Message{
	
	private String username = null;
	private String passwd = null;
	private String userType = null;
	public LoginMessage() {
		super("LoginMsg");//设置标签为MemoryMsg
	}
	public LoginMessage(String username, String passwd) {
		super("LoginMsg");//设置标签为MemoryMsg
		this.username = username;
		this.passwd = passwd;
		this.userType = "NormalUser";
	}
	
	public LoginMessage(String username, String passwd, String userType) {
		super("LoginMsg");//设置标签为MemoryMsg
		this.username = username;
		this.passwd = passwd;
		this.userType = userType;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	public String toString(){
		String str = this.label + Message.DELIMITER + username + Message.DELIMITER 
				+ passwd + Message.DELIMITER + userType;
		return str;
	}
}
