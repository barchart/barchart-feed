package com.barchart.feed.api.model.meta.id;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.feed.api.model.meta.Metadata.MetaType;


public class InstrumentID extends MetadataID<InstrumentID> {

	@Deprecated
	public InstrumentID(final String id) {
		super(id, InstrumentID.class);
	}

	public InstrumentID(final long id) {
		super(String.valueOf(id), InstrumentID.class);
	}

	public static final InstrumentID NULL = new InstrumentID(Long.MIN_VALUE) {

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
