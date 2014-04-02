package com.barchart.feed.api.consumer;

import rx.Observable;

import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.meta.id.InstrumentID;

public interface MarketSnapshotService {

	/**
	 * 
	 * @param instrument
	 * @return
	 */
	Observable<Market> snapshot(InstrumentID instrument);
	
}
