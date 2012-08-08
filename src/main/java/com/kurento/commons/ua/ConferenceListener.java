package com.kurento.commons.ua;


public interface ConferenceListener {

	void onMainStatusChanged(Connection connectionId, Boolean main);

	void onConnectionAdded(Connection connection);

	void onConnectionRemoved(Connection connection);

	void onTerminated();
}
