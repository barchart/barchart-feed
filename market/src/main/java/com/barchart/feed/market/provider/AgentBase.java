package com.barchart.feed.market.provider;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.barchart.feed.api.consumer.data.Instrument;
import com.barchart.feed.api.framework.FrameworkAgent;
import com.barchart.feed.api.framework.FrameworkEntity;
import com.barchart.feed.api.framework.FrameworkMarketplace;
import com.barchart.feed.api.framework.MarketEntity;
import com.barchart.feed.api.framework.MarketTag;
import com.barchart.feed.api.framework.message.Message;
import com.barchart.missive.api.Tag;

public abstract class AgentBase implements FrameworkAgent {
	
	private final Set<MarketEntity> markets = Collections.newSetFromMap(
			new ConcurrentHashMap<MarketEntity, Boolean>());
	
	private final AtomicBoolean active = new AtomicBoolean(true);
	private final AtomicBoolean dismiss = new AtomicBoolean(false);

	/* ***** ***** ***** ***** ***** ***** ***** */
	
/*	@Override
	public synchronized void attach(final MarketEntity market) {
		
		if(market == null || dismiss.get()) {
			return;
		}
		
		if(markets.contains(market)) {
			update(market);
			return;
		}
		
		markets.add(market);
		market.attach(this);
		
	}
	
	@Override
	public synchronized void update(final MarketEntity market) {
		
		if(market == null || dismiss.get()) {
			return;
		}
		
		if(!markets.contains(market)) {
			attach(market);
			return;
		}
		
		market.update(this);
		
	}

	@Override
	public synchronized void detach(final MarketEntity market) {
		
		if(market == null || !markets.contains(market)) {
			return;
		}
		
		markets.remove(market);
		market.detach(this);
		
	}
*/
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public <M extends FrameworkEntity<M>> void handle(final MarketEntity market,
			final Message message, final FrameworkEntity<M> data) {
		
		if(market == null || message == null || data == null) {
			return;
		}
		
		if(!active.get() || !markets.contains(market)) {
			return;
		}
		
		if(dismiss.get()) {
			market.detach(this);
			return;
		}
		
		// CALLBACK AT ME BRO
		
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	// built by Builder or whatever
	
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
	public <M extends FrameworkEntity<M>> MarketTag<M> callbackDataObjectTag() {
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
		
		// This should perform work on Marketplace,
		
	}

	@Override
	public void dismiss() {
		
		dismiss.set(true);
		marketplace.detachAgent(this);
		
		for(final MarketEntity market : markets) {
			market.detach(this);
		}
		
		markets.clear();
	}

	

}
