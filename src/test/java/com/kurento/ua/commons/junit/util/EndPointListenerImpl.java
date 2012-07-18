package com.kurento.ua.commons.junit.util;

import com.kurento.ua.commons.EndPointEvent;
import com.kurento.ua.commons.EndPointListener;

public class EndPointListenerImpl extends EventListener<EndPointEvent>
		implements EndPointListener {

	public EndPointListenerImpl(String id) {
		super("EndPoint-" + id);
	}

	@Override
	public void onEvent(EndPointEvent event) {
		super.onEvent(event);
	}

}
