package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;
import java.util.Scanner;

public class DiskMsgTextDecode {
	public static Message messageDecode(Scanner scanner) throws IOException {
		long totalSize = 0L;
		long usedSize = 0L;
		long availableSize = 0L;
		
		try {
			String temp = scanner.next();
			totalSize = Long.parseLong(temp);
			temp = scanner.next();
			usedSize = Long.parseLong(temp);
			temp = scanner.next();
			availableSize = Long.parseLong(temp);
		} catch (Exception e) {
			throw new IOException("Parse error...");
		} finally {
			scanner.close();
		}
		return new DiskMessage(totalSize, usedSize, availableSize);
	}
}
