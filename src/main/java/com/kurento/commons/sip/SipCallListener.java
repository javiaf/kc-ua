package com.kurento.commons.sip;

import com.kurento.commons.sip.event.SipCallEvent;

public interface SipCallListener {
	
	public void onEvent (SipCallEvent event);

}
