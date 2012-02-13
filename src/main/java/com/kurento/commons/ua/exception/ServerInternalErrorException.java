package com.kurento.commons.ua.exception;

public class ServerInternalErrorException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a ServerInternalErrorException
	 * 
	 * @param message
	 *            The message with the Exception
	 */
	public ServerInternalErrorException(String message) {
		super(message);
	}

	/**
	 * Constructs a ServerInternalErrorException
	 * 
	 * @param message
	 *            The message with the Exception
	 * @param cause
	 *            The cause of the Exception
	 */
	public ServerInternalErrorException(String message, Throwable cause) {
		super(message, cause);
	}

}
