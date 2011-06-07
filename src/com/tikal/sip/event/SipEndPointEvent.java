package com.tikal.sip.event;

import java.util.EventObject;

import com.tikal.sip.SipCall;
import com.tikal.sip.SipEndPoint;
import com.tikal.sip.event.SipEndPointEvent;

public class SipEndPointEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6307930313482035976L;
	
	public static final SipEventType INCOMING_CALL = EventTypeEnum.INCOMING_CALL;
	
	public static final SipEventType REGISTER_USER_SUCESSFUL = EventTypeEnum.REGISTER_USER_SUCESSFUL;
	public static final SipEventType REGISTER_USER_NOT_FOUND = EventTypeEnum.REGISTER_USER_NOT_FOUND;
	public static final SipEventType REGISTER_USER_FAIL = EventTypeEnum.REGISTER_USER_FAIL;
	
	public static final SipEventType SERVER_INTERNAL_ERROR = EventTypeEnum.SERVER_INTERNAL_ERROR;
	
	private SipEventType eventType;


	public SipEndPointEvent(SipEventType eventType, SipCall source) {
		super(source);
		this.eventType = eventType;
	}
	public SipEndPointEvent(SipEventType eventType, SipEndPoint source) {
		super(source);
		this.eventType = eventType;
	}
	

	public SipCall getCallSource() {
		return (SipCall) source;
	}
	
	public SipEndPoint getEndPointSource () {
		return (SipEndPoint) source;
	}
	
	public SipEventType getEventType() {
		return eventType;
	}
	
	public String toString() {
		return eventType.toString();
	}
}
