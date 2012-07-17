package com.kurento.ua.commons;

import java.util.Collection;

public interface Conference extends Call {

	public void terminateConference();

	public void invite(String remoteParty, Continuation<Void, InviteStateEnum> cont);

	public void invite(String remoteParty, CallAttributes attributes,
			Continuation<Void, InviteStateEnum> cont);

	public void setMain(String connectionId, Boolean main);

	public void setMute(String connectionId, Boolean mute);

	public String getMyConnectionId();

	public void getConnections(Continuation<Collection<String>, Void> cont);

	public void getConnectionUri(String connection,
			Continuation<String, Void> cont);

	public void getConnectionMain(String connection,
			Continuation<Boolean, Void> cont);

	/**
	 * Add a new listener object that will receive conference events
	 */
	public void addListener(ConferenceListener listener);

	/**
	 * Remove an object from the list of elements receiving conference event
	 * notifications
	 * 
	 * @param listener
	 */
	public void removeListener(ConferenceListener listener);
}
