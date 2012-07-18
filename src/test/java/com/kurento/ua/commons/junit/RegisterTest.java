package com.kurento.ua.commons.junit;

import java.util.Map;

import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurento.ua.commons.UA;

public class RegisterTest {

	private final static Logger log = LoggerFactory
			.getLogger(RegisterTest.class);

	public final static int WAIT_TIME = 5;

	protected static UA clientUA;
	protected static UA serverUA;

	protected static String serverName = "server";
	protected static String clientName = "client";

	protected static Map<String, Object> sEpConfig;
	protected static Map<String, Object> cEpConfig;

	public static UA getClientUA() {
		return clientUA;
	}

	public static void setClientUA(UA clientUA) {
		RegisterTest.clientUA = clientUA;
	}

	public static UA getServerUA() {
		return serverUA;
	}

	public static void setServerUA(UA serverUA) {
		RegisterTest.serverUA = serverUA;
	}

	public static Map<String, Object> getsEpConfig() {
		return sEpConfig;
	}

	public static void setsEpConfig(Map<String, Object> sEpConfig) {
		RegisterTest.sEpConfig = sEpConfig;
	}

	public static Map<String, Object> getcEpConfig() {
		return cEpConfig;
	}

	public static void setcEpConfig(Map<String, Object> cEpConfig) {
		RegisterTest.cEpConfig = cEpConfig;
	}

	@After
	public void terminateClientUa() {
		log.debug("terminateClientUa");
		clientUA.terminate();
	}

}
