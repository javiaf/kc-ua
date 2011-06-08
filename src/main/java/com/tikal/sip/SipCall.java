package com.tikal.sip;

import javax.media.mscontrol.join.JoinableStream;
import javax.media.mscontrol.networkconnection.NetworkConnection;

import com.tikal.sip.exception.ServerInternalErrorException;





public interface SipCall {
	
	// Control interface
	public void accept() throws ServerInternalErrorException ;
	public void reject() throws ServerInternalErrorException ;
	public void hangup() throws ServerInternalErrorException ;

	// monitor interface
	public Boolean isConnected();
	
	// Callbacks
	public void setListener (SipCallListener listener);
	
	// Media
	public NetworkConnection getNetworkConnection(JoinableStream.StreamType media);
	
	
}
