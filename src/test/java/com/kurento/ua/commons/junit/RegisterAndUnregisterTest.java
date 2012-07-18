package com.kurento.ua.commons.junit;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurento.ua.commons.EndPoint;
import com.kurento.ua.commons.EndPointEvent;
import com.kurento.ua.commons.junit.util.EndPointListenerImpl;

public class RegisterAndUnregisterTest extends RegisterTest {

	private final static Logger log = LoggerFactory
			.getLogger(RegisterAndUnregisterTest.class);

	@BeforeClass
	public static void setup() throws Exception {
		log.debug("setup");
		EndPointListenerImpl serverEndPointListener = new EndPointListenerImpl(
				serverName);
		EndPoint serverEndPoint = serverUA.registerEndpoint(serverName,
				"kurento.com", serverEndPointListener, sEpConfig);
	}

	@AfterClass
	public static void tearDown() {
		log.debug("tearDown");
		serverUA.terminate();
	}

	@Test
	public void testRegisterAndUnregister() throws Exception {
		log.debug("-------------------- testRegisterAndUnregister --------------------");

		log.info("Register user " + clientName + "...");
		EndPointListenerImpl clientEndPointListener = new EndPointListenerImpl(
				clientName);
		EndPoint clientEndPoint = clientUA.registerEndpoint(clientName,
				"kurento.com", clientEndPointListener, cEpConfig);

		EndPointEvent endPointEvent = clientEndPointListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", endPointEvent);
		Assert.assertEquals("Bad message received in client UA: "
				+ endPointEvent.getEventType(),
				EndPointEvent.REGISTER_USER_SUCESSFUL,
				endPointEvent.getEventType());
		log.info("OK");

		log.info("Implicit un-register of user " + clientName + "...");
		clientEndPoint.terminate();
		endPointEvent = clientEndPointListener.poll(WAIT_TIME);
		Assert.assertNotNull("No message received in client UA", endPointEvent);
		Assert.assertEquals("Bad message received in client UA: "
				+ endPointEvent.getEventType(),
				EndPointEvent.UNREGISTER_USER_SUCESSFUL,
				endPointEvent.getEventType());
		log.info("OK");

		// Wait a moment
		Thread.sleep(500);
		log.info("Terminate user " + clientName
				+ "... Verify no register request is sent");
		clientEndPoint.terminate();
		endPointEvent = clientEndPointListener.poll(WAIT_TIME);
		Assert.assertNull("Client UA sent unregister twice", endPointEvent);

		// Check no register is sent when UA terminates
		clientUA.terminate();
		endPointEvent = clientEndPointListener.poll(WAIT_TIME);
		Assert.assertNull("Client UA sent unregister twice", endPointEvent);

		log.info(" -------------------- testRegisterAndUnregister finished OK --------------------");
	}

}
