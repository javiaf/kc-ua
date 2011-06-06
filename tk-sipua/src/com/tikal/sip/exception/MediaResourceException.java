package com.tikal.sip.exception;


/**
 * Global exception for resources errors.
 * @author qiang
 *
 */
public class MediaResourceException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a MediaResourceException
	 * 
	 * @param message
	 *            The message with the Exception
	 */
	public MediaResourceException(String message) {
		super(message);
	}

	/**
	 * Constructs a MediaResourceException
	 * 
	 * @param message
	 *            The message with the Exception
	 * @param cause
	 *            The cause of the Exception
	 */
	public MediaResourceException(String message, Throwable cause) {
		super(message, cause);
	}
}
