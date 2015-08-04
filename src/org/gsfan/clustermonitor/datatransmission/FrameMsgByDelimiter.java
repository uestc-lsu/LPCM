package org.gsfan.clustermonitor.datatransmission;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FrameMsgByDelimiter implements MessageFramer{
	
	private InputStream input = null;
	private static final byte DELIMITER = '\n';
	
	public FrameMsgByDelimiter(InputStream input) {
		this.input = input;
	}
	
	public void frameMessage(byte message[], OutputStream out) throws IOException {

		for(byte b : message){
			if(b==DELIMITER){
				throw new IOException("Message contains delimiter!");
			}
		}
		out.write(message);
		out.write(DELIMITER);
		out.flush();
	}
	
	public byte[] nextMessage() throws IOException {	
		ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream();
		int nextByte;
		while((nextByte = input.read())!=DELIMITER){
			if(nextByte==-1) {
				if(messageBuffer.size()==0) {
					return null;
				} else {
					throw new EOFException("Non-empty message without demiliter!");
				}
			}
			messageBuffer.write(nextByte);
		}
		
		return messageBuffer.toByteArray();
	}
}
