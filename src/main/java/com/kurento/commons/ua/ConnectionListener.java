package com.kurento.commons.ua;

public interface ConnectionListener {

	void onMainStatusChanged(Boolean main);

	void onMuteStatusChanged(Boolean muteStatus);

}
