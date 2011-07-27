package com.tikal.sip;

import com.tikal.mscontrol.join.JoinableStream;
import com.tikal.mscontrol.networkconnection.NetworkConnection;
import com.tikal.sip.exception.ServerInternalErrorException;

public interface SipCall {

	// Control interface
	public void accept() throws ServerInternalErrorException;

	public void reject() throws ServerInternalErrorException;

	public void hangup() throws ServerInternalErrorException;

	// monitor interface
	public Boolean isConnected();

	// Callbacks
	public void addListener(SipCallListener listener);

	public void removeListener(SipCallListener listener);

	// Media
	public NetworkConnection getNetworkConnection(
			JoinableStream.StreamType media);

	public String getRemoteUri();

	public String getRemoteDisplayName();

}
