package com.kurento.commons.sip;

import com.kurento.commons.sip.exception.ServerInternalErrorException;


public interface SipEndPoint {
	
	public void terminate () throws ServerInternalErrorException;

	public SipCall dial(String remoteParty, SipCallListener callController) throws ServerInternalErrorException;
	public void options(String remoteParty, SipCallListener callController) throws ServerInternalErrorException;

}
