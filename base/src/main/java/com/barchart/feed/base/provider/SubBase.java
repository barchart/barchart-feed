package com.barchart.feed.base.provider;

import java.util.EnumSet;
import java.util.Set;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.feed.api.model.meta.Metadata.MetaType;
import com.barchart.feed.api.model.meta.id.ExchangeID;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.MetadataID;
import com.barchart.feed.base.sub.SubCommand;
import com.barchart.feed.base.sub.SubscriptionType;

public class SubBase implements SubCommand {
	
	private final MetadataID<?> interestID;
	private final Set<SubscriptionType> subTypes;
	
	SubBase(
			final MetadataID<?> interestID,
			final Set<SubscriptionType> types) {
		
		if(interestID == null || types == null || types.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		this.interestID = interestID;
		this.subTypes = EnumSet.copyOf(types);
	}

	@Override
	public MetadataID<?> interestID() {
		return interestID;
	}
	
	@Override
	public Metadata.MetaType metaType() {
		
		if(interestID instanceof InstrumentID) {
			return MetaType.INSTRUMENT;
		}
		
		if(interestID instanceof ExchangeID) {
			return MetaType.EXCHANGE;
		}
		
		return MetaType.NULL;
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
	public String typeString() {
		
		if(subTypes.isEmpty()) {
			throw new IllegalStateException("Subscription type set is empty");
		}
		
		final StringBuilder sb = new StringBuilder();
		for(final SubscriptionType t : subTypes) {
			sb.append(t.code());
		}
		
		return sb.toString();
		
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
