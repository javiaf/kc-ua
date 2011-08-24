package com.kurento.commons.sip;

import com.kurento.commons.sip.event.SipEndPointEvent;

public interface SipEndPointListener  {
		
	public void onEvent (SipEndPointEvent event);

}
