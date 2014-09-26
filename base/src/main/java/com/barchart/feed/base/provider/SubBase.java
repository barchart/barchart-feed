package com.barchart.feed.base.provider;

import java.util.EnumSet;
import java.util.Set;

import com.barchart.feed.base.sub.SubCommand;
import com.barchart.feed.base.sub.SubscriptionType;

public class SubBase implements SubCommand {
	
	private final Set<SubscriptionType> subTypes;
	private final String interest;
	private final SubCommand.Type type;
	
	SubBase(
			final String interest, 
			final SubCommand.Type type, 
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
		final StringBuffer sb = new StringBuffer().append(interest).append("=");
		for(SubscriptionType t : subTypes) {
			sb.append(t.code());
		}
		return sb.toString();
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
