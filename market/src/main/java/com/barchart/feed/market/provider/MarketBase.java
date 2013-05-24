package com.barchart.feed.market.provider;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.barchart.feed.api.consumer.data.Cuvol;
import com.barchart.feed.api.consumer.data.Instrument;
import com.barchart.feed.api.consumer.data.Market;
import com.barchart.feed.api.consumer.data.OrderBook;
import com.barchart.feed.api.consumer.data.PriceLevel;
import com.barchart.feed.api.consumer.data.Session;
import com.barchart.feed.api.consumer.data.TopOfBook;
import com.barchart.feed.api.consumer.data.Trade;
import com.barchart.feed.api.consumer.enums.SessionType;
import com.barchart.feed.api.framework.FrameworkAgent;
import com.barchart.feed.api.framework.MarketEntity;
import com.barchart.feed.api.framework.MarketTag;
import com.barchart.feed.api.framework.data.InstrumentEntity;
import com.barchart.feed.api.framework.message.Message;
import com.barchart.feed.api.framework.message.Snapshot;
import com.barchart.feed.api.framework.message.Update;
import com.barchart.missive.api.Tag;
import com.barchart.missive.core.ObjectMapSafe;
import com.barchart.util.value.api.Time;

public class MarketBase extends ObjectMapSafe implements MarketEntity {
	
	// MAKE CANONICAL NULL OBJECTS
	private volatile Update lastUpdate = null;
	private volatile Snapshot lastSnapshot = null;
	
	private final ConcurrentMap<Tag<?>, Set<FrameworkAgent>> agentMap = 
			new ConcurrentHashMap<Tag<?>, Set<FrameworkAgent>>();
	
	private final Set<FrameworkAgent> agents = Collections.newSetFromMap(
			new ConcurrentHashMap<FrameworkAgent, Boolean>());
	
	/*
	 * Default constructor only for ObjectMaps
	 */
	@Override
	public void init() {
		
		// INIT SHIT SON
		
	}
	
	/* Instrument set in MarketFactory */
	private volatile InstrumentEntity instrument;
	
	void setInstrument(final InstrumentEntity inst) {
		instrument = inst;
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	/*
	 * Attach and detach here just clear references to the agent.
	 * 
	 */
	@Override
	public synchronized void attach(final FrameworkAgent agent) {
		
		if(agent == null) {
			return;
		}
		
		if(agents.contains(agent)) {
			update(agent);
			return;
		}
		
		agents.add(agent);
		
	}
	
	@Override
	public synchronized void update(final FrameworkAgent agent) {
		
//		if(agent == null) {
//			return;
//		}
//		
//		if(!agents.contains(agent)) {
//			attach(agent);
//			return;
//		}
//		
//		for(final Tag<?> t : agentMap.keySet()) {
//			
//			/* If tag's set has the agent, but the agent is no longer listening to the tag */
//			if(agentMap.get(t).contains(agent) && !contains(t, agent.tagsToListenTo())) {
//				agentMap.get(t).remove(agent);
//			}
//			
//			/* If tag's set is missing the agent and the agent is now listening to the tag */
//			if(!agentMap.get(t).contains(agent) && contains(t, agent.tagsToListenTo())) {
//				agentMap.get(t).add(agent);
//			}
//		}
//		
	}
	
	private <V> boolean contains(V v, V[] array) {
		for(final V vv : array) {
			if(vv.equals(v)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public synchronized void detach(final FrameworkAgent agent) {
		
		if(agent == null) {
			return;
		}
		
		/* Remove agent from all data structures */
		for(final Tag<?> t : agentMap.keySet()) {
			agentMap.get(t).remove(agent);
		}
		
		agents.remove(agent);
		
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public void update(final Message message) {
		
		
	}

	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public Update lastUpdate() {
		return lastUpdate;
	}

	@Override
	public Snapshot lastSnapshot() {
		return lastSnapshot;
	}

	@Override
	public MarketTag<MarketEntity> tag() {
		return MarketEntity.MARKET;
	}
	
	/* ***** ***** Market Data Object Getters ***** ***** */
	
	@Override
	public Instrument instrument() {
		return instrument;
	}

	@Override
	public Trade lastTrade() {
		return get(MarketEntity.LAST_TRADE);
	}

	@Override
	public OrderBook orderBook() {
		return get(MarketEntity.ORDER_BOOK);
	}

	@Override
	public PriceLevel lastBookUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopOfBook topOfBook() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cuvol cuvol() {
		// TODO Auto-generated method stub
		return null;
	}

	/* ***** ***** ***** ***** ***** ***** ***** */

	@Override
	public int compareTo(final MarketEntity o) {
		return instrument.compareTo(o.instrumentEntity());
	}
	
	@Override
	public boolean equals(final Object o) {
		
		if(o == null) {
			return false;
		}
		
		if(!Market.class.isAssignableFrom(o.getClass())) {
			return false;
		}
		
		return instrument.equals(((Market)o).instrument());
		
	}
	
	@Override
	public int hashCode() {
		return instrument.hashCode();
	}

	@Override
	public Time lastTime() {
		return null;
	}

	@Override
	public Session session(SessionType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time lastUpdateTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketEntity copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstrumentEntity instrumentEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNull() {
		// TODO Auto-generated method stub
		return false;
	}

}
