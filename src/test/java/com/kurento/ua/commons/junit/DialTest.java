package com.kurento.ua.commons.junit;

import junit.framework.Assert;

import org.junit.Before;
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

	private static EndPointListenerImpl serverEndPointListener;

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

	@Before
	public void setupEndPointListeners() {
		serverEndPointListener = new EndPointListenerImpl(serverName);
		serverEndPoint.addListener(serverEndPointListener);
	}

	/**
	 * Verify the EndPoint is able to dial other peer and the caller can hang up
	 * the call.
	 * 
	 * <pre>
	 *  1 -  clientEndPoint.dial() >>> C:--- DIAL REQUEST ------------->:S >>> INCOMING_CALL
	 *                CALL_RINGING <<< C:<----- DIAL REQUEST ARRIVED ---:S
	 *  2 -             CALL_SETUP <<< C:<-------------- ACCEPT CALL ---:S <<< serverCall.accept()
	 *                                 C:--- ACCEPT CALL ARRIVED ------>:S >>> CALL_SETUP
	 *  3 - clientCall.terminate() >>> C:--- TERMINATE CALL REQUEST --->:S >>> CALL_TERMINATE
	 *              CALL_TERMINATE <<< C:<-------- TERMINATE CALL OK ---:S
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallSetupAndDropFromCaller() throws Exception {
		log.debug("-------------------- testCallSetupAndDropFromCaller --------------------");

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
		endPointEvent = serverEndPointListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", endPointEvent);
		Assert.assertEquals("Bad message received in server UA: "
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
		// <<< serverCall.accept()
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
		Assert.assertNotNull("No message received in server UA", callEvent);
		Assert.assertEquals(
				"Bad message received in server UA: "
						+ callEvent.getEventType(), CallEvent.CALL_SETUP,
				callEvent.getEventType());
		log.info("OK");

		// 3 - clientCall.terminate() >>> C:--- TERMINATE CALL REQUEST --->:S
		// >>> CALL_TERMINATE
		// CALL_TERMINATE <<< C:<------ TERMINATE CALL OK -----:S
		log.info(clientName + " hangup...");
		clientCall.terminate();
		log.info("OK");

		log.info(serverName + " expects call hangup from " + clientName + "...");
		callEvent = serverCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", callEvent);
		Assert.assertEquals(
				"Bad message received in server UA: "
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

		log.info(" -------------------- testCallSetupAndDropFromCaller finished OK --------------------");
	}

	/**
	 * Verify the EndPoint is able to dial other peer and the callee can hang up
	 * the call.
	 * 
	 * <pre>
	 *  1 -  clientEndPoint.dial() >>> C:--- DIAL REQUEST ------------->:S >>> INCOMING_CALL
	 *                CALL_RINGING <<< C:<----- DIAL REQUEST ARRIVED ---:S
	 *  2 -             CALL_SETUP <<< C:<-------------- ACCEPT CALL ---:S <<< serverCall.accept()
	 *                                 C:--- ACCEPT CALL ARRIVED ------>:S >>> CALL_SETUP
	 *  3 -         CALL_TERMINATE <<< C:<--- TERMINATE CALL REQUEST ---:S <<< serverCall.terminate()
	 *                                 C:--- TERMINATE CALL OK--------->:S >>> CALL_TERMINATE
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallSetupAndDropFromCallee() throws Exception {
		log.debug("-------------------- testCallSetupAndDropFromCallee --------------------");

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
		endPointEvent = serverEndPointListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", endPointEvent);
		Assert.assertEquals("Bad message received in server UA: "
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
		Assert.assertNotNull("No message received in server UA", callEvent);
		Assert.assertEquals(
				"Bad message received in server UA: "
						+ callEvent.getEventType(), CallEvent.CALL_SETUP,
				callEvent.getEventType());
		log.info("OK");

		// 3 - CALL_TERMINATE <<< C:<--- TERMINATE CALL REQUEST ---:S <<<
		// serverCall.terminate()
		// C:--- TERMINATE CALL OK--------->:S >>> CALL_TERMINATE
		log.info(serverName + " hangup...");
		serverCall.terminate();
		log.info("OK");

		log.info(clientName + " expects call hangup from " + serverName + "...");
		callEvent = clientCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_TERMINATE,
				callEvent.getEventType());
		log.info("OK");

		log.info(serverName + " call terminate...");
		callEvent = serverCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", callEvent);
		Assert.assertEquals(
				"Bad message received in server UA: "
						+ callEvent.getEventType(), CallEvent.CALL_TERMINATE,
				callEvent.getEventType());
		log.info("OK");

		log.info(" -------------------- testCallSetupAndDropFromCallee finished OK --------------------");
	}
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_TERMINATE,
				callEvent.getEventType());
		log.info("OK");


		log.info(" -------------------- testCallSetupAndDropFromCallee finished OK --------------------");
	}

}
