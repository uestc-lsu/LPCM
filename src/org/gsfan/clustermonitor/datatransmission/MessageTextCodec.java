package org.gsfan.clustermonitor.datatransmission;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MessageTextCodec implements MessageCodec{
	
	public byte[] messageEncode(Message msg) throws IOException {
		String msgStr = null;
		if(msg.getLabel().equals("CpuMsg")){
			CpuMessage cpuMsg = (CpuMessage)msg;
			msgStr = cpuMsg.toString();
		} else if(msg.getLabel().equals("DiskMsg")){
			DiskMessage diskMsg = (DiskMessage)msg;
			msgStr = diskMsg.toString();
		} else if(msg.getLabel().equals("MemoryMsg")){
			MemoryMessage memoryMsg = (MemoryMessage)msg;
			msgStr = memoryMsg.toString();
		} else if(msg.getLabel().equals("NetworkMsg")){
			NetworkMessage netMsg = (NetworkMessage)msg;
			msgStr = netMsg.toString();
		} else if(msg.getLabel().equals("LoginMsg")) {
			LoginMessage loginMsg = (LoginMessage)msg;
			msgStr = loginMsg.toString();
		} else {
			throw new IOException("Message error: Bad message label string!");
		}
		return msgStr.getBytes(CHARSETNAME);
	}
	
	public Message messageDecode(byte[] input) throws IOException {
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
		Scanner scanner = new Scanner(new InputStreamReader(inputStream,CHARSETNAME));
		
		String token = scanner.next();
		if(token.equals("CpuMsg")){
			return CpuMsgTextDecode.messageDecode(scanner);
		} else if(token.equals("DiskMsg")){
			return DiskMsgTextDecode.messageDecode(scanner);
		} else if(token.equals("MemoryMsg")){
			return MemoryMsgTextDecode.messageDecode(scanner);
		} else if(token.equals("NetworkMsg")){
			return NetworkMsgTextDecode.messageDecode(scanner);
		} else {
			scanner.close();
			throw new IOException("Message error: Bad message label string!");
		}		
	}
}

//public class MessageTextCodec implements MessageCodec{
//
//	private CpuMessage cpuMsg = null;
//	private MemoryMessage memoryMsg = null;
//	private DiskMessage diskMsg = null;
//	private NetworkMessage netMsg = null;
//	private LoginMessage loginMsg = null;
//	
//	public byte[] messageEncode(Message msg) throws IOException {
//		String msgStr = null;
//		if(msg.getLabel().equals("CpuMsg")){
//			this.cpuMsg = (CpuMessage)msg;
//			msgStr = this.cpuMsg.toString();
//		} else if(msg.getLabel().equals("DiskMsg")){
//			this.diskMsg = (DiskMessage)msg;
//			msgStr = this.diskMsg.toString();
//		} else if(msg.getLabel().equals("MemoryMsg")){
//			this.memoryMsg = (MemoryMessage)msg;
//			msgStr = this.memoryMsg.toString();
//		} else if(msg.getLabel().equals("NetworkMsg")){
//			this.netMsg = (NetworkMessage)msg;
//			msgStr = this.netMsg.toString();
//		} else if(msg.getLabel().equals("LoginMsg")) {
//			this.loginMsg = (LoginMessage)msg;
//			msgStr = this.loginMsg.toString();
//		} else {
//			throw new IOException("Message error: Bad message label string!");
//		}
//		return msgStr.getBytes(CHARSETNAME);
//	}
//	
//	public Message messageDecode(byte[] input) throws IOException {
//		
//		ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
//		Scanner scanner = new Scanner(new InputStreamReader(inputStream,CHARSETNAME));
//		
//		String token = scanner.next();
//		if(token.equals("CpuMsg")){
//			return CpuMsgTextDecode.messageDecode(scanner);
//		} else if(token.equals("DiskMsg")){
//			return DiskMsgTextDecode.messageDecode(scanner);
//		} else if(token.equals("MemoryMsg")){
//			return MemoryMsgTextDecode.messageDecode(scanner);
//		} else if(token.equals("NetworkMsg")){
//			return NetworkMsgTextDecode.messageDecode(scanner);
//		} else {
//			scanner.close();
//			throw new IOException("Message error: Bad message label string!");
//		}		
//	}
//}