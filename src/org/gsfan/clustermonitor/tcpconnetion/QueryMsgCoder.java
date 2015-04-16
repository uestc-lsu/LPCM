package org.gsfan.clustermonitor.tcpconnetion;

import java.io.IOException;

public interface QueryMsgCoder {
	public byte[] queryMsgSerialization(QueryMsg msg) throws IOException;
	public QueryMsg queryMsgDeserialization(byte[] input) throws IOException;
}