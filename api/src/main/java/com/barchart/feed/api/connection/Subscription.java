package com.barchart.feed.api.connection;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.util.value.api.Existential;
import com.barchart.util.value.api.Time;

public interface Subscription<T extends Metadata> extends Existential {

	enum SubscriptionType {
		NULL, REALTIME, FIFTEEN_MIN_DELAYED
	}
	
	SubscriptionType type();
	
	T metadata();
	
	Time subscribed(); // ?? Name
	
	
	
	@Override
	boolean isNull();
	
	// NULL 
	
	
}
