package org.gsfan.clustermonitor.tcpconnetion;

import java.io.IOException;
import java.io.OutputStream;

public interface MessageExtractor {
	public void constructMessage(byte message[], OutputStream out) throws IOException;
	public byte[] nextMessage() throws IOException;
}