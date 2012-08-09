package com.kurento.commons.ua;

public interface Connection {

	public void getUri(Continuation<String, Void> continuation);

	public String getId();

	public void isMain(Continuation<Boolean, Void> continuation);

	public void sendMessage(ConnectionMessage message);

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

	public void addListener(ConnectionMessageListener listener);

	public void removeListener(ConnectionMessageListener listener);
}
