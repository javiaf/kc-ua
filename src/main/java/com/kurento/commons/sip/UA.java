package com.kurento.commons.sip;

import java.text.ParseException;

import com.kurento.commons.sip.exception.ServerInternalErrorException;



/**
 *
 * @author fjlopez
 * @version 1.0.0
 */
public interface UA  {
		
//	public UserAgentHeader getUserAgentHeader();
	public String getLocalAddress();
	public int getLocalPort();
	public String getProxyAddress();
	public int getProxyPort();
	
	public void terminate();
	
	public SipEndPoint registerEndPoint(String user, String realm, String password, int expires, SipEndPointListener handler) throws ParseException, ServerInternalErrorException;
	
}