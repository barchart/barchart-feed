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
public interface Subscription {

	boolean isNull();
	
	Set<SubscriptionType> types();
	void addTypes(Set<SubscriptionType> types);
	void removeTypes(Set<SubscriptionType> types);
	
	String encode();
	
	String subscribe();
	String unsubscribe();
	
	public static Subscription NULL_SUBSCRIPTION = new Subscription() {

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public Set<SubscriptionType> types() {
			return null;
		}

		@Override
		public void addTypes(Set<SubscriptionType> types) {
			
		}

		@Override
		public void removeTypes(Set<SubscriptionType> types) {
			
		}

		@Override
		public String encode() {
			return null;
		}

		@Override
		public String subscribe() {
			return null;
		}

		@Override
		public String unsubscribe() {
			return null;
		}
		
	};
	
}
