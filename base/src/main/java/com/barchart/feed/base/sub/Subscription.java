package com.barchart.feed.base.sub;

import java.util.Set;

/**
 * Subscription are for either instruments or exchanges
 * 
 *  All?
 *  
 *  This is pretty ad-hoc atm, needs to be looked at again
 *  should use MetaData interface now
 * 
 * @author Gavin M Litchfield
 *
 */
public interface Subscription {
	
	enum Type {
		NULL, INSTRUMENT, EXCHANGE
	}
	
	Type type();

	boolean isNull();
	
	Set<SubscriptionType> types();
	void addTypes(Set<SubscriptionType> types);
	void removeTypes(Set<SubscriptionType> types);
	
	String interest();
	String encode();
	
	public static Subscription NULL = new Subscription() {

		@Override
		public Type type() {
			return Type.NULL;
		}
		
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
		public String interest() {
			return null;
		}
		
	};
	
}
