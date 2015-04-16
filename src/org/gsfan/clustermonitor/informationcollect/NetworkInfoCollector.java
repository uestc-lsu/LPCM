package org.gsfan.clustermonitor.informationcollect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.gsfan.clustermonitor.datatransmission.NetworkMessage;


/**
 * 采集网络带宽使用率
 */
public class NetworkInfoCollector{
	
	private static NetworkInfoCollector INSTANCE = new NetworkInfoCollector();
	private NetworkMessage netMessage = null;
//	private final static float TotalBandwidth = 1000;	//网口带宽,Mbps
	private float curRate = 0.0f;//网络带宽,Mbps
	private NetworkInfoCollector(){
		this.netMessage = new NetworkMessage();
	}
	
	public static NetworkInfoCollector getInstance(){
		return INSTANCE;
	}
	
	public NetworkMessage getNetworkMessage() {
		return this.netMessage;
	}
	/**
	 * @Purpose:采集网络带宽使用率
	 * @param args
	 * @return float,网络带宽使用率,小于1
	 */
	public void collectNetworkBandwidth() {
		Process pro = null;
		BufferedReader input = null;
		Runtime runtime = Runtime.getRuntime();
		try {
			String command = "cat /proc/net/dev";
			//第一次采集流量数据
			long startTime = System.currentTimeMillis();
			pro = runtime.exec(command);
			input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line = null;
			long inSize1 = 0, outSize1 = 0;
			while((line=input.readLine()) != null){	
				line = line.trim();
				if(line.startsWith("eth0")){
					String[] temp = line.split("\\s+"); 
					inSize1 = Long.parseLong(temp[1]);	//Receive bytes,单位为Byte
					outSize1 = Long.parseLong(temp[9]);				//Transmit bytes,单位为Byte
					break;
				}				
			}	
			input.close();
			pro.destroy();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
			}
			//第二次采集流量数据
			long endTime = System.currentTimeMillis();
			pro = runtime.exec(command);
			input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			long inSize2 = 0 ,outSize2 = 0;
			while((line=input.readLine()) != null){	
				line = line.trim();
				if(line.startsWith("eth0")){
					String[] temp = line.split("\\s+"); 
					inSize2 = Long.parseLong(temp[1]);	//Receive bytes,单位为Byte
					outSize2 = Long.parseLong(temp[9]);
					break;
				}				
			}
			if(inSize1 != 0 && outSize1 !=0 && inSize2 != 0 && outSize2 !=0){
				float interval = (float)(endTime - startTime)/500;
				//网口传输速度,单位为Mbps
				curRate = (float)(inSize2 - inSize1 + outSize2 - outSize1)/(1024*interval);
//				netUsage = curRate/TotalBandwidth;

			}				
			input.close();
			pro.destroy();
		} catch (IOException e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
		}
		this.netMessage.setCurRate(curRate);
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		NetworkInfoCollector collector = NetworkInfoCollector.getInstance();
		
		while(true){
			collector.collectNetworkBandwidth();
			System.out.println(collector.getNetworkMessage().getCurRate());
			Thread.sleep(1000);
		}
	}
}