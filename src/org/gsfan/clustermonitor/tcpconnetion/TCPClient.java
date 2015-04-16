package org.gsfan.clustermonitor.tcpconnetion;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {
	private String serAddr = null;
	private int conPort = 0;
	private Socket client = null;
//	private PrintStream out = null;
	private DataOutputStream dataOutputStream = null;
	private InputStream input = null;
	private OutputStream output = null;
	
	public TCPClient(String addr, int port) throws IOException{
		conPort = port;
		serAddr = addr ;
		client = new Socket(serAddr,conPort);
		input = client.getInputStream();
		output = client.getOutputStream();
		dataOutputStream = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
	}
	public DataOutputStream getDataOutputStream(){
		return this.dataOutputStream;
	}
	
	public InputStream getInputStream(){
		return this.input;
	}
	
	public OutputStream getOutputStream(){
		return this.output;
	}
	
//	public void connectToServer() throws IOException{
//		
////		out = new PrintStream(client.getOutputStream());
//		String userName = "gsfan";
//		String passwd = "0620631FGS";
//		dataOutputStream.writeBytes(userName);
//		dataOutputStream.writeBytes(passwd);
//		dataOutputStream.flush();
////		out.println(userName);
////		out.println(passwd);		
//	}
	public void disconnectToServer() throws IOException{
//		out.close();
		output.close();
		input.close();
		dataOutputStream.close();
		client.close();
	}
	
	public static final int CANDIDATEID = 888;
	public static void main(String[] args) throws IOException{
		
//		TCPClient cli = new TCPClient("192.168.233.130",9000);
		TCPClient cli = new TCPClient("192.77.108.249",9000);
		System.out.println("************Client connected!*************");
		QueryMsgCoder msgCoder = new QueryMsgTextCoder();
		MessageExtractor msgExt = new ExtractMsgByDelimiter(cli.getInputStream());
		QueryMsg msg = new QueryMsg(false, true, CANDIDATEID, 0);
		
		byte[] encodeMsg = msgCoder.queryMsgSerialization(msg);//序列化消息
		//查询
		System.out.println("Sending Inquery("+ encodeMsg.length + ") bytes:");
		System.out.println(msg);
		msgExt.constructMessage(encodeMsg, cli.getOutputStream());//构造信息
		
		msg.setInquiry(false);
		encodeMsg = msgCoder.queryMsgSerialization(msg);//序列化信息
		System.out.println("Sending Vote ("+ encodeMsg.length + ") bytes:");
//		System.out.println(msg);
		msgExt.constructMessage(encodeMsg, cli.getOutputStream());
		
		//接收查询信息
		encodeMsg = msgExt.nextMessage();//提取信息
		msg = msgCoder.queryMsgDeserialization(encodeMsg);//反序列化信息
		System.out.println("Receive Response ("+ encodeMsg.length + ") bytes:");
		System.out.println(msg);

		msg = msgCoder.queryMsgDeserialization(msgExt.nextMessage());
		System.out.println("Receive Response ("+ encodeMsg.length + ") bytes:");
		System.out.println(msg);
//		cli.connectToServer();
		System.out.println("************Client closing connection!*************");
		cli.disconnectToServer();
	}
}
