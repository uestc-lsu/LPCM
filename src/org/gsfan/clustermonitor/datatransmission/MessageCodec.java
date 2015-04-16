package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;

public interface MessageCodec {
	public static final String CHARSETNAME = "US-ASCII";
	public byte[] messageEncode(Message msg) throws IOException;
	public Message messageDecode(byte[] input) throws IOException;
}