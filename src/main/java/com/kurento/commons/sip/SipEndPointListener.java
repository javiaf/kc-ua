package com.kurento.commons.sip;

import com.kurento.commons.sip.event.SipEndPointEvent;

/**
 * This interface must be implemented by applications willing to receive
 * information from SipEndPoints in the form of {@link SipEndPointEvent}. Application is
 * responsible of event management and actions to be performed will depend on the model of use
 * 
 * @author Kurento
 * 
 */
public interface SipEndPointListener  {
	/**
	 * Event end point. This method will be called by {@link SipEndPoint} every time
	 * a EndPoint event event occurs
	 * 
	 * @param event
	 */	
	public void onEvent (SipEndPointEvent event);

}
