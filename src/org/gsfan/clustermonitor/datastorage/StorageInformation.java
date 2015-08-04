package org.gsfan.clustermonitor.datastorage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.Timer;

import org.gsfan.clustermonitor.datatransmission.CpuMessage;
import org.gsfan.clustermonitor.datatransmission.DiskMessage;
import org.gsfan.clustermonitor.datatransmission.MemoryMessage;
import org.gsfan.clustermonitor.datatransmission.NetworkMessage;
import org.gsfan.clustermonitor.dbconnector.ConfigureFileReader;
import org.gsfan.clustermonitor.informationcollect.CpuInfoCollector;
import org.gsfan.clustermonitor.informationcollect.DiskInfoCollector;
import org.gsfan.clustermonitor.informationcollect.MemoryInfoCollector;
import org.gsfan.clustermonitor.informationcollect.NetworkInfoCollector;

@SuppressWarnings("serial")
public class StorageInformation extends Timer implements ActionListener {
	private ConfigureFileReader reader = ConfigureFileReader.getInstance();
	private String storageMode = null;
	private String filePath = null;
	private String cpuInfoFile = null;
	private String memInfoFile = null;
	private String diskInfoFile = null;
	private String netInfoFile = null;
	
	private static int times = 0;
	
	public StorageInformation(int interval) {
		super(interval, null);
		
		@SuppressWarnings("static-access")
		Hashtable<String, String> confTable = reader.getConfInfoTable();
		String storageConf = confTable.get("storagemode");
		int loc = storageConf.indexOf(':');
		storageMode = storageConf.substring(0, loc);
		if(storageMode.equals("file")) {
			filePath = storageConf.substring(loc+1);
			
			cpuInfoFile = confTable.get("cpuinfofilename");
			memInfoFile = confTable.get("memoryinfofilename");
			diskInfoFile = confTable.get("diskinfofilename");
			netInfoFile = confTable.get("networkinfofilename");
		}
		
		addActionListener(this);
	}
	
	public void writeCpuInformation() {
		CpuInfoCollector collector = CpuInfoCollector.getInstance();
		collector.collectCpuUsage();
		CpuMessage cpuMsg = collector.getCpuMessage();
		File file = new File(this.filePath, cpuInfoFile);
		try {
			RandomAccessFile random = new RandomAccessFile(file, "rw");
			long fileLength = random.length();
			if(fileLength==0) {
				random.writeBytes("Information\tusage\n");
			} else {
				random.seek(fileLength);
			}
//			random.writeBytes(cpuMsg.getLabel()+"\t"+cpuMsg.getCpuUsage()*100+"%\n");
			DecimalFormat df = new DecimalFormat("0.00");
			random.writeBytes("CpuMessage\t"+df.format(cpuMsg.getCpuUsage()*100)+"%\n");
			random.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeMemoryInformation() {
		MemoryInfoCollector collector = MemoryInfoCollector.getInstance();
		collector.collectMemoryInfo();;
		MemoryMessage memMsg = collector.getMemoryMessage();
		File file = new File(this.filePath, memInfoFile);
		try {
			RandomAccessFile random = new RandomAccessFile(file, "rw");
			long fileLength = random.length();
			if(fileLength==0) {
				random.writeBytes("Information\ttotal\tfree\tusage\n");
			} else {
				random.seek(fileLength);
			}
			DecimalFormat df = new DecimalFormat("0.00");
			float total = (float)memMsg.getTotalMemory()/1024/1024;
			float free = (float)memMsg.getFreeMemory()/1024/1024;
			float usage = memMsg.getMemoryUsage()*100;
			random.writeBytes("MemoryMessage\t"+df.format(total)+"GB\t"+df.format(free)+"GB\t"+df.format(usage)+"%\n");
			
			random.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeDiskInformation() {
		DiskInfoCollector collector = DiskInfoCollector.getInstance();
		collector.collectDiskInfo();
		DiskMessage diskMsg = collector.getDiskMessage();
		File file = new File(this.filePath, diskInfoFile);
		try {
			RandomAccessFile random = new RandomAccessFile(file, "rw");
			long fileLength = random.length();
			if(fileLength==0) {
				random.writeBytes("Information\ttotal\tused\toccupied\n");
			} else {
				random.seek(fileLength);
			}
			DecimalFormat df = new DecimalFormat("0.00");
			float total = (float)diskMsg.getTotalSize()/1024/1024;
			float used = (float)diskMsg.getUsedSize()/1024/1024;
			float usage = 100*(float)diskMsg.getUsedSize()/diskMsg.getTotalSize();
			random.writeBytes("DiskMessage\t"+df.format(total)+"GB\t"+df.format(used)+"GB\t"+df.format(usage)+"%\n");
			random.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeNetworkInformation() {
		NetworkInfoCollector collector = NetworkInfoCollector.getInstance();
		collector.collectNetworkBandwidth();
		NetworkMessage netMsg = collector.getNetworkMessage();
		File file = new File(this.filePath, netInfoFile);
		try {
			RandomAccessFile random = new RandomAccessFile(file, "rw");
			long fileLength = random.length();
			if(fileLength==0) {
				random.writeBytes("Information\trate\n");
			} else {
				random.seek(fileLength);
			}
//			random.writeBytes(netMsg.getLabel()+"\t"+netMsg.getCurRate()+"\n");
			DecimalFormat df = new DecimalFormat("0.00");
			random.writeBytes("NetworkMessage\t"+df.format(netMsg.getCurRate())+"Mbps\n");
			random.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent evt) {
		times++;
		writeCpuInformation();
		writeMemoryInformation();
		writeNetworkInformation();
		if(times==100) {
			times=0;
			writeDiskInformation();
		}
	}
	
	public static void main(String args[]) {
//		StorageInformation storage = new StorageInformation(2000);
	}
}
