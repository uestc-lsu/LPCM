package org.gsfan.clustermonitor.tcpconnetion;

import java.util.HashMap;
import java.util.Map;

public class QueryService {
	private Map<Integer, Long> results = new HashMap<Integer, Long>();
	
//	public QueryService() {
//		
//	}
	
	public QueryMsg handleRequest(QueryMsg msg){
		if(msg.isResponse()){
			return msg;
		}
		
		msg.setResponse(true);
		int candidateID = msg.getCandidateID();
		Long count = results.get(candidateID);
		if(count==null){
			count = 0L;
		}
		if(!msg.isInquiry()){
			results.put(candidateID, ++count);
		}
		msg.setVoteCount(count);
		return msg;
	}
}
