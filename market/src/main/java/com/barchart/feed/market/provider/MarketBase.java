package com.barchart.feed.market.provider;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.joda.time.DateTime;

import com.barchart.feed.api.data.FrameworkElement;
import com.barchart.feed.api.data.MarketTag;
import com.barchart.feed.api.data.client.CurrentSessionObject;
import com.barchart.feed.api.data.client.CuvolObject;
import com.barchart.feed.api.data.client.ExtendedSessionObject;
import com.barchart.feed.api.data.client.OrderBookObject;
import com.barchart.feed.api.data.client.PreviousSessionObject;
import com.barchart.feed.api.data.client.PriceLevelObject;
import com.barchart.feed.api.data.client.TopOfBookObject;
import com.barchart.feed.api.data.client.TradeObject;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.data.framework.Market;
import com.barchart.feed.api.fields.MarketField;
import com.barchart.feed.api.market.FrameworkAgent;
import com.barchart.feed.api.message.Message;
import com.barchart.feed.api.message.Snapshot;
import com.barchart.feed.api.message.Update;
import com.barchart.missive.api.Tag;
import com.barchart.missive.core.ObjectMapSafe;

class MarketBase extends ObjectMapSafe implements Market {
	
	// MAKE CANONICAL NULL OBJECTS
	private volatile Update<Market> lastUpdate = null;
	private volatile Snapshot<Market> lastSnapshot = null;
	
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
	private volatile Instrument instrument;
	
	void setInstrument(final Instrument inst) {
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
		
		for(final Tag<?> tag : agent.tagsToListenTo()) {
			agentMap.get(tag).add(agent);
		}
		
	}
	
	@Override
	public synchronized void update(final FrameworkAgent agent) {
		
		if(agent == null) {
			return;
		}
		
		if(!agents.contains(agent)) {
			attach(agent);
			return;
		}
		
		for(final Tag<?> t : agentMap.keySet()) {
			
			/* If tag's set has the agent, but the agent is no longer listening to the tag */
			if(agentMap.get(t).contains(agent) && !contains(t, agent.tagsToListenTo())) {
				agentMap.get(t).remove(agent);
			}
			
			/* If tag's set is missing the agent and the agent is now listening to the tag */
			if(!agentMap.get(t).contains(agent) && contains(t, agent.tagsToListenTo())) {
				agentMap.get(t).add(agent);
			}
		}
		
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <V extends FrameworkElement<V>> void handle(
			final Message<V> message) {
		
		// Check instrument?
		
		if(Snapshot.class.isAssignableFrom(message.getClass())) {
			get(message.tag()).snapshot((Snapshot)message);
		} else if(Update.class.isAssignableFrom(message.getClass())) {
			get(message.tag()).update((Update)message);
		} else {
			// Unknown
		}
		
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public void update(final Update<Market> update) {
		
		lastUpdate = update;
		
		
		
	}

	@Override
	public void snapshot(final Snapshot<Market> snapshot) {

		lastSnapshot = snapshot;
		
		
		
	}

	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public Update<Market> lastUpdate() {
		return lastUpdate;
	}

	@Override
	public Snapshot<Market> lastSnapshot() {
		return lastSnapshot;
	}

	@Override
	public MarketTag<Market> tag() {
		return MarketField.MARKET;
	}
	
	/* ***** ***** Market Data Object Getters ***** ***** */
	
	@Override
	public Instrument instrument() {
		return instrument;
	}

	@Override
	public DateTime lastChangeTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TradeObject lastTrade() {
		return get(MarketField.LAST_TRADE);
	}

	@Override
	public OrderBookObject orderBook() {
		return get(MarketField.ORDER_BOOK);
	}

	@Override
	public PriceLevelObject lastBookUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopOfBookObject topOfBook() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CuvolObject cuvol() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CurrentSessionObject currentSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreviousSessionObject extraSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExtendedSessionObject previousSession() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */

	@Override
	public int compareTo(final Market o) {
		return instrument.compareTo(o.instrument());
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

}
