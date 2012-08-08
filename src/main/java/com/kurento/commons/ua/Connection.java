package com.kurento.commons.ua;

public interface Connection {

	public void getUri(Continuation<String, Void> continuation);

	public String getId();

	public void isMain(Continuation<Boolean, Void> continuation);

	/**
	 * Add a new listener object that will receive conference events
	 */
	public void addListener(ConnectionListener listener);

	/**
	 * Remove an object from the list of elements receiving conference event
	 * notifications
	 * 
	 * @param listener
	 */
	public void removeListener(ConnectionListener listener);
}
