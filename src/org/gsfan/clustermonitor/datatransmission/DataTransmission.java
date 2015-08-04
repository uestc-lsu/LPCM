package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;
import java.net.Socket;

import org.gsfan.clustermonitor.datastorage.StorageInformation;
import org.gsfan.clustermonitor.dbconnector.ConfigureFileReader;
import org.gsfan.clustermonitor.informationcollect.CpuInfoCollector;
import org.gsfan.clustermonitor.informationcollect.DiskInfoCollector;
import org.gsfan.clustermonitor.informationcollect.MemoryInfoCollector;
import org.gsfan.clustermonitor.informationcollect.NetworkInfoCollector;


public class DataTransmission {
	
	private MessageCodec codec = null;
	private MessageFramer framer = null;
	private Socket client =null;
	private TCPServer server = null;

	private DataTransmission(){
		this.server = new TCPServer(9000);
	}
	
	public Socket getClient() {
		return this.client;
	}
	
	public void startTransmit(){
		try {
			client = this.server.getClient();
			this.codec = new MessageTextCodec();
			this.framer = new FrameMsgByDelimiter(client.getInputStream());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public MessageCodec getCodec() {
		return codec;
	}

	public void setCodec(MessageCodec codec) {
		this.codec = codec;
	}

	public MessageFramer getFramer() {
		return framer;
	}

	public void setFramer(MessageFramer framer) {
		this.framer = framer;
	}

	public void transLoginInfo() throws IOException{

		LoginMessage loginMsg = new LoginMessage();
		byte[] encodeMsg;
		try {
			encodeMsg = codec.messageEncode(loginMsg);
			System.out.println("Sending login message ("+ encodeMsg.length + ") bytes:");
			System.out.println(loginMsg);
			framer.frameMessage(encodeMsg, client.getOutputStream());
		} catch (IOException e) {
			throw new IOException("Transfer longin information error!");
		}

	}
	
	public void transCPUInfo() throws IOException{
		CpuInfoCollector collector = CpuInfoCollector.getInstance();
		collector.collectCpuUsage();
		CpuMessage cpuMsg = collector.getCpuMessage();
		byte[] encodeMsg;
		try {
			encodeMsg = codec.messageEncode(cpuMsg);
			System.out.println("Sending CPU message ("+ encodeMsg.length + ") bytes:");
			System.out.println(cpuMsg);
			framer.frameMessage(encodeMsg, client.getOutputStream());
		} catch (IOException e) {
			throw new IOException("Transfer CPU information error!");
		}

	}
	
	public void transDiskInfo() throws IOException{
		DiskInfoCollector collector = DiskInfoCollector.getInstance();
		collector.collectDiskInfo();
		DiskMessage diskMsg = collector.getDiskMessage();
		byte[] encodeMsg;
		try {
			encodeMsg = codec.messageEncode(diskMsg);
			System.out.println("Sending disk message ("+ encodeMsg.length + ") bytes:");
			System.out.println(diskMsg);
			framer.frameMessage(encodeMsg, client.getOutputStream());
		} catch (IOException e) {
			throw new IOException("Transfer Disk information error!");
		}

	}
	
	public void transNetworkInfo() throws IOException{
		NetworkInfoCollector collector = NetworkInfoCollector.getInstance();
		collector.collectNetworkBandwidth();
		NetworkMessage netMsg = collector.getNetworkMessage();
		byte[] encodeMsg;
		try {
			encodeMsg = codec.messageEncode(netMsg);
			System.out.println("Sending network message ("+ encodeMsg.length + ") bytes:");
			System.out.println(netMsg);
			framer.frameMessage(encodeMsg, client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Transfer network information error!");
		}

	}
	
	public void transMemoryInfo() throws IOException{
		MemoryInfoCollector collector = MemoryInfoCollector.getInstance();
		collector.collectMemoryInfo();
		MemoryMessage memMsg = collector.getMemoryMessage();
		byte[] encodeMsg;
		try {
			encodeMsg = codec.messageEncode(memMsg);
			System.out.println("Sending memory message ("+ encodeMsg.length + ") bytes:");
			System.out.println(memMsg);
			framer.frameMessage(encodeMsg, client.getOutputStream());
		} catch (IOException e) {
			throw new IOException("Transfer memory information error!");
		}

	}
	public void giveUpTransmit(){
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		DataTransmission transmit = new DataTransmission();
		
		ConfigureFileReader reader = ConfigureFileReader.getInstance();
		@SuppressWarnings("static-access")
		String time = reader.getConfInfoTable().get("storagetimeinterval");
		int loc = time.indexOf('s');
		int interval = new Integer(time.substring(0, loc-1));
		StorageInformation storage = new StorageInformation(interval*1000);
		storage.start();	//每隔20s向文件中写一次信息
		while(true){
			transmit.startTransmit();
			new DataTransmissionThread(transmit).run();
		}
	}

}
