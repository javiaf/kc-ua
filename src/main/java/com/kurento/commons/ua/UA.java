package com.kurento.commons.ua;

import java.util.Map;

import com.kurento.commons.ua.exception.ServerInternalErrorException;

/**
 * The User Agent (UA) is the common interface provided by communications
 * components to applications. There will be an UA implementation for each
 * domain and platform where Kurento connects <br>
 * The UA can host multiple EndPoints, each one with a single URI representing a
 * service point or user. The UA can host multiple EndPoints, being able to
 * manage simultaneously communications for different users. <br>
 * Th User Agent is the software element that manages protocol internals. Its
 * functions are:
 * <ul>
 * <li>Encode and decode messages. It must be able to handle header values
 * <li>Send and receive packets
 * <li>Register and un-register URI contacts. It manages a list of active
 * {@link EndPoint}
 * <li>Multiplex messages among EndPoints. Automatically discard messages not
 * addressed to any known EndPoint
 * 
 * @version 1.0.0
 */
public interface UA {

	/**
	 * Terminate the UA. This process will perform the procedure below
	 * <ul>
	 * <li>Terminate all Call managed by this UA
	 * <li>Un-register URI from all EndPoint
	 * <li>Terminate the stack and free the network socket
	 * </ul>
	 */
	public void terminate();

	/**
	 * Allows the application to register a service Endpoint connected to the UA
	 * communication domain.
	 * 
	 * @param uriUser
	 *            Together with the domain provides the Service Endpoint name,
	 *            i.e. the id used by this Endpoint to become uniquelly
	 *            identified within the communication domain where the UA is
	 *            connected
	 * @param domain
	 * @param listener
	 *            Native listener the Endpoint will send events
	 * @param extra
	 *            A set of extra configuration parameters given as [key, value]
	 *            pairs, required by the UA to set up the Endpoint. Refer to
	 *            specific UA implementations to find out the extra
	 *            configuration settings
	 * @return
	 * @throws ServerInternalErrorException
	 */
	public EndPoint registerEndpoint(String uriUser, String domain,
			EndPointListener listener, Map<String, Object> extra)
			throws ServerInternalErrorException;

	/**
	 * Allows the application to register a service Endpoint connected to the UA
	 * communication domain.
	 * 
	 * @param uriUser
	 *            Together with the domain provides the Service Endpoint name,
	 *            i.e. the id used by this Endpoint to become uniquelly
	 *            identified within the communication domain where the UA is
	 *            connected
	 * @param domain
	 * @param listener
	 *            Native listener the Endpoint will send events
	 * @param user
	 *            The user for authenticating registering process
	 * @param passwd
	 *            The password for authenticating registering process
	 * @param extra
	 *            A set of extra configuration parameters given as [key, value]
	 *            pairs, required by the UA to set up the Endpoint. Refer to
	 *            specific UA implementations to find out the extra
	 *            configuration settings
	 * @return
	 * @throws ServerInternalErrorException
	 */
	public EndPoint registerEndpoint(String uriUser, String domain,
			EndPointListener listener, String user, String passwd,
			Map<String, Object> extra) throws ServerInternalErrorException;

	public void unregisterEndpoint(EndPoint endpoint) throws ServerInternalErrorException;
}