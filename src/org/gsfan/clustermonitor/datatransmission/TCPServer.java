package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.gsfan.clustermonitor.informationcollect.CpuInfoCollector;
import org.gsfan.clustermonitor.informationcollect.DiskInfoCollector;

public class TCPServer{
	
	private ServerSocket server = null;
	private Socket client = null ;
	
	public TCPServer(int port)  {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	public Socket getClient() throws IOException{
		client = server.accept();
		return client;
	}
	
	public void disconnection() throws IOException{
		client.close();
		server.close();
	}
	
	public static void main(String[] args) throws IOException{
		TCPServer ser = new TCPServer(9000);
		
		while(true){
			Socket client = ser.getClient();
			
			System.out.println("Handling client at "+client.getRemoteSocketAddress());
			
			MessageCodec codec = new MessageTextCodec();
			MessageFramer framer = new FrameMsgByDelimiter(client.getInputStream());
			
			DiskInfoCollector collector = DiskInfoCollector.getInstance();
			collector.collectDiskInfo();
			DiskMessage diskMsg = collector.getDiskMessage();
			
			byte[] encodeMsg = codec.messageEncode(diskMsg);
			
			System.out.println("Sending disk message ("+ encodeMsg.length + ") bytes:");
			System.out.println(diskMsg);
			framer.frameMessage(encodeMsg, client.getOutputStream());
			
			CpuInfoCollector collector2 = CpuInfoCollector.getInstance();
			collector2.collectCpuUsage();
		
			CpuMessage cpuMsg = collector2.getCpuMessage();
			encodeMsg = codec.messageEncode(cpuMsg);
			System.out.println("Sending CPU message ("+ encodeMsg.length + ") bytes:");
			System.out.println(cpuMsg);
			framer.frameMessage(encodeMsg, client.getOutputStream());
			
			client.close();
		}
	}
}