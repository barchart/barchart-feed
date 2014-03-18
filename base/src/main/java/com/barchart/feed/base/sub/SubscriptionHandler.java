package com.barchart.feed.base.sub;

import java.util.Set;
import java.util.concurrent.Future;

public interface SubscriptionHandler {
	
	Future<Boolean> subscribe(SubCommand subscription);
	Future<Boolean> subscribe(Set<SubCommand> subscriptions);
	
	Future<Boolean> unsubscribe(SubCommand subscription);
	Future<Boolean> unsubscribe(Set<SubCommand> subscriptions);

}
