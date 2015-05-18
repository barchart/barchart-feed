package com.barchart.feed.base.sub;

import java.util.Set;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.feed.api.model.meta.id.MetadataID;

/**
 * Subscription are for either instruments or exchanges
 */
public interface SubCommand {
	
	MetadataID<?> interestID();
	
	Metadata.MetaType metaType();

	boolean isNull();
	
	Set<SubscriptionType> types();
	void addTypes(Set<SubscriptionType> types);
	void removeTypes(Set<SubscriptionType> types);
	
	String typeString();
	
	@SuppressWarnings("rawtypes")
	public static SubCommand NULL = new SubCommand() {
		
		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public Metadata.MetaType metaType() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Set<SubscriptionType> types() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addTypes(Set types) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeTypes(Set types) {
			throw new UnsupportedOperationException();
		}

		@Override
		public MetadataID<?> interestID() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String typeString() {
			throw new UnsupportedOperationException();
		}

	};
	
}
