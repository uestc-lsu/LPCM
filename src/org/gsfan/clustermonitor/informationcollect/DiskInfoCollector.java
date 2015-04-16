package org.gsfan.clustermonitor.informationcollect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.gsfan.clustermonitor.datatransmission.DiskMessage;

public class DiskInfoCollector {
	
	private static DiskInfoCollector instance = new DiskInfoCollector();
	
	private DiskMessage diskMessage = null;
	
	private DiskInfoCollector(){
		this.diskMessage = new DiskMessage();
	}
	
	public static DiskInfoCollector getInstance(){
		return instance;
	}
	
	public DiskMessage getDiskMessage(){
		return this.diskMessage;
	}
//	public long[] getDiskInfo(){
	public void collectDiskInfo(){
//		long[] result = new long[3];
		String command = new String("df -k");
		//Every Java Application has a single instance of class Runtime
		Runtime runtime = Runtime.getRuntime();
		Process pro;
		long totalSize = 0L, usedSize = 0L , availableSize = 0L;
		try {
			//Executes the specified string command in a separate process. 
			pro = runtime.exec(command);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line = reader.readLine();//第一行作废
			while((line = reader.readLine())!=null){
				String[] temp = line.split("\\s+");
				int startFlag = 1;
				if(temp.length<6){//防止Filesystem这项名称过长导致的出现分行情况
					startFlag=0;
					if((line = reader.readLine())!=null){
						line = line.trim();
						temp = line.split("\\s+");
					}
				}
				totalSize += Long.parseLong(temp[startFlag++]);
				usedSize += Long.parseLong(temp[startFlag++]);
				availableSize += Long.parseLong(temp[startFlag]);
			}
//			result[0] = totalSize;
//			result[1] = usedSize;
//			result[2] = availableSize;
			diskMessage.setTotalSize(totalSize);
			diskMessage.setUsedSize(usedSize);
			diskMessage.setAvailableSize(availableSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		return result;
	}
	public static void main(String[] args) {
		DiskInfoCollector collector = DiskInfoCollector.getInstance();
		collector.collectDiskInfo();
		DiskMessage message = collector.getDiskMessage();
		System.out.println("total size = "+ ((float)message.getTotalSize()/(1024*1024)) +"GB\nused size = "+ (message.getUsedSize()>>20) +"GB\navailable size = "+ (message.getAvailableSize()>>20)+"GB");
	}
}