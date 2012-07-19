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
	private static EndPoint clientEndPoint1;
	private static EndPoint clientEndPoint2;

	private static EndPointListenerImpl serverEndPointListener;

	private static String serverName = "server";
	private static String clientName1 = "client1";
	private static String clientName2 = "client2";

	public static EndPoint getClientEndPoint1() {
		return clientEndPoint1;
	}

	public static void setClientEndPoint1(EndPoint clientEndPoint) {
		DialTest.clientEndPoint1 = clientEndPoint;
	}

	public static EndPoint getClientEndPoint2() {
		return clientEndPoint2;
	}

	public static void setClientEndPoint2(EndPoint clientEndPoint2) {
		DialTest.clientEndPoint2 = clientEndPoint2;
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
		log.info(clientName1 + " dial to " + serverName + "...");
		CallListenerImpl clientCallListener = new CallListenerImpl(clientName1);
		Call clientCall = clientEndPoint1.dial(serverEndPoint.getUri(),
				clientCallListener);
		log.info("OK");

		log.info(serverName + " expects incoming call from " + clientName1
				+ "...");
		endPointEvent = serverEndPointListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", endPointEvent);
		Assert.assertEquals("Bad message received in server UA: "
				+ endPointEvent.getEventType(), EndPointEvent.INCOMING_CALL,
				endPointEvent.getEventType());
		Call serverCall = endPointEvent.getCallSource();
		log.info("OK");

		log.info(clientName1 + " expects ringing from " + serverName + "...");
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

		log.info(clientName1 + " expects accepted call from " + serverName
				+ "...");
		callEvent = clientCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_SETUP,
				callEvent.getEventType());
		log.info("OK");

		log.info(serverName + " expects ACK from " + clientName1 + "...");
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
		log.info(clientName1 + " hangup...");
		clientCall.terminate();
		log.info("OK");

		log.info(serverName + " expects call hangup from " + clientName1
				+ "...");
		callEvent = serverCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", callEvent);
		Assert.assertEquals(
				"Bad message received in server UA: "
						+ callEvent.getEventType(), CallEvent.CALL_TERMINATE,
				callEvent.getEventType());
		log.info("OK");

		log.info(clientName1 + " call terminate...");
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
		log.info(clientName1 + " dial to " + serverName + "...");
		CallListenerImpl clientCallListener = new CallListenerImpl(clientName1);
		Call clientCall = clientEndPoint1.dial(serverEndPoint.getUri(),
				clientCallListener);
		log.info("OK");

		log.info(serverName + " expects incoming call from " + clientName1
				+ "...");
		endPointEvent = serverEndPointListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", endPointEvent);
		Assert.assertEquals("Bad message received in server UA: "
				+ endPointEvent.getEventType(), EndPointEvent.INCOMING_CALL,
				endPointEvent.getEventType());
		Call serverCall = endPointEvent.getCallSource();
		log.info("OK");

		log.info(clientName1 + " expects ringing from " + serverName + "...");
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

		log.info(clientName1 + " expects accepted call from " + serverName
				+ "...");
		callEvent = clientCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_SETUP,
				callEvent.getEventType());
		log.info("OK");

		log.info(serverName + " expects ACK from " + clientName1 + "...");
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

		log.info(clientName1 + " expects call hangup from " + serverName
				+ "...");
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

	/**
	 * Verify the EndPoint is able to dial other peer and cancel it before the
	 * other peer accepts the call.
	 * 
	 * <pre>
	 *  1 -  clientEndPoint.dial() >>> C:--- DIAL REQUEST ------------->:S >>> INCOMING_CALL
	 *                CALL_RINGING <<< C:<----- DIAL REQUEST ARRIVED ---:S
	 *  2 - clientCall.terminate() >>> C:--- CANCEL CALL -------------->:S >>> CALL_CANCEL
	 *                 CALL_CANCEL <<< C:<----------- CANCEL CALL OK ---:S
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCancelCall() throws Exception {
		log.debug("-------------------- testCallSetupAndDropFromCallerBeforAccept --------------------");

		EndPointEvent endPointEvent;
		CallEvent callEvent;

		// 1 - clientEndPoint.dial() >>> C:------- DIAL REQUEST ------->:S >>>
		// INCOMING_CALL
		// CALL_RINGING <<< C:<--- DIAL REQUEST ARRIVED ---:S
		log.info(clientName1 + " dial to " + serverName + "...");
		CallListenerImpl clientCallListener = new CallListenerImpl(clientName1);
		Call clientCall = clientEndPoint1.dial(serverEndPoint.getUri(),
				clientCallListener);
		log.info("OK");

		log.info(serverName + " expects incoming call from " + clientName1
				+ "...");
		endPointEvent = serverEndPointListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", endPointEvent);
		Assert.assertEquals("Bad message received in server UA: "
				+ endPointEvent.getEventType(), EndPointEvent.INCOMING_CALL,
				endPointEvent.getEventType());
		Call serverCall = endPointEvent.getCallSource();
		CallListenerImpl serverCallListener = new CallListenerImpl(serverName);
		serverCall.addListener(serverCallListener);
		log.info("OK");

		log.info(clientName1 + " expects ringing from " + serverName + "...");
		callEvent = clientCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_RINGING,
				callEvent.getEventType());
		log.info("OK");

		// 2 - clientCall.terminate() >>> C:--- CANCEL CALL -------------->:S
		// >>> CALL_CANCEL
		// CALL_CANCEL <<< C:<----------- CANCEL CALL OK ---:S
		log.info(clientName1 + " hangup...");
		clientCall.terminate();
		log.info("OK");

		log.info(serverName + " expects call cancel from " + clientName1
				+ "...");
		log.info(clientName1 + " call terminate...");
		callEvent = serverCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", callEvent);
		Assert.assertEquals(
				"Bad message received in server UA: "
						+ callEvent.getEventType(), CallEvent.CALL_CANCEL,
				callEvent.getEventType());
		log.info("OK");

		log.info(clientName1 + " call cancel...");
		callEvent = clientCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_CANCEL,
				callEvent.getEventType());
		log.info("OK");

		log.info(" -------------------- testCallSetupAndDropFromCallerBeforAccept finished OK --------------------");
	}

	/**
	 * Verify call is terminated in both sides after the caller party rejects
	 * the dial request.
	 * 
	 * <pre>
	 *  1 -  clientEndPoint.dial() >>> C:--- DIAL REQUEST ------------->:S >>> INCOMING_CALL
	 *                CALL_RINGING <<< C:<----- DIAL REQUEST ARRIVED ---:S
	 *  2 -            CALL_REJECT <<< C:<-------------- REJECT CALL ---:S <<< serverCall.terminate()
	 *                                 C:--- REJECT CALL OK------------>:S >>> CALL_REJECT
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRejectCall() throws Exception {
		log.debug("-------------------- testCallSetupAndDropFromCallerBeforAccept --------------------");

		EndPointEvent endPointEvent;
		CallEvent callEvent;

		// 1 - clientEndPoint.dial() >>> C:------- DIAL REQUEST ------->:S >>>
		// INCOMING_CALL
		// CALL_RINGING <<< C:<--- DIAL REQUEST ARRIVED ---:S
		log.info(clientName1 + " dial to " + serverName + "...");
		CallListenerImpl clientCallListener = new CallListenerImpl(clientName1);
		Call clientCall = clientEndPoint1.dial(serverEndPoint.getUri(),
				clientCallListener);
		log.info("OK");

		log.info(serverName + " expects incoming call from " + clientName1
				+ "...");
		endPointEvent = serverEndPointListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", endPointEvent);
		Assert.assertEquals("Bad message received in server UA: "
				+ endPointEvent.getEventType(), EndPointEvent.INCOMING_CALL,
				endPointEvent.getEventType());
		Call serverCall = endPointEvent.getCallSource();
		CallListenerImpl serverCallListener = new CallListenerImpl(serverName);
		serverCall.addListener(serverCallListener);
		log.info("OK");

		log.info(clientName1 + " expects ringing from " + serverName + "...");
		callEvent = clientCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_RINGING,
				callEvent.getEventType());
		log.info("OK");

		// 2 - CALL_REJECT <<< C:<-------------- REJECT CALL ---:S <<<
		// serverCall.terminate()
		// C:--- REJECT CALL OK------------>:S >>> CALL_REJECT
		log.info(serverName + " rejects...");
		serverCall.terminate();
		log.info("OK");

		log.info(clientName1 + " expects call reject from " + serverName
				+ "...");
		callEvent = clientCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_REJECT,
				callEvent.getEventType());
		log.info("OK");

		log.info(serverName + " expects call reject...");
		callEvent = serverCallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", callEvent);
		Assert.assertEquals(
				"Bad message received in server UA: "
						+ callEvent.getEventType(), CallEvent.CALL_REJECT,
				callEvent.getEventType());
		log.info("OK");

		log.info(" -------------------- testCallSetupAndDropFromCallerBeforAccept finished OK --------------------");
	}

	/**
	 * Verify the EndPoint is able to receive simultaneously 2 or more dials.
	 * 
	 * <pre>
	 *  1 -  clientEndPoint1.dial() >>> C1:--- DIAL REQUEST ------------->:S >>> INCOMING_CALL
	 *                 CALL_RINGING <<< C1:<----- DIAL REQUEST ARRIVED ---:S
	 *  2 -  clientEndPoint2.dial() >>> C2:--- DIAL REQUEST ------------->:S >>> INCOMING_CALL
	 *                 CALL_RINGING <<< C2:<----- DIAL REQUEST ARRIVED ---:S
	 *  3 -             CALL_REJECT <<< C1:<-------------- REJECT CALL ---:S <<< serverCall1.terminate()
	 *                                  C1:--- REJECT CALL OK------------>:S >>> CALL_REJECT
	 *  4 -             CALL_REJECT <<< C2:<-------------- REJECT CALL ---:S <<< serverCall2.terminate()
	 *                                  C2:--- REJECT CALL OK------------>:S >>> CALL_REJECT
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDialsFrom2Callers() throws Exception {
		log.debug("-------------------- testDialsFrom2Callers --------------------");

		EndPointEvent endPointEvent;
		CallEvent callEvent;

		// 1 - clientEndPoint1.dial() >>> C1:--- DIAL REQUEST ------------->:S
		// >>> INCOMING_CALL
		// CALL_RINGING <<< C1:<----- DIAL REQUEST ARRIVED ---:S
		log.info(clientName1 + " dial to " + serverName + "...");
		CallListenerImpl client1CallListener = new CallListenerImpl(clientName1);
		Call client1Call = clientEndPoint1.dial(serverEndPoint.getUri(),
				client1CallListener);
		log.info("OK");

		log.info(serverName + " expects incoming call from " + clientName1
				+ "...");
		endPointEvent = serverEndPointListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", endPointEvent);
		Assert.assertEquals("Bad message received in server UA: "
				+ endPointEvent.getEventType(), EndPointEvent.INCOMING_CALL,
				endPointEvent.getEventType());
		Call serverCall1 = endPointEvent.getCallSource();
		CallListenerImpl serverCallListener1 = new CallListenerImpl(serverName);
		serverCall1.addListener(serverCallListener1);
		log.info("OK");

		log.info(clientName1 + " expects ringing from " + serverName + "...");
		callEvent = client1CallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_RINGING,
				callEvent.getEventType());
		log.info("OK");

		// 2 - clientEndPoint2.dial() >>> C2:--- DIAL REQUEST ------------->:S
		// >>> INCOMING_CALL
		// CALL_RINGING <<< C2:<----- DIAL REQUEST ARRIVED ---:S
		log.info(clientName2 + " dial to " + serverName + "...");
		CallListenerImpl client2CallListener = new CallListenerImpl(clientName2);
		Call client2Call = clientEndPoint2.dial(serverEndPoint.getUri(),
				client2CallListener);
		log.info("OK");

		log.info(serverName + " expects incoming call from " + clientName2
				+ "...");
		endPointEvent = serverEndPointListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", endPointEvent);
		Assert.assertEquals("Bad message received in server UA: "
				+ endPointEvent.getEventType(), EndPointEvent.INCOMING_CALL,
				endPointEvent.getEventType());
		Call serverCall2 = endPointEvent.getCallSource();
		CallListenerImpl serverCallListener2 = new CallListenerImpl(serverName);
		serverCall2.addListener(serverCallListener2);
		log.info("OK");

		log.info(clientName2 + " expects ringing from " + serverName + "...");
		callEvent = client2CallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_RINGING,
				callEvent.getEventType());
		log.info("OK");

		// 3 - CALL_REJECT <<< C1:<-------------- REJECT CALL ---:S <<<
		// serverCall1.terminate()
		// C1:--- REJECT CALL OK------------>:S >>> CALL_REJECT
		log.info(serverName + " rejects ..." + clientName1);
		serverCall1.terminate();
		log.info("OK");

		log.info(clientName1 + " expects call reject from " + serverName
				+ "...");
		callEvent = client1CallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_REJECT,
				callEvent.getEventType());
		log.info("OK");

		log.info(serverName + " expects call reject...");
		callEvent = serverCallListener1.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", callEvent);
		Assert.assertEquals(
				"Bad message received in server UA: "
						+ callEvent.getEventType(), CallEvent.CALL_REJECT,
				callEvent.getEventType());
		log.info("OK");

		// 4 - CALL_REJECT <<< C2:<-------------- REJECT CALL ---:S <<<
		// serverCall2.terminate()
		// C2:--- REJECT CALL OK------------>:S >>> CALL_REJECT
		log.info(serverName + " rejects ..." + clientName2);
		serverCall2.terminate();
		log.info("OK");

		log.info(clientName2 + " expects call reject from " + serverName
				+ "...");
		callEvent = client2CallListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", callEvent);
		Assert.assertEquals(
				"Bad message received in client UA: "
						+ callEvent.getEventType(), CallEvent.CALL_REJECT,
				callEvent.getEventType());
		log.info("OK");

		log.info(serverName + " expects call reject...");
		callEvent = serverCallListener2.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in server UA", callEvent);
		Assert.assertEquals(
				"Bad message received in server UA: "
						+ callEvent.getEventType(), CallEvent.CALL_REJECT,
				callEvent.getEventType());
		log.info("OK");

		log.info(" -------------------- testDialsFrom2Callers finished OK --------------------");
	}

}
