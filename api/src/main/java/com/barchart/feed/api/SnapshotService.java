package com.barchart.feed.api;

import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;

public interface SnapshotService {

	Market snapshot(final Instrument instrument);
	
	Market snapshot(final InstrumentID instID);
	
	Market snapshot(final String symbol);
	
}
