package com.barchart.feed.api.connection;

import java.util.Set;
import java.util.concurrent.Future;

public interface SubscriptionHandler {
	
	Future<Boolean> subscribe(Subscription<?> subscription);
	Future<Boolean> subscribe(Set<Subscription<?>> subscriptions);
	
	Future<Boolean> unsubscribe(Subscription<?> subscription);
	Future<Boolean> unsubscribe(Set<Subscription<?>> subscriptions);

}
