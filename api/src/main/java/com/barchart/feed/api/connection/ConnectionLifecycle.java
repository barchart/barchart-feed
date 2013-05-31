package com.barchart.feed.api.connection;

public interface ConnectionLifecycle {

	void startup();
	
	void shutdown();
	
	void bindConnectionStateListener(ConnectionStateListener listener);
	
}
