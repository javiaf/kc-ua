package com.tikal.sip;

import java.text.ParseException;

import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.header.UserAgentHeader;

import com.tikal.sip.exception.ServerInternalErrorException;



/**
 *
 * @author fjlopez
 * @version 1.0.0
 */
public interface UA  {
		
	public UserAgentHeader getUserAgentHeader();
	public String getLocalAddress();
	public int getLocalPort();
	public String getProxyAddress();
	public int getProxyPort();
	public String getTransport();
	public int getMaxForwards();

	public SipProvider getSipProvider();
	public SipStack getSipStack();
	
	public void terminate();
	
	public SipEndPoint registerEndPoint(String user, String realm, int expires, SipEndPointListener handler) throws ParseException, ServerInternalErrorException;
	
}