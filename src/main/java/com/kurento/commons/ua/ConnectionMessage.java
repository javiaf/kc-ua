package com.kurento.commons.ua;

import java.util.HashMap;
import java.util.Map;

public class ConnectionMessage {

	private final Connection replyTo;
	private final Map<String, String> data;

	public ConnectionMessage(Connection replyTo) {
		this(replyTo, new HashMap<String, String>());
	}

	public ConnectionMessage(Connection replyTo, Map<String, String> data) {
		if (replyTo == null || data == null)
			throw new NullPointerException();

		this.replyTo = replyTo;
		this.data = data;
	}

	public Connection getReplyTo() {
		return replyTo;
	}

	public Map<String, String> getData() {
		return data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConnectionMessage [");
		if (replyTo != null) {
			builder.append("replyTo=");
			builder.append(replyTo);
			builder.append(", ");
		}
		if (data != null) {
			builder.append("data=");
			builder.append(data);
		}
		builder.append("]");
		return builder.toString();
	}

}
