package com.kurento.commons.ua;

import com.kurento.commons.ua.event.CallEvent;

/**
 * This interface must be implemented by applications willing to receive
 * information from Call in the form of {@link CallEvent}. Application is
 * responsible of event management and actions to be performed will depend on
 * its model of use
 * 
 * @author Kurento
 * 
 */
public interface CallListener {

	/**
	 * Event end point. This method will be called by {@link Call} every time
	 * a call event occurs
	 * 
	 * @param event
	 */
	public void onEvent(CallEvent event);

}
