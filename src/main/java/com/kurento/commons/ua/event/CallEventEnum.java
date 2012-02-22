package com.kurento.commons.ua.event;


public enum CallEventEnum {
	/**
	 * Call negotiation has completed successfully and media channels are setup
	 */
	CALL_SETUP,
	/**
	 * Call rejected by the called party
	 */
	CALL_REJECT,
	/**
	 * Call canceled by the calling party
	 */
	CALL_CANCEL,
	/**
	 * Call terminated (hang up) by one of the parties
	 */
	CALL_TERMINATE,
	/**
	 * Call setup failure due to a protocol problem
	 */
	CALL_ERROR,
	/**
	 * Call setup failure due to incompatible media formats
	 */
	MEDIA_NOT_SUPPORTED,
	/**
	 * Call setup failure due to lack of media resources. No ports available
	 */
	MEDIA_RESOURCE_NOT_AVAILABLE,
	/**
	 * Internal error within the local party prevents a normal negotiation flow
	 */
	SERVER_INTERNAL_ERROR,
	/**
	 * Call requested
	 */
	CALL_REQUEST,
}
