package com.kurento.ua.commons.junit;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurento.ua.commons.EndPoint;
import com.kurento.ua.commons.EndPointEvent;
import com.kurento.ua.commons.junit.util.EndPointListenerImpl;

public class RegisterTimeOutTest extends RegisterTest {

	private final static Logger log = LoggerFactory
			.getLogger(RegisterTimeOutTest.class);

	/**
	 * Verify the UA receive a REGISTER_USER_FAIL after a timeout when the
	 * register request has problems.
	 * 
	 * <pre>
	 *  1 - clientUA.registerEndpoint() >>> C:--- REGISTER REQUEST --->:S
	 *               REGISTER_USER_FAIL <<< C:<--- REGISTER TIMEOUT
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRegisterTimeOut() throws Exception {
		log.debug("-------------------- testRegisterTimeOut --------------------");

		// 1 - clientUA.registerEndpoint() >>> C:--- REGISTER REQUEST --->:S
		// REGISTER_USER_FAIL <<< C:<--- REGISTER TIMEOUT
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

		log.info(" -------------------- testRegisterTimeOut finished OK --------------------");
	}

}
