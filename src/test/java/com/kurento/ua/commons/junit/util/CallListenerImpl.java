package com.kurento.ua.commons.junit.util;

import com.kurento.ua.commons.CallEvent;
import com.kurento.ua.commons.CallListener;

public class CallListenerImpl extends EventListener<CallEvent> implements
		CallListener {

	public CallListenerImpl(String id) {
		super("EndPoint-" + id);
	}

	@Override
	public void onEvent(CallEvent event) {
		super.onEvent(event);
	}

}
