package com.barchart.feed.base.provider;

import java.util.EnumSet;
import java.util.Set;

import com.barchart.feed.base.sub.Sub;
import com.barchart.feed.base.sub.SubscriptionType;

public class SubscriptionBase implements Sub {
	
	private final Set<SubscriptionType> subTypes;
	private final String interest;
	private final Sub.Type type;
	
	SubscriptionBase(final String interest, Sub.Type type, 
			final Set<SubscriptionType> types) {
		
		if(interest == null || types == null || types.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		this.subTypes = EnumSet.copyOf(types);
		this.interest = interest;
		this.type = type;
	}

	@Override
	public Type type() {
		return type;
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
