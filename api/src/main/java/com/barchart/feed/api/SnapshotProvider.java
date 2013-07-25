package com.barchart.feed.api;

import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.meta.Instrument;

public interface SnapshotProvider {
	
	Market snapshot(Instrument instrument);
	
	Market snapshot(String symbol);

}
