package com.barchart.feed.base.provider;

import java.util.EnumSet;
import java.util.Set;

import com.barchart.feed.api.connection.Subscription;
import com.barchart.feed.api.connection.SubscriptionType;

public class SubscriptionBase implements Subscription {
	
	private final Set<SubscriptionType> subTypes;
	private final String code;
	
	SubscriptionBase(final String code, final Set<SubscriptionType> types) {
		
		if(code == null || types == null || types.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		this.subTypes = EnumSet.copyOf(types);
		this.code = code;
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
		return code;
	}

	@Override
	public String subscribe() {
		return "";
	}

	@Override
	public String unsubscribe() {
		return "";
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
