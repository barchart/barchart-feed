package com.barchart.feed.base.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.barchart.feed.api.data.MarketDataObject;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.market.Market;
import com.barchart.feed.api.market.MarketAgent;
import com.barchart.feed.api.market.MarketCallback;
import com.barchart.feed.api.market.Marketplace;
import com.barchart.feed.base.market.api.MarketFactory;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.values.api.PriceValue;

public abstract class MarketplaceImpl<Message extends MarketMessage> 
		extends MakerBase<Message> implements Marketplace {
	
	final ConcurrentMap<MarketAgent, MarketTaker<?>> agentMap =
			new ConcurrentHashMap<MarketAgent, MarketTaker<?>>();
	
	protected MarketplaceImpl(MarketFactory factory) {
		super(factory);
	}
	
	/* ***** ***** ***** ***** ***** */
	
	@Override
	public void attachAgent(final MarketAgent agent) {
		
		if(agentMap.containsKey(agent)) {
			return;
		}
		
		final TakerAgent takerAgent = new TakerAgent(agent);
		
		agentMap.put(agent, takerAgent.taker());
		register(takerAgent.taker());
		
	}
	
	@Override
	public void updateAgent(final MarketAgent agent) {
		
	}

	@Override
	public void detachAgent(final MarketAgent agent) {
		
	}
	
	/* ***** ***** ***** ***** ***** */
	
	private class TakerAgent implements MarketAgent {
		
		private final MarketAgent agent;
		private final MarketTaker<?> taker;
		
		TakerAgent(final MarketAgent agent) {
			this.agent = agent;
			this.taker = makeTaker(agent);
		}
		
		public MarketTaker<?> taker() {
			return taker;
		}

		@Override
		public void activate() {
			register(taker);
		}

		@Override
		public void update() {
			MarketplaceImpl.super.update(taker);
		}
		
		@Override
		public void deactivate() {
			unregister(taker);
		}

		@Override
		public void dismiss() {
			unregister(taker);
		}

		// These methods will be moved out, IGNORE
		
		@Override
		public <V extends MarketDataObject<V>> MarketCallback<V> callback() {
			return null;
		}
		@Override
		public void attach(Market market) {
			// Do nothing
		}
		@Override
		public void update(Market market) {
			// Do nothing
		}
		@Override
		public void detach(Market market) {
			// Do nothing
		}
		
	}

	static MarketTaker<?> makeTaker(final MarketAgent agent) {
		
		
		return new MarketTaker<PriceValue>() {

			@Override
			public MarketField<PriceValue> bindField() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MarketEvent[] bindEvents() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Instrument[] bindInstruments() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onMarketEvent(MarketEvent event, Instrument instrument,
					PriceValue value) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
	

}
