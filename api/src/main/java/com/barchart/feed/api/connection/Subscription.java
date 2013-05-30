package com.barchart.feed.api.connection;

import java.util.Set;

/**
 * Subscription are for either instruments or exchanges
 * 
 *  All?
 * 
 * @author Gavin M Litchfield
 *
 */
public interface Subscription<V> {

	Set<SubscriptionType> types();
	
	V interest();
	String interestName();
	
	void addTypes(Set<SubscriptionType> types);
	void removeTypes(Set<SubscriptionType> types);
	
	String subscribe();
	String unsubscribe();
	
}
