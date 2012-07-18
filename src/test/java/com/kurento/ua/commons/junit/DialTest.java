package com.kurento.ua.commons.junit;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurento.ua.commons.Call;
import com.kurento.ua.commons.CallEvent;
import com.kurento.ua.commons.EndPoint;
import com.kurento.ua.commons.EndPointEvent;
import com.kurento.ua.commons.junit.util.CallListenerImpl;
import com.kurento.ua.commons.junit.util.EndPointListenerImpl;

public class DialTest {

	private final static Logger log = LoggerFactory.getLogger(DialTest.class);

	public static final int WAIT_TIME = 5;

	private static EndPoint serverEndPoint;
	private static EndPoint clientEndPoint;

	private static String serverName = "server";
	private static String clientName = "client";

	public static EndPoint getClientEndPoint() {
		return clientEndPoint;
	}

	public static void setClientEndPoint(EndPoint clientEndPoint) {
		DialTest.clientEndPoint = clientEndPoint;
	}

	public static EndPoint getServerEndPoint() {
		return serverEndPoint;
	}

	public static void setServerEndPoint(EndPoint serverEndPoint) {
		DialTest.serverEndPoint = serverEndPoint;
	}

	@BeforeClass
	public static void setup() throws Exception {
		log.debug("setup");
	}

	@AfterClass
	public static void tearDown() {
		log.debug("tearDown");
	}

	/**
	 * Verify the UA is able to register a EndPoint for a given URI and it
	 * manages its register/non-register status.
	 * 
	 * <pre>
	 *  1 - clientEndPoint.dial() >>> C:------- DIAL REQUEST ------->:S >>> INCOMING_CALL
	 *               CALL_RINGING <<< C:<--- DIAL REQUEST ARRIVED ---:S
	 *  2 - CALL_SETUP <<< C:<------- ACCEPT CALL -------:S serverCall.accept()
	 *                     C:--- ACCEPT CALL ARRIVED --->:S >>> CALL_SETUP
	 *  3 - clientCall.terminate() >>> C:--- TERMINATE CALL REQUEST --->:S CALL_TERMINATE
	 *       CALL_TERMINATE <<< C:<------ TERMINATE CALL OK -----:S
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDial() throws Exception {
		log.debug("-------------------- testDial --------------------");

		EndPointEvent endPointEvent;
		CallEvent callEvent;

		// 1 - clientEndPoint.dial() >>> C:------- DIAL REQUEST ------->:S >>>
		// INCOMING_CALL
		// CALL_RINGING <<< C:<--- DIAL REQUEST ARRIVED ---:S
		log.info(clientName + " dial to " + serverName + "...");
		CallListenerImpl clientCallListener = new CallListenerImpl(clientName);
		Call clientCall = clientEndPoint.dial(serverEndPoint.getUri(),
				clientCallListener);
		log.info("OK");

		log.info(serverName + " expects incoming call from " + clientName
				+ "...");
		EndPointListenerImpl serverEndPointListener = new EndPointListenerImpl(
				serverName);
		serverEndPoint.addListener(serverEndPointListener);
		endPointEvent = serverEndPointListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", endPointEvent);
		Assert.assertEquals("Bad message received in client UA: "
				+ endPointEvent.getEventType(), EndPointEvent.INCOMING_CALL,
				endPointEvent.getEventType());
		Call serverCall = endPointEvent.getCallSource();
		log.info("OK");

		log.info(clientName + " expects ringing from " + serverName + "...");
		callEvent = clientCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_RINGING,
				callEvent.getEventType());
		log.info("OK");

		// 2 - CALL_SETUP <<< C:<------- ACCEPT CALL -------:S
		// serverCall.accept()
		// C:--- ACCEPT CALL ARRIVED --->:S >>> CALL_SETUP
		log.info(serverName + " accepts call...");
		CallListenerImpl serverCallListener = new CallListenerImpl(serverName);
		serverCall.addListener(serverCallListener);
		serverCall.accept();
		log.info("OK");

		log.info(clientName + " expects accepted call from " + serverName
				+ "...");
		callEvent = clientCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_SETUP,
				callEvent.getEventType());
		log.info("OK");

		log.info(serverName + " expects ACK from " + clientName + "...");
		callEvent = serverCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_SETUP,
				callEvent.getEventType());
		log.info("OK");

		// 3 - clientCall.terminate() >>> C:--- TERMINATE CALL REQUEST --->:S
		// CALL_TERMINATE
		// CALL_TERMINATE <<< C:<------ TERMINATE CALL OK -----:S
		log.info(clientName + " hangup...");
		clientCall.terminate();
		log.info("OK");

		log.info(serverName + " expects call hangup from " + clientName + "...");
		callEvent = serverCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_TERMINATE,
				callEvent.getEventType());
		log.info("OK");

		log.info(clientName + " call terminate...");
		callEvent = clientCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_TERMINATE,
				callEvent.getEventType());
		log.info("OK");

		log.info(" -------------------- testDial finished OK --------------------");
	}

}
