package com.barchart.feed.api.model.meta.id;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.feed.api.model.meta.Metadata.MetaType;


public class InstrumentID extends MetadataID<InstrumentID> {

	public InstrumentID(final String id) {
		super(id, InstrumentID.class);
	}
	
	public static final InstrumentID NULL = new InstrumentID("NULL_INSTRUMENT_ID") {
		
		@Override
		public boolean isNull() {
			return true;
		}
		
	};
	
	@Override
	public Metadata.MetaType metaType() {
		return MetaType.INSTRUMENT;
	}

}
