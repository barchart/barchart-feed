package com.barchart.feed.api;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

import com.barchart.feed.api.connection.ConnectionLifecycle;
import com.barchart.feed.api.connection.ConnectionStateListener;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.enums.MarketEventType;
import com.barchart.feed.api.inst.InstrumentService;

public interface Feed extends ConnectionLifecycle, InstrumentService<CharSequence>,
		AgentBuilder {

	/* ***** ***** ConnectionLifecycle ***** ***** */
	
	@Override
	void startup();
	
	@Override
	void shutdown();
	
	@Override
	void bindConnectionStateListener(ConnectionStateListener listener);
	
	/* ***** ***** InstrumentService ***** ***** */
	
	@Override
	Instrument lookup(CharSequence symbol);
	
	@Override
	Future<Instrument> lookupAsync(CharSequence symbol);
	
	@Override
	Map<CharSequence, Instrument> lookup(
			Collection<? extends CharSequence> symbols);
	
	@Override
	Map<CharSequence, Future<Instrument>> lookupAsync(
			Collection<? extends CharSequence> symbols);
	
	/* ***** ***** AgentBuilder ***** ***** */
	
	@Override
	<V extends MarketData<V>> Agent newAgent(MarketData.Type type, 
			MarketCallback<V> callback,	MarketEventType... types);
	
	
}
