package org.gsfan.clustermonitor.datatransmission;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FrameMsgByLength implements MessageFramer{
	
	private DataInputStream input = null;
	private static final int MAXMESSAGELENGTH = 65535;
	private static final int BYTEMASK = 0xff;
	private static final int BYTESHIFT = 8;
	
	public FrameMsgByLength(InputStream input) {
		this.input = new DataInputStream(input);
	}
	
	public void frameMessage(byte message[], OutputStream out) throws IOException {
		if( message.length > MAXMESSAGELENGTH) {
			throw new IOException("Message is to long!");
		}
		//void write(int b) 将参数 b 的八个低位写入输出流。 
		out.write( (message.length >> BYTESHIFT) & BYTEMASK);	//先写高8位
		out.write( message.length & BYTEMASK );			//再写低8位
		//
		out.write(message);
		out.flush();
	}
	
	public byte[] nextMessage() throws IOException {	
		int msgLength = 0;
		try {
			msgLength = input.readUnsignedShort();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		byte[] msg = new byte[msgLength];
		input.readFully(msg);
		return msg;
	}
}