package com.tikal.sip;

import com.tikal.sip.event.SipCallEvent;

public interface SipCallListener {
	
	public void onEvent (SipCallEvent event);

}
