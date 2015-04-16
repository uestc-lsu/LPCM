package org.gsfan.clustermonitor.tcpconnetion;

public class VoteMsg {

	private boolean isInquiry;
	private boolean isResponse;
	private int candidateID;
	private long voteCount;
	
	public static final int MAX_CANDIDATE_ID = 1000;
	
	public VoteMsg(boolean isInquiry, boolean isResponse, int candidateID, long voteCount) {

		if(voteCount != 0 && !isResponse) {
			throw new IllegalArgumentException("Request vote must be zero!");
		}
		if(candidateID < 0 || candidateID > MAX_CANDIDATE_ID ) {
			throw new IllegalArgumentException("Bad Candidate id: " + candidateID);
		}
		if(voteCount < 0) {
			throw new IllegalArgumentException("Total votes must be >= zero!");
		}
		this.isInquiry = isInquiry;
		this.isResponse = isResponse;
		this.candidateID = candidateID;
		this.voteCount = voteCount;
	}
	
	public boolean isInquiry() {
		return isInquiry;
	}
	public void setInquiry(boolean isInquiry) {
		this.isInquiry = isInquiry;
	}
	public boolean isResponse() {
		return isResponse;
	}
	public void setResponse(boolean isResponse) {
		this.isResponse = isResponse;
	}
	public int getCandidateID() {
		return candidateID;
	}
	public void setCandidateID(int candidateID) {
		if(candidateID < 0 || candidateID > MAX_CANDIDATE_ID ) {
			throw new IllegalArgumentException("Bad Candidate id: " + candidateID);
		}
		this.candidateID = candidateID;
	}
	public long getVoteCount() {
		return voteCount;
	}
	
	public void setVoteCount(long voteCount) {
		if((voteCount != 0 && !isResponse) || voteCount < 0 ) {
			throw new IllegalArgumentException("Bad Candidate id: " + candidateID);
		}
		this.voteCount = voteCount;
	}
	
	public String toString(){
		String str = (isInquiry ? "inquiry" : "vote") + "for candidate: " + candidateID;
		if(isResponse) {
			str = "response to " + str + "who now has " + voteCount + "vote(s)"; 
		}
		return str;
	}

}
