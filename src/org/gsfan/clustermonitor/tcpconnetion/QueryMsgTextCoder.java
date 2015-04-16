package org.gsfan.clustermonitor.tcpconnetion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class QueryMsgTextCoder implements QueryMsgCoder {

	public static final String MAGIC = "Voting";
	public static final String VOTESTR = "v";
	public static final String INQUIRYSTR = "i";
	public static final String RESPONSESTR = "R";
	
	public static final String CHARSETNAME = "US-ASCII";
	public static final String DELIMITER = " ";
//	public static final int MAX_WIRE_LENGTH = 2000;
	
	public byte[] queryMsgSerialization(QueryMsg msg) throws IOException{
		String msgStr = MAGIC + DELIMITER + (msg.isInquiry() ? INQUIRYSTR : VOTESTR)
				+ DELIMITER + (msg.isResponse() ? RESPONSESTR + DELIMITER : " ")
				+Integer.toString(msg.getCandidateID()) + DELIMITER
				+Long.toString(msg.getVoteCount());
		return msgStr.getBytes(CHARSETNAME);
	}
	
	public QueryMsg queryMsgDeserialization(byte[] input) throws IOException{
		
		ByteArrayInputStream msgStream = new ByteArrayInputStream(input);
		
		String token = null;
		
		int candidateID = 0;
		boolean isInquiry = false;
		boolean isResponse = false;
		long voteCount = 0L;
		
		Scanner scanner = new Scanner(new InputStreamReader(msgStream,CHARSETNAME));
		try {
			token = scanner.next();
			if(!token.equals(MAGIC)) {
				throw new IOException("Bad magic string:" + token);
			}
			
			token = scanner.next();
			if(token.equals(VOTESTR)) {
				isInquiry = false;
			} else if(!token.equals(INQUIRYSTR)) {
				throw new IOException("Bad vote/inq indcator:" + token);
			} else {
				isInquiry = true;
			}
			
			token = scanner.next();
			if(token.equals(RESPONSESTR)) {
				isResponse = true;
				token = scanner.next();
			} else {
				isResponse = false;
			}
			
			candidateID = Integer.parseInt(token);
			if(isResponse) {
				token = scanner.next();
				voteCount = Integer.parseInt(token);
			} else {
				voteCount = 0;
			}
		} catch (Exception e) { 
			throw new IOException("Parse error...");
		} finally {
			scanner.close();
		}
		
		return new QueryMsg(isResponse, isInquiry, candidateID, voteCount);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
