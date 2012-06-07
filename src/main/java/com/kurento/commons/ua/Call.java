package com.kurento.commons.ua;

import java.util.Map;

import com.kurento.commons.media.format.enums.MediaType;
import com.kurento.commons.media.format.enums.Mode;
import com.kurento.commons.mscontrol.networkconnection.NetworkConnection;
import com.kurento.commons.ua.exception.ServerInternalErrorException;

/**
 * The Call provides a management interface to a Dialog between the local and
 * remote User Agent with an interface designed to emulate the phone model of
 * use as much as possible.
 * <p>
 * Call can be created by calling the
 * {@link EndPoint#dial(String, CallListener) dial} method or as a result of an
 * INVITE message reception. First calls are called outgoing (initiated by the
 * local peer) while the seconds are called incoming (initiated by the remote
 * peer)
 * 
 * @author Kurento
 * 
 */
public interface Call {

	// Control interface
	/**
	 * This method provides the control mechanism to accept incoming calls
	 * notified by {@link EndPoint}. A 200 OK message will be sent to the remote
	 * peer. Before incoming calls are notified to the listener it has been
	 * verified a common format exists for the communication, otherwise the call
	 * is automatically rejected silently
	 * 
	 * @throws ServerInternalErrorException
	 *             If call is not incoming or Dialog is not in early state an
	 *             Exception will be generated
	 */
	public void accept() throws ServerInternalErrorException;

	/**
	 * Provides the control mechanism to terminate this call
	 * 
	 * @throws ServerInternalErrorException
	 *             If the call is not active
	 * 
	 */
	public void terminate() throws ServerInternalErrorException;

	/**
	 * Provides the control mechanism to terminate this call
	 * 
	 * @param code
	 *            Code with the reason of call termination. Only used when a
	 *            incoming call is rejected
	 * @throws ServerInternalErrorException
	 *             If the call is not active
	 */
	public void terminate(TerminateReason code)
			throws ServerInternalErrorException;

	// monitor interface
	/**
	 * Provides information on connection state. TRUE value means the call is
	 * setup and active and Dialog is in state CONFIRMED
	 */
	public Boolean isConnected();

	// Callbacks
	/**
	 * Add a new listener object that will receive call events
	 */
	public void addListener(CallListener listener);

	/**
	 * Remove an object from the list of elements receiving call event
	 * notifications
	 * 
	 * @param listener
	 */
	public void removeListener(CallListener listener);

	// Media
	/**
	 * This method returns the NetworkConnection associated to this call.
	 * NetworkConnection is the class that actually performs the media
	 * negotiation and provides control interface to the media EndPoint. Once
	 * the call setup is completed successfully, the network connection can be
	 * used to access the streams (audio and video) to connect to devices
	 * (camera, display...)
	 * 
	 * @return NetworkConnection associated with the call
	 * 
	 */
	public NetworkConnection getNetworkConnection();

	/**
	 * Returns the connection mode for each media type. Available modes are
	 * <ul>
	 * <li>SENDRECV: Both peers will send and receive media
	 * <li>RECV: Local peer will receive media send by remote peer
	 * <li>SEND: remote peer will receive media send by local peer
	 * </ul>
	 * Connection mode is independent for each media channel. That means a peer
	 * can be sending video to the other with DUPLEX audio connection
	 * 
	 * @return map with connection modes for each media type
	 */
	public Map<MediaType, Mode> getMediaTypesModes();

	/**
	 * Get Uri of remote peer for this call
	 * 
	 * @return String with remote peer URI
	 */
	public String getRemoteUri();

	/**
	 * Get display name of remote peer for this call. This can be used by
	 * applications to provide a friendly interface for the call
	 * 
	 * @return String with display name of remote peer
	 */
	public String getRemoteDisplayName();

	/**
	 * Returns the call identifier
	 * 
	 * @return The call identifier
	 */
	public String getId();

	/**
	 * Get extra attributes set in this call
	 * 
	 * @return The attributes class
	 */
	public CallAttributes getAttributes();
}
