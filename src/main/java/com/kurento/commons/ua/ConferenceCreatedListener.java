package com.kurento.commons.ua;


public interface ConferenceCreatedListener {

	public void conferenceCreated(Conference conference);

	public void error(int code, String message);
}
