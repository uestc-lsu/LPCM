package org.gsfan.clustermonitor.datatransmission;

import java.io.IOException;

public class DataTransmissionThread implements Runnable{

	private DataTransmission transmit = null;

	public DataTransmissionThread(DataTransmission transmit){
		this.transmit = transmit;

	}
	
	public Message firstCommunication(){

		MessageCodec codec = transmit.getCodec();
		MessageFramer framer = transmit.getFramer();
		
		Message message = null;
		try{
			byte[] req;
			if((req=framer.nextMessage())!=null){
				System.out.println("Received message ("+req.length+") bytes" );
				message = codec.messageDecode(req);
			} 
		}catch (IOException ioe){
			System.out.println("Error handling client:"+ioe.getMessage());
		}
		return message;

	}
	public void run() {

		Message msg = firstCommunication();
		if(msg!=null){
			QueryService service = new QueryService();
			try {
				service.handleRequest(msg, this.transmit);
			} catch (IOException e) {
				System.out.println("In DataTransmissionThread!");
			}
		}
	}
}
