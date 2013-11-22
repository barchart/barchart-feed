package com.barchart.feed.api.connection;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.util.value.api.Existential;

public interface Subscription<T extends Metadata> extends Existential {

	enum Lense {
		REALTIME, DELAYED, REPLAY
	}
	
	Lense lense();
	
	/**
	 * 
	 * @return The meta-data object this subscription is for.
	 */
	T metadata();
	
	@Override
	boolean isNull();
	
//	Time subscribed(); // ?? Name
	
//	Set<Class<? extends MarketData<?>>> dataTypes();
	
//	/**
//	 * @return The time delay in seconds of this subscription, 0 = realtime
//	 */
//	int delay();
//	
//	/**
//	 * @return The frequency in seconds of snapshot messages
//	 */
//	int snapshotFrequency();
	
	// NULL 
	
//	- Update frequency (every message, snapshots every 10 seconds, etc)
	
}
