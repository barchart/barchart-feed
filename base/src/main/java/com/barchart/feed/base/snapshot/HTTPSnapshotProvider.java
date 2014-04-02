package com.barchart.feed.base.snapshot;

import rx.Observable;

import com.barchart.feed.api.consumer.MarketSnapshotService;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.meta.id.InstrumentID;

public class HTTPSnapshotProvider implements MarketSnapshotService  {

	@Override
	public Observable<Market> snapshot(InstrumentID instrument) {
		
		
		
		return null;
	}

}
