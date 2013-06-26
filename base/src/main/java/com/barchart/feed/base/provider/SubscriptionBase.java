package com.barchart.feed.base.provider;

import java.util.EnumSet;
import java.util.Set;

import com.barchart.feed.base.sub.Subscription;
import com.barchart.feed.base.sub.SubscriptionType;

public class SubscriptionBase implements Subscription {
	
	private final Set<SubscriptionType> subTypes;
	private final String interest;
	
	SubscriptionBase(final String interest, final Set<SubscriptionType> types) {
		
		if(interest == null || types == null || types.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		this.subTypes = EnumSet.copyOf(types);
		this.interest = interest;
	}

	@Override
	public Set<SubscriptionType> types() {
		return subTypes;
	}

	@Override
	public void addTypes(Set<SubscriptionType> types) {
		subTypes.addAll(types);
	}

	@Override
	public void removeTypes(Set<SubscriptionType> types) {
		subTypes.removeAll(types);
	}

	@Override
	public String encode() {
		return interest;
	}
	
	@Override
	public String interest() {
		return interest;
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
