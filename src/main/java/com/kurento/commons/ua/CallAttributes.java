package com.kurento.commons.ua;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CallAttributes {

	public static final String EXTRA_ATTRIBUTES_HEADER = "ExtraAttributes";
	private static final String KEY_VAL_SEP = "|";
	private static final String ATTRIBUTE_SEP = ";";

	public static final String CONNECTION_ID = "ConnectionId";
	public static final String CONFERENCE_ID = "ConferenceId";
	public static final String GROUP_ID = "GroupId";

	private Map<String, String> attributes = new HashMap<String, String>();

	public CallAttributes() {

	}

	public CallAttributes(Map<String, String> attributes) {
		if (attributes != null)
			this.attributes.putAll(attributes);
	}

	public CallAttributes(String attributesStr) {
		if (attributesStr == null || attributesStr.equals(""))
			return;

		String[] attributesArray = attributesStr.split(ATTRIBUTE_SEP);

		for (String attribute : attributesArray) {
			String[] keyVal = attribute.split(KEY_VAL_SEP);
			if (keyVal.length == 2) {
				attributes.put(keyVal[0], keyVal[1]);
			}
		}
	}

	public Set<String> keySet() {
		return attributes.keySet();

	}

	public Collection<String> values() {
		return attributes.values();
	}

	public String getAttribute(String key) {
		return attributes.get(key);
	}

	public Map<String, String> getMap() {
		return attributes;
	}

	@Override
	public String toString() {
		if (attributes == null || attributes.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder();

		for (String key : attributes.keySet()) {
			if (sb.length() != 0)
				sb.append(ATTRIBUTE_SEP);
			sb.append(key);
			sb.append(KEY_VAL_SEP);
			sb.append(attributes.get(key));
		}

		return sb.toString();
	}
}
