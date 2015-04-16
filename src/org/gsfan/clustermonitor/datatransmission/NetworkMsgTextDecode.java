package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;
import java.util.Scanner;

public class NetworkMsgTextDecode {
	public static Message messageDecode(Scanner scanner) throws IOException {
		float curRate = 0.0f;
		
		try {
			String temp = scanner.next();
			curRate = Float.parseFloat(temp);
		} catch (Exception e) {
			throw new IOException("Parse error...");
		} finally {
			scanner.close();
		}
		return new NetworkMessage(curRate);
	}
}
