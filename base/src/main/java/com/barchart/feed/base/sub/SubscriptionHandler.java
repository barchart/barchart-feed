package com.barchart.feed.base.sub;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import com.barchart.feed.api.model.meta.id.MetadataID;

public interface SubscriptionHandler {
	
	Future<Boolean> subscribe(Set<SubCommand> subscriptions);
	
	Future<Boolean> unsubscribe(Set<SubCommand> subscriptions);

	Map<MetadataID<?>, SubCommand> subscriptions();
	
}
