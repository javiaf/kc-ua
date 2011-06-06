package com.tikal.sip;

import javax.media.mscontrol.networkconnection.NetworkConnection;

import com.tikal.sip.exception.ServerInternalErrorException;




/**
 * 
 * @author qiang
 *
 */
public interface SipCall {
	
	// Control interface
	public void accept() throws ServerInternalErrorException ;
	public void reject() throws ServerInternalErrorException ;
	public void hangup() throws ServerInternalErrorException ;

	// monitor interface
	public Boolean isConnected();
	
	// Callbacks
	public void addListener (SipCallListener listener);
	public void removeListener (SipCallListener listener);
	
	// Media
	public NetworkConnection getNetworkConnection();
	
	
}
