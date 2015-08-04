package org.gsfan.clustermonitor.informationcollect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.gsfan.clustermonitor.datatransmission.CpuMessage;

public class CpuInfoCollector{
	
	private static CpuInfoCollector instance = new CpuInfoCollector();
	private CpuMessage cpuMessage = null;
	private CpuInfoCollector(){
		this.cpuMessage = new CpuMessage();
	}
	
	public CpuMessage getCpuMessage(){
		return this.cpuMessage;
	}
	public static CpuInfoCollector getInstance(){
		return instance;
	}
	
	public void collectCpuUsage() {
		float cpuUsage = 0;
		Process pro1,pro2;
		Runtime runtime = Runtime.getRuntime();
		try {
			String command = "cat /proc/stat";
			
//			long startTime = System.currentTimeMillis();
			pro1 = runtime.exec(command);
			BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
			String line = null;
			long idleCpuTime1 = 0, totalCpuTime1 = 0;	//�ֱ�Ϊϵͳ��������е�CPUʱ����ܵ�CPUʱ��
			while((line=in1.readLine()) != null){	
				if(line.startsWith("cpu")){
					line = line.trim();
//					log.info(line);
					String[] temp = line.split("\\s+"); 
					idleCpuTime1 = Long.parseLong(temp[4]);
					for(String s : temp){
						if(!s.equals("cpu")){
							totalCpuTime1 += Long.parseLong(s);
						}
					}	
//					log.info("IdleCpuTime: " + idleCpuTime1 + ", " + "TotalCpuTime" + totalCpuTime1);
					break;
				}						
			}	
			in1.close();
			pro1.destroy();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));

			}
			
			pro2 = runtime.exec(command);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
			long idleCpuTime2 = 0, totalCpuTime2 = 0;
			while((line=in2.readLine()) != null){	
				if(line.startsWith("cpu")){
					line = line.trim();
					String[] temp = line.split("\\s+"); 
					idleCpuTime2 = Long.parseLong(temp[4]);
					for(String s : temp){
						if(!s.equals("cpu")){
							totalCpuTime2 += Long.parseLong(s);
						}
					}
//					log.info("IdleCpuTime: " + idleCpuTime2 + ", " + "TotalCpuTime" + totalCpuTime2);
					break;	
				}								
			}
			if(idleCpuTime1 != 0 && totalCpuTime1 !=0 && idleCpuTime2 != 0 && totalCpuTime2 !=0){
				cpuUsage = 1 - (float)(idleCpuTime2 - idleCpuTime1)/(float)(totalCpuTime2 - totalCpuTime1);
				this.cpuMessage.setCpuUsage(cpuUsage);
			}				
			in2.close();
			pro2.destroy();
		} catch (IOException e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
		}	
	}

	public static void main(String[] args) throws InterruptedException {
		CpuInfoCollector collector = CpuInfoCollector.getInstance();
		while(true){
			collector.collectCpuUsage();
			System.out.println(collector.getCpuMessage().getCpuUsage());
			Thread.sleep(5000);		
		}
	}
}