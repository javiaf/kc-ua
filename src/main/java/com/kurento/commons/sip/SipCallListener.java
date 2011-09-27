package com.kurento.commons.sip;

import com.kurento.commons.sip.event.SipCallEvent;

/**
 * This interface must be implemented by applications willing to receive
 * information from SipCall in the form of {@link SipCallEvent}. Application is
 * responsible of event management and actions to be performed will depend on
 * its model of use
 * 
 * @author Kurento
 * 
 */
public interface SipCallListener {

	/**
	 * Event end point. This method will be called by {@link SipCall} every time
	 * a call event occurs
	 * 
	 * @param event
	 */
	public void onEvent(SipCallEvent event);

}
