package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;
import java.util.Scanner;

public class CpuMsgTextDecode {

	public static Message messageDecode(Scanner scanner) throws IOException{
		float cpuUsage = 0.0f;
		try {
			String cpu = scanner.next();
			cpuUsage = Float.parseFloat(cpu);
		} catch (Exception e) {
			throw new IOException("Parse error...");
		} finally {
			scanner.close();
		}
		return new CpuMessage(cpuUsage);
	}
}
