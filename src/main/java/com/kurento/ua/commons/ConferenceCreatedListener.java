package com.kurento.ua.commons;

import com.kurento.ua.commons.Conference;

public interface ConferenceCreatedListener {

	public void conferenceCreated(Conference conference);

	public void error(int code, String message);
}
