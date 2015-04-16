package org.gsfan.clustermonitor.tcpconnetion;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer{
	
	private int listenPort = 0;
	
	private ServerSocket server = null;
	private Socket client = null ;
	
//	private InputStream input = null;
	private BufferedReader reader = null;
	private DataInputStream dataInput = null;
	
	public TCPServer(int port)  {
		this.listenPort = port;
		try {
			server = new ServerSocket(listenPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	public Socket getClient() throws IOException{
		client = server.accept();
		return client;
	}
	public boolean establishConnection() throws IOException{
		
		client = server.accept();
		
//		input = client.getInputStream();
//		reader = new BufferedReader(new InputStreamReader(input));
		dataInput  = new DataInputStream(new BufferedInputStream(client.getInputStream()));
		reader = new BufferedReader(new InputStreamReader(dataInput));
		
		System.out.println("start reading");
		
		String userName = null;
		String passwd = null;
		String tempStr = null;
		int i = 0;
		while((tempStr=reader.readLine())!=null){
			if(i==0){
				userName = tempStr ;
			}
			else{
				passwd = tempStr;
			}
			i++;
			System.out.println(tempStr);
		}
//		char[] user = new char[10];
//		reader.read(user);
//		userName = new String(user);
//		char[] pass = new char[10];
//		reader.read(pass);
//		passwd = new String(pass);
//		System.out.println(userName+"\t"+passwd);
		if(userName.equals("gsfan") && passwd.equals("0620631FGS")){
			System.out.println(userName+"\t"+passwd);
			disconnection();
			return true;
		}
		else
			return false;
	}
	public void disconnection() throws IOException{
		reader.close();
//		input.close();
		dataInput.close();
		client.close();
		server.close();
	}
	
	public static void main(String[] args) throws IOException{
		TCPServer ser = new TCPServer(9000);
		
		QueryMsgCoder coder = new QueryMsgTextCoder();
		QueryService service  = new QueryService();
		while(true){
			Socket client = ser.getClient();
			System.out.println("Handling client at "+client.getRemoteSocketAddress());
			
//			MessageExtractor msgExt = new ExtractMsgByLength(client.getInputStream());
			MessageExtractor msgExt = new ExtractMsgByDelimiter(client.getInputStream());
			try{
				byte[] req;
				while((req=msgExt.nextMessage())!=null){
					System.out.println("Received message ("+req.length+") bytes");
					QueryMsg msg = service.handleRequest(coder.queryMsgDeserialization(req));
					
					msgExt.constructMessage(coder.queryMsgSerialization(msg), client.getOutputStream());
				} 
			}catch (IOException ioe){
				System.out.println("Error handling client:"+ioe.getMessage());
			} finally {
				System.out.println("Closing connection!");
				client.close();
			}
		}
		
//		if(ser.establishConnection()){
//			System.out.println("Connected success!");
//		}
//		else{
//			ser.disconnection();
//		}
//		ser.disconnection();
	}
}