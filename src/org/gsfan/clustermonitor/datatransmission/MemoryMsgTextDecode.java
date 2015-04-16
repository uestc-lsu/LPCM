package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;
import java.util.Scanner;

public class MemoryMsgTextDecode {
	public static Message messageDecode(Scanner scanner) throws IOException {
		String token = null;
		long totalMemory = 0L;
		long freeMemory = 0L;
		float memoryUsage = 0.0f;
		try{
			token = scanner.next();
			totalMemory = Long.parseLong(token);
			
			token = scanner.next();
			freeMemory = Long.parseLong(token);
			
			token = scanner.next();
			memoryUsage = Float.parseFloat(token);
		} catch (Exception e) {
			throw new IOException("Parse error...");
		} finally {
			scanner.close();
		}
	
		return new MemoryMessage(totalMemory, freeMemory, memoryUsage);
	}
}
