package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;

public class QueryService {
	
	public void handleRequest(Message msg,DataTransmission transmit) throws IOException{
		if(msg!=null){
			String label = msg.getLabel();
			if(label.equals("CpuMsg")){
				transmit.transCPUInfo();
			} else if(label.equals("DiskMsg")){
				transmit.transDiskInfo();
			} else if(label.equals("MemoryMsg")){
				transmit.transMemoryInfo();
			} else if(label.equals("NetworkMsg")){
				transmit.transNetworkInfo();
			} else if(label.equals("LoginMsg")){
				transmit.transLoginInfo();
			} else {
				throw new IOException("Message error: Bad message label string!");
			}
		}
		transmit.giveUpTransmit();//¶Ï¿ªÁ¬½Ó
	}
}
