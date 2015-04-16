package org.gsfan.clustermonitor.tcpconnetion;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExtractMsgByDelimiter implements MessageExtractor {
	
	private static final byte DELIMITER = '\n';
	private InputStream input = null;
	
	public ExtractMsgByDelimiter(InputStream input) {
		this.input = input;
	}
	
	public void constructMessage(byte message[], OutputStream out) throws IOException {
		
		for(byte test: message){
			if(test == DELIMITER) {
				throw new IOException("Message contains delimiter!");
			}
		}
		out.write(message);
		out.write(DELIMITER);
		out.flush();
	}
	
	public byte[] nextMessage() throws IOException{
		ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream();
		
		int nextByte = 0;
		
		while( ( nextByte = input.read() ) != DELIMITER) {
			if(nextByte == -1) {
				if( messageBuffer.size() == 0 ) {
					return null;
				} else {
					throw new EOFException("Non-empty message without delimiter!");
				}
			}
			messageBuffer.write(nextByte);
		}
		return messageBuffer.toByteArray();
	}
	public static void main(String[] args) {
		
	}
}