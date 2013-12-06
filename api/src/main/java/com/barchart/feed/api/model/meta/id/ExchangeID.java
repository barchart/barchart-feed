package com.barchart.feed.api.model.meta.id;

import com.barchart.util.common.identifier.Identifier;

public class ExchangeID extends Identifier<String, ExchangeID> {
	
	public ExchangeID(final String id) {
		super(id, ExchangeID.class);
	}
	
	public static final ExchangeID NULL = new ExchangeID("NULL_EXCHANGE_ID") {
		
		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
