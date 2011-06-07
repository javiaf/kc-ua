package com.tikal.sip;

import com.tikal.sip.event.SipEndPointEvent;

public interface SipEndPointListener  {
		
	public void onEvent (SipEndPointEvent event);

}
