package com.barchart.feed.market.provider;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.barchart.feed.api.data.FrameworkElement;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.data.framework.Market;
import com.barchart.feed.api.market.FrameworkAgent;
import com.barchart.feed.api.market.Marketplace;
import com.barchart.feed.api.message.Message;

public class MarketplaceBase implements Marketplace {

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
		
		/* Agent already attached just update */
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
				/* Agent will attach itself to market */
				agent.attach(e.getValue());
			}
			
		}
		
	}

	@Override
	public synchronized void updateAgent(final FrameworkAgent agent) {
		
		if(agent == null) {
			return;
		}
		
		if(!agents.contains(agent)) {
			attachAgent(agent);
			return;
		}
		
		for(final Entry<Instrument, Market> e : marketMap.entrySet()) {
			
			if(agent.filter(e.getKey())) {
				agent.attach(e.getValue());
			} else {
				agent.detach(e.getValue());
			}
			
		}
		
	}

	@Override
	public synchronized void detachAgent(final FrameworkAgent agent) {
		
		if(agent == null || !agents.contains(agent)) {
			return;
		}
		
		for(final Market market : markets) {
			agent.detach(market);
		}
		
		agents.remove(agent);
		
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	// static Market buildMarket(final Instrument inst);
	
	
	
	protected synchronized void attachMarket(final Market market) {
		
		if(market == null || markets.contains(market)) {
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

	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public void handle(final Message message) {
		
		
		
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public <V extends FrameworkElement<V>> Builder<V> builder() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
