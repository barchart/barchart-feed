package com.barchart.feed.api.connection;

public interface ConnectionLifecycle {

	void startup();
	
	void startUpProxy();
	
	void shutdown();
	
	void bindConnectionStateListener(ConnectionStateListener listener);
	
}
