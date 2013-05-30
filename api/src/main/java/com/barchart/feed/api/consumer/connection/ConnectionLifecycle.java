package com.barchart.feed.api.consumer.connection;

public interface ConnectionLifecycle {

	void startup();
	
	void startUpProxy();
	
	void shutdown();
	
}
