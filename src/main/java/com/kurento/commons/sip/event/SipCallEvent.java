package com.kurento.commons.sip.event;

import java.util.EventObject;

import com.kurento.commons.sip.SipCall;

public class SipCallEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6895550063104778497L;
	
	public static final SipEventType CALL_SETUP = EventTypeEnum.CALL_SETUP;
	public static final SipEventType CALL_REJECT = EventTypeEnum.CALL_REJECT;
	public static final SipEventType CALL_CANCEL = EventTypeEnum.CALL_CANCEL;
	public static final SipEventType CALL_TERMINATE = EventTypeEnum.CALL_TERMINATE;
	public static final SipEventType CALL_ERROR = EventTypeEnum.CALL_ERROR;

	public static final SipEventType MEDIA_NOT_SUPPORTED = EventTypeEnum.MEDIA_NOT_SUPPORTED;
	public static final SipEventType MEDIA_RESOURCE_NOT_AVAILABLE = EventTypeEnum.MEDIA_RESOURCE_NOT_AVAILABLE;
	
	public static final SipEventType SERVER_INTERNAL_ERROR = EventTypeEnum.SERVER_INTERNAL_ERROR;


	private SipEventType eventType;
	
	public SipCallEvent (SipEventType eventType, SipCall source) {
		super(source);
		this.eventType = eventType;
	}
	
	public void setSource(SipCall source) {
		this.source = source;
	}
	public SipCall getSource() {
		return (SipCall) source;
	}
	public void setEventType(SipEventType eventType) {
		this.eventType = eventType;
	}
	public SipEventType getEventType() {
		return eventType;
	}
	
	public String toString() {
		return eventType.toString();
	}

}
