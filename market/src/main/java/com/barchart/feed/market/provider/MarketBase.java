package com.barchart.feed.market.provider;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.joda.time.DateTime;

import com.barchart.feed.api.fields.MarketField;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.market.FrameworkAgent;
import com.barchart.feed.api.market.Market;
import com.barchart.feed.api.market.MarketMessage;
import com.barchart.feed.api.market.Snapshot;
import com.barchart.feed.api.market.Update;
import com.barchart.feed.api.market.data.CurrentSessionObject;
import com.barchart.feed.api.market.data.CuvolObject;
import com.barchart.feed.api.market.data.ExtendedSessionObject;
import com.barchart.feed.api.market.data.MarketDataObject;
import com.barchart.feed.api.market.data.MarketObject;
import com.barchart.feed.api.market.data.OrderBookObject;
import com.barchart.feed.api.market.data.PreviousSessionObject;
import com.barchart.feed.api.market.data.PriceLevelObject;
import com.barchart.feed.api.market.data.TopOfBookObject;
import com.barchart.feed.api.market.data.TradeObject;
import com.barchart.missive.api.Tag;

public class MarketBase implements Market {
	
	// MAKE CANONICAL NULL OBJECTS
	private volatile Update<MarketObject> lastUpdate = null;
	private volatile Snapshot<MarketObject> lastSnapshot = null;
	
	// Fake tagMap
	private final ConcurrentMap<Tag<?>, MarketDataObject<?>> tagMap = 
			new ConcurrentHashMap<Tag<?>, MarketDataObject<?>>();
	
	private final ConcurrentMap<Tag<?>, Set<FrameworkAgent>> agentMap = 
			new ConcurrentHashMap<Tag<?>, Set<FrameworkAgent>>();
	
	private final Set<FrameworkAgent> agents = Collections.newSetFromMap(
			new ConcurrentHashMap<FrameworkAgent, Boolean>());
	
	private final Instrument instrument;
	
	public MarketBase(final Instrument instrument) {
		this.instrument = instrument;
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
	public <V extends MarketDataObject<V>> void handle(
			final MarketMessage<V> message) {
		
		// Check instrument?
		
		if(Snapshot.class.isAssignableFrom(message.getClass())) {
			tagMap.get(message.tag()).snapshot((Snapshot)message);
		} else if(Update.class.isAssignableFrom(message.getClass())) {
			tagMap.get(message.tag()).update((Update)message);
		} else {
			// Unknown
		}
		
	}
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public void update(final Update<MarketObject> update) {
		
		lastUpdate = update;
		
	}

	@Override
	public void snapshot(final Snapshot<MarketObject> snapshot) {

		lastSnapshot = snapshot;
		
	}

	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public Update<MarketObject> lastUpdate() {
		return lastUpdate;
	}

	@Override
	public Snapshot<MarketObject> lastSnapshot() {
		return lastSnapshot;
	}

	@Override
	public Tag<MarketObject> tag() {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderBookObject orderBook() {
		// TODO Auto-generated method stub
		return null;
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

}
