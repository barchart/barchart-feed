package com.barchart.feed.api.framework;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.barchart.feed.api.consumer.data.Exchange;
import com.barchart.feed.api.consumer.data.Instrument;

public class SubscriptionManager implements AgentLifecycleHandler {
	
	public enum Interest {
		ALL, SNAP, UPDATE
	}
	
	private final ConcurrentMap<Instrument, Interest> instruments = 
		new ConcurrentHashMap<Instrument, Interest>();
	
	private final ConcurrentMap<Exchange, Interest> exchanges =
		new ConcurrentHashMap<Exchange, Interest>();
	
	private AtomicBoolean allMarkets = new AtomicBoolean(false);
	
	@Override
	public synchronized void attachAgent(final FrameworkAgent<?> agent) {
		
		
		
	}
	
	@Override
	public synchronized void updateAgent(final FrameworkAgent<?> agent) {
		
		
		
	}

	@Override
	public synchronized void detachAgent(final FrameworkAgent<?> agent) {
		
		
		
	}

}
