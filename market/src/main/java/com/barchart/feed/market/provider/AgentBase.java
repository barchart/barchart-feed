package com.barchart.feed.market.provider;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.market.MarketMessage;
import com.barchart.feed.api.market.data.MarketDataObject;
import com.barchart.feed.market.api.FrameworkAgent;
import com.barchart.feed.market.api.FrameworkMarketplace;
import com.barchart.feed.market.api.Market;
import com.barchart.missive.api.Tag;

public class AgentBase implements FrameworkAgent {
	
	// Not exactly sure what I will do with markets, but
	// it could allow for lazy removal of agents
	private final Set<Market> markets = Collections.newSetFromMap(
			new ConcurrentHashMap<Market, Boolean>());
	
	private final AtomicBoolean active = new AtomicBoolean(true);
	private final AtomicBoolean dismiss = new AtomicBoolean(false);
	

	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public void attach(final Market market) {
		
		if(market == null) {
			return;
		}
		
		markets.add(market);
	}

	// This seems redundant
	@Override
	public void update(final Market market) {
		
		if(market == null) {
			return;
		}
		
	}

	@Override
	public void detach(final Market market) {
		
		if(market == null) {
			return;
		}
		
		markets.remove(market);
		
	}

	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public <M extends MarketDataObject<M>> void handle(final Market market,
			final MarketMessage<?> message, final MarketDataObject<M> data) {
		
		if(!active.get()) {
			return;
		}
		
		if(dismiss.get()) {
			market.detach(this);
			return;
		}
		
		// check if market is registered
		// not sure what it would mean if handle was called on an unregistered market
		if(!markets.contains(market)) {
			// market.attach(this);
			// or
			// market.detach(this);
		}
		
		// CALLBACK AT ME BRO
		
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	// built by Builder or whatever
	
	@Override
	public Tag<?>[] tagsToListenTo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean filter(final Instrument m) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	private volatile FrameworkMarketplace marketplace = null;
	
	// This should go away if the marketplace provides the agent builders
	// For now we'll just have it
	@Override
	public void bindMarketplace(final FrameworkMarketplace marketplace) {
		this.marketplace = marketplace;
	}

	// This will be set in factory or something
	@Override
	public <M extends MarketDataObject<M>> Tag<M> callbackDataObjectTag() {
		// TODO Auto-generated method stub
		return null;
	}

	/* ***** ***** Lifecycle methods ***** ***** */
	
	@Override
	public void activate() {
		active.set(true);
	}
	
	@Override
	public void deactivate() {
		active.set(false);
	}

	@Override
	public void update() {
		// ??? This is a different concept than the activate / deactivate
	}

	@Override
	public void dismiss() {
		
		dismiss.set(true);
		marketplace.detachAgent(this);
		
		for(final Market market : markets) {
			market.detach(this);
		}
		
		markets.clear();
	}

	

}
