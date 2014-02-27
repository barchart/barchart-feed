package com.barchart.feed.api.model.meta.id;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.feed.api.model.meta.Metadata.MetaType;

public class ExchangeID extends MetadataID<ExchangeID> {
	
	public ExchangeID(final String id) {
		super(id, ExchangeID.class);
	}
	
	public static final ExchangeID NULL = new ExchangeID("NULL_EXCHANGE_ID") {
		
		@Override
		public boolean isNull() {
			return true;
		}
		
	};

	@Override
	public Metadata.MetaType metaType() {
		return MetaType.EXCHANGE;
	}
	
}
