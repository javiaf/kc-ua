package com.kurento.ua.commons;

public enum EndpointEventEnum {
	/**
	 * This event signals the UA received a call INVITE for the EndPoint URI
	 */
	INCOMING_CALL,
	/**
	 * This event signals the UA successfully registered the URI
	 */
	REGISTER_USER_SUCESSFUL,
	/**
	 * This event signals the UA successfully unregistered the URI
	 */
	UNREGISTER_USER_SUCESSFUL,
	/**
	 * This event signals the REGISTER rejected to register the URI with a 403
	 * code
	 */
	REGISTER_USER_NOT_FOUND,
	/**
	 * This event signals the UNREGISTER rejected to register the URI with a 403
	 * code
	 */
	UNREGISTER_USER_NOT_FOUND,
	/**
	 * This event signals a unregister failure with 4xx response
	 */
	UNREGISTER_USER_FAIL,
	/**
	 * This event signals a register failure with 4xx response
	 */
	REGISTER_USER_FAIL,
	/**
	 * This event signals an internal UA error preventing the REGISTER operation
	 * to complete sucessfully
	 */
	MEDIA_NOT_SUPPORTED,
	/**
	 * Call setup failure due to lack of media resources. No ports available
	 */
	MEDIA_RESOURCE_NOT_AVAILABLE,
	/**
	 * Internal error within the local party prevents a normal negotiation flow
	 */
	SERVER_INTERNAL_ERROR
}
