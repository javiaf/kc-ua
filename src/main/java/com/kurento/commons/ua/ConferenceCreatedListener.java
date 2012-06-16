package com.kurento.commons.ua;

import com.kurento.commons.ua.Conference;

public interface ConferenceCreatedListener {

	public void conferenceCreated(Conference conference);

	public void error(int code, String message);
}
