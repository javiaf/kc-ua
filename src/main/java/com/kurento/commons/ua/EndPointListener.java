package com.kurento.commons.ua;

import com.kurento.commons.ua.event.EndPointEvent;

/**
 * This interface must be implemented by applications willing to receive
 * information from EndPoints in the form of {@link EndPointEvent}. Application is
 * responsible of event management and actions to be performed will depend on the model of use
 * 
 * @author Kurento
 * 
 */
public interface EndPointListener  {
	/**
	 * Event end point. This method will be called by {@link EndPoint} every time
	 * a EndPoint event event occurs
	 * 
	 * @param event
	 */	
	public void onEvent (EndPointEvent event);

}
