package com.barchart.feed.api.model.meta.id;

import com.barchart.util.value.api.identifier.Identifier;

public class InstrumentID extends Identifier<String, InstrumentID> {

	public InstrumentID(final String id) {
		super(id, InstrumentID.class);
	}
	
	public static final InstrumentID NULL = new InstrumentID("NULL_INSTRUMENT_ID") {
		
		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
