package org.gsfan.clustermonitor.informationcollect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.gsfan.clustermonitor.datatransmission.MemoryMessage;

public class MemoryInfoCollector {

	private static MemoryInfoCollector instance = new MemoryInfoCollector();
	private MemoryMessage memoryMessage = null;
	
	private MemoryInfoCollector(){
		this.memoryMessage = new MemoryMessage();

	}
	
	public MemoryMessage getMemoryMessage(){
		return this.memoryMessage;
	}
	
	public static MemoryInfoCollector getInstance(){
		return instance;
	}
	
	
//	public float getMemoryUsage(){
	public void collectMemoryInfo(){
		Process pro = null;
		Runtime runtime = Runtime.getRuntime();
		try {
			String command = "cat /proc/meminfo";
			pro = runtime.exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line = null;
			int count = 0;
			long freeMemory = 0, totalMemory = 0;
 			while((line=in.readLine()) != null){	
				String[] memInfo = line.split("\\s+");
				if(memInfo[0].startsWith("MemTotal")){
					totalMemory = Long.parseLong(memInfo[1]);
					memoryMessage.setTotalMemory(totalMemory);
				}
				if(memInfo[0].startsWith("MemFree")){

					freeMemory = Long.parseLong(memInfo[1]);
					memoryMessage.setFreeMemory(freeMemory);
				}
				memoryMessage.setMemoryUsage(1- (float)freeMemory/(float)totalMemory);	
				if(++count == 2){
					break;
				}				
			}
			in.close();
			pro.destroy();
		} catch (IOException e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
		}	
	}
	
	public static void main(String[] args) throws InterruptedException {
		MemoryInfoCollector collector = MemoryInfoCollector.getInstance();
		collector.collectMemoryInfo();
		while(true){
			System.out.println(collector.getMemoryMessage().getMemoryUsage());
			Thread.sleep(5000);
		}
	}

}
