package com.barchart.feed.api.connection;

import com.barchart.util.common.anno.Mutable;
import com.barchart.util.common.anno.UsedOnce;
import com.barchart.util.common.anno.aQute.bnd.annotation.ConsumerType;
import com.barchart.util.common.anno.aQute.bnd.annotation.ProviderType;

/**
 * Logical end-user connection to the remote feed services.
 * <p>
 * Only one connection can be active at a time.
 * <p>
 * Can contain multiple underlying physical connections over different
 * transports and to different servers.
 */
@Mutable
@ProviderType
public interface Connection {

	/**
	 * Supported connection states.
	 * <p>
	 * Represent transport events and not related to session state.
	 */
	enum State {
		CREATED, //
		CONNECTING, //
		CONNECTED, //
		DISCONNECTING, //
		DISCONNECTED, //
		TERMINATED, //
	}

	/**
	 * Current connection state.
	 */
	State state();

	/**
	 * Connection state listener.
	 */
	@ConsumerType
	interface Monitor {

		/**
		 * Connection state change notification.
		 */
		void handle(State state, Connection connection);

	}

	/**
	 * Connection builder.
	 */
	interface Builder {

		/**
		 * Remote configuration discovery URL.
		 * <p>
		 * Example https://feed.barchart.com/connection
		 */
		Builder discovery(String remoteURL);

		/**
		 * Optional connection monitor.
		 */
		Builder monitor(Monitor monitor);

		/**
		 * User-configurable connection properties.
		 */
		Builder property(String key, String value);

		/**
		 * Used-once username.
		 */
		@UsedOnce
		Builder username(String username);

		/**
		 * Used-once password.
		 */
		@UsedOnce
		Builder password(String password);

		/**
		 * Terminate existing connection (if any), and create a new connection.
		 */
		Connection build();

	}

	/**
	 * Session bound to the connection.
	 */
	Session session();

	/**
	 * Terminate the connection. Terminated connection can not be used again.
	 */
	void terminate();
	
}
