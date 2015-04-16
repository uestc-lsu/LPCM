package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;
import java.io.OutputStream;

public interface MessageFramer {
	public void frameMessage(byte message[], OutputStream out) throws IOException;
	public byte[] nextMessage() throws IOException;
}
