package com.tikal.sip;

import javax.media.mscontrol.join.Joinable.Direction;
import javax.sip.address.Address;

import com.tikal.sip.exception.ServerInternalErrorException;


public interface SipEndPoint {
	
	public void terminate () throws ServerInternalErrorException;

	public SipCall dial(String remoteParty, Direction direction, SipCallListener callController) throws ServerInternalErrorException;
	public void options(String remoteParty, SipCallListener callController) throws ServerInternalErrorException;

}
