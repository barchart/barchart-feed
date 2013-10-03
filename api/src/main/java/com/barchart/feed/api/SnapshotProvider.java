package com.barchart.feed.api;

import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Identifier;

public interface SnapshotProvider {
	
	Market snapshot(Instrument instrument);
	
	Market snapshot(Identifier instID);
	
	Market snapshot(String symbol);

}
