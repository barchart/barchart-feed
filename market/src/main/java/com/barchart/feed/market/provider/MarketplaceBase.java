package com.barchart.feed.market.provider;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.barchart.feed.api.core.Marketplace;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.InstrumentEntity;
import com.barchart.feed.api.data.Market;
import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.framework.FrameworkAgent;
import com.barchart.feed.api.framework.FrameworkEntity;
import com.barchart.feed.api.framework.FrameworkMarketplace;
import com.barchart.feed.api.framework.MarketEntity;
import com.barchart.feed.api.message.Message;

public class MarketplaceBase implements FrameworkMarketplace {

	private final Set<FrameworkAgent> agents = Collections.newSetFromMap(
			new ConcurrentHashMap<FrameworkAgent, Boolean>());
	
	private final Set<MarketEntity> markets = Collections.newSetFromMap( 
			new ConcurrentHashMap<MarketEntity, Boolean>());
	
	private final ConcurrentMap<InstrumentEntity, MarketEntity> marketMap = 
			new ConcurrentHashMap<InstrumentEntity, MarketEntity>();
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@SuppressWarnings("unchecked")
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
		for(final Entry<InstrumentEntity, MarketEntity> e : marketMap.entrySet()) {
			
			if(agent.include(e.getKey())) {
				/* Agent will attach itself to market */
				agent.attach(e.getValue());
			}
			
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void updateAgent(final FrameworkAgent agent) {
		
		if(agent == null) {
			return;
		}
		
		if(!agents.contains(agent)) {
			attachAgent(agent);
			return;
		}
		
		for(final Entry<InstrumentEntity, MarketEntity> e : marketMap.entrySet()) {
			
			if(agent.include(e.getKey())) {
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
		
		for(final MarketEntity market : markets) {
			market.detach(agent);
		}
		
		agents.remove(agent);
		
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	// static Market buildMarket(final Instrument inst);
	
	
	
	@SuppressWarnings("unchecked")
	protected synchronized void attachMarket(final MarketEntity market) {
		
		if(market == null || markets.contains(market)) {
			return;
		}
		
		markets.add(market);
		marketMap.put(market.instrument(), market);
		
		/* Attach agent and market to each other if passes agent's filter */
		for(final FrameworkAgent agent : agents) {
			if(agent.include(market.instrument())) {
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
	public <V extends FrameworkEntity<V>> Builder<V> agentBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V extends MarketData> Builder<V> agentBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
