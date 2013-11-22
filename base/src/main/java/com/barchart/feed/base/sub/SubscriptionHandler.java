package com.barchart.feed.base.sub;

import java.util.Set;
import java.util.concurrent.Future;

public interface SubscriptionHandler {
	
	Future<Boolean> subscribe(Sub subscription);
	Future<Boolean> subscribe(Set<Sub> subscriptions);
	
	Future<Boolean> unsubscribe(Sub subscription);
	Future<Boolean> unsubscribe(Set<Sub> subscriptions);

}
