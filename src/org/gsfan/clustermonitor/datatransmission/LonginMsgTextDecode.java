package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;
import java.util.Scanner;

public class LonginMsgTextDecode {
	public static Message messageDecode(Scanner scanner) throws IOException {
		String username = null;
		String passwd = null;
		String userType = null;
		
		try {
			username = scanner.next();
			passwd = scanner.next();
			userType = scanner.next();
		} catch (Exception e) {
			throw new IOException("Parse error...");
		} finally {
			scanner.close();
		}
		return new LoginMessage(username, passwd, userType);
	}
}
