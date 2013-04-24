package com.barchart.feed.market.provider;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.joda.time.DateTime;

import com.barchart.feed.api.fields.MarketField;
import com.barchart.feed.api.inst.Instrument;
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
import com.barchart.feed.market.api.FrameworkAgent;
import com.barchart.feed.market.api.Market;
import com.barchart.missive.api.Tag;

public class MarketBase implements Market {
	
	// MAKE CANONICAL NULL OBJECTS
	private volatile Update<MarketObject> lastUpdate = null;
	private volatile Snapshot<MarketObject> lastSnapshot = null;
	
	// Fake tagMap
	private final ConcurrentMap<Tag<?>, MarketDataObject<?>> tagMap = 
			new ConcurrentHashMap<Tag<?>, MarketDataObject<?>>();
	
	private final ConcurrentMap<Tag<?>, List<FrameworkAgent>> agentMap = 
			new ConcurrentHashMap<Tag<?>, List<FrameworkAgent>>();
	
	/* ***** ***** ***** ***** ***** ***** ***** */
	
	@Override
	public void attach(final FrameworkAgent agent) {
		
		if(agent == null) {
			return;
		}
		
		for(final Tag<?> tag : agent.tagsToListenTo()) {
			
			//if agentMap contains tag
			agentMap.get(tag).add(agent);
			
		}
		
	}
	
	@Override
	public void detach(final FrameworkAgent agent) {
		
		
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <V extends MarketDataObject<V>> void handle(
			final MarketMessage<V> message) {
		
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
		// TODO Auto-generated method stub
		return null;
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
