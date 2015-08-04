package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class TCPClient {

	private Socket client = null;
	private InputStream input = null;
	private OutputStream output = null;
	
	public TCPClient(String addr, int port) throws Exception{
		SocketAddress socAddr = new InetSocketAddress(addr, port);
		try {
			client = new Socket();
			client.connect(socAddr, 5);
			try {
				input = client.getInputStream();
				output = client.getOutputStream();
			} catch (IOException e) {
				System.out.println("I/O error!");
			}
		}  catch (IOException e) {
			client.close();
			System.out.println("TCPClient "+e.getMessage());

		}
	}
	
	public InputStream getInputStream(){
		return this.input;
	}
	
	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public OutputStream getOutputStream(){
		return this.output;
	}
	
	public void disconnectToServer(){
		try {
			if(output!=null)
				output.close();
			if(input!=null)
				input.close();
			if(client!=null){
				client.close();
				System.out.println("In disconnectToServer() : ---------client Close------" );
			}
				
		} catch (IOException e) {
			System.out.println("In disconnectToServer()" + e.getMessage());
		}
	}
}
