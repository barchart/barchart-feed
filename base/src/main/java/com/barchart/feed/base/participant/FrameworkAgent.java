package com.barchart.feed.base.participant;

import java.util.Set;

import com.barchart.feed.api.Agent;
import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.model.meta.Instrument;

public interface FrameworkAgent<V extends MarketData<V>> extends Agent {

	Class<V> type();

	MarketObserver<V> callback();

	V data(Market market);

	/* Filter methods */
	boolean accept(Instrument instrument);

	Set<String> interests();

}
