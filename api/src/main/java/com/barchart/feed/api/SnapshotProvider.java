package com.barchart.feed.api;

import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;

public interface SnapshotProvider {
	
	Market snapshot(Instrument instrument);
	
	Market snapshot(InstrumentID instID);
	
	Market snapshot(String symbol);

}
