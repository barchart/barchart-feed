package com.barchart.feed.api.connection;

import aQute.bnd.annotation.ProviderType;

import com.barchart.util.anno.Mutable;

/**
 * Logical conversation with remote feed service.
 * <p>
 * Session outlives the connection.
 */
@Mutable
@ProviderType
public interface Session {
	
	/**
	 * Unique session token GUID, used to authenticate additional services.
	 */
	SessionID id();

	/**
	 * Supported session states.
	 * <p>
	 * Represent session events and not related to transport .
	 */
	enum State {
		CREATED, //
		EXPIRED, //
		TERMINATED, //
	}

	/**
	 * Current session state.
	 */
	State state();

	/**
	 * Session state listener
	 */
	interface Monitor {

		void handle(State state, Session session);

	}

	/**
	 * Terminate the session. Terminated session can not be used again.
	 * <p>
	 * Not related to connection.
	 */
	void terminate();

}