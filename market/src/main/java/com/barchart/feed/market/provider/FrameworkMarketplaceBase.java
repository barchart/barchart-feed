package com.barchart.feed.market.provider;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.market.MarketMessage;
import com.barchart.feed.api.market.data.MarketDataObject;
import com.barchart.feed.market.api.FrameworkAgent;
import com.barchart.feed.market.api.FrameworkMarketplace;
import com.barchart.feed.market.api.Market;

public class FrameworkMarketplaceBase implements FrameworkMarketplace {

	private final Set<FrameworkAgent> agents = Collections.newSetFromMap(
			new ConcurrentHashMap<FrameworkAgent, Boolean>());
	
	private final Set<Market> markets = Collections.newSetFromMap( 
			new ConcurrentHashMap<Market, Boolean>());
	
	private final ConcurrentMap<Instrument, Market> marketMap = 
			new ConcurrentHashMap<Instrument, Market>();
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public synchronized void attachAgent(final FrameworkAgent agent) {
		
		if(agent == null) {
			return;
		}
		
		/* Agent already attached */
		if(agents.contains(agent)) {
			updateAgent(agent);
			return;
		}
		
		/* Bind marketplace to agent so calls to update/dismiss can be handled */
		agent.bindMarketplace(this);
		agents.add(agent);
		
		/* Attach agent and market to each other if passes agent's filter */
		for(final Entry<Instrument, Market> e : marketMap.entrySet()) {
			
			if(agent.filter(e.getKey())) {
				agent.attach(e.getValue());
				e.getValue().attach(agent);
			}
			
		}
		
	}

	@Override
	public synchronized void updateAgent(final FrameworkAgent agent) {
		
		if(agent == null) {
			return;
		}
		
		for(final Entry<Instrument, Market> e : marketMap.entrySet()) {
			
		}
		
	}

	@Override
	public synchronized void detachAgent(final FrameworkAgent agent) {
		
		if(agent == null) {
			return;
		}
		
		
		
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	// static Market buildMarket(final Instrument inst);
	
	@Override
	public synchronized void attachMarket(final Market market) {
		
		if(market == null) {
			return;
		}
		
		/* Market already attached */
		if(markets.contains(market)) {
			updateMarket(market);
			return;
		}
		
		markets.add(market);
		marketMap.put(market.instrument(), market);
		
		/* Attach agent and market to each other if passes agent's filter */
		for(final FrameworkAgent agent : agents) {
			if(agent.filter(market.instrument())) {
				agent.attach(market);
				market.attach(agent);
			}
		}
		
	}

	@Override
	public synchronized void updateMarket(final Market market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void detachMarket(final Market market) {
		// TODO Auto-generated method stub
		
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public <V extends MarketDataObject<V>> Builder<V> builder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V extends MarketDataObject<V>> void handle(MarketMessage<V> message) {
		
		
		
	}

}
