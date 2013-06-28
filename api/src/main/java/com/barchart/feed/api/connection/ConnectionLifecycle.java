package com.barchart.feed.api.connection;

public interface ConnectionLifecycle<V extends ConnectionLifecycle<V>> {

	ConnectionFuture<V> startup();
	
	ConnectionFuture<V> shutdown();
	
	/**
	 * Applications which need to react to the connectivity state of the feed
	 * instantiate a FeedStateListener and bind it to the client.
	 * 
	 * @param listener
	 *            The listener to be bound.
	 */
	void bindConnectionStateListener(Connection.Monitor listener);
	
	/**
	 * Applications which require time-stamp or heart-beat messages from the
	 * data server instantiate a DDF_TimestampListener and bind it to the
	 * client.
	 * 
	 * @param listener
	 */
	void bindTimestampListener(TimestampListener listener);
	
}
