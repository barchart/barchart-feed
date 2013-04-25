package com.barchart.feed.base.provider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.barchart.feed.api.data.client.MarketDataObject;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.exchange.Exchange;
import com.barchart.feed.api.inst.InstrumentService;
import com.barchart.feed.api.market.MarketAgent;
import com.barchart.feed.api.market.MarketCallback;
import com.barchart.feed.api.market.Marketplace;
import com.barchart.feed.api.util.Filter;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.values.api.PriceValue;

public abstract class MarketplaceImpl<Message extends MarketMessage> 
		implements Marketplace {
	
	private final MakerBase<Message> maker;
	final InstrumentService<String> instService;
	
	final ConcurrentMap<MarketAgent, MarketTaker<?>> agentMap =
			new ConcurrentHashMap<MarketAgent, MarketTaker<?>>();
	
	protected MarketplaceImpl(MakerBase<Message> maker, 
			InstrumentService<String> instService) {
		this.maker = maker;
		this.instService = instService;
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
			maker.register(taker);
		}

		@Override
		public void update() {
			maker.update(taker);
		}
		
		@Override
		public void deactivate() {
			maker.unregister(taker);
		}

		@Override
		public void dismiss() {
			maker.unregister(taker);
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
	
	private static Map<Class<com.barchart.feed.api.market.MarketMessage<?>>, MarketEvent> eventMap = 
			new HashMap<Class<com.barchart.feed.api.market.MarketMessage<?>>, MarketEvent>();
	
	
	
	private class MBuilder<V extends MarketDataObject<V>> implements Builder<V> {
		
		private volatile Map<Instrument, Void> insts = new HashMap<Instrument, Void>();
		private volatile Map<Exchange, Void> exchs = new HashMap<Exchange, Void>();
		private volatile Map<MarketEvent, Void> events = new HashMap<MarketEvent, Void>();
		private volatile Map<Filter<?>, Void> filMap = new HashMap<Filter<?>, Void>();
		
		@Override
		public Builder<V> filter(String... symbols) {
			
			if(symbols == null) {
				return this;
			}
			
			for(final Instrument i : instService.lookup(Arrays.asList(symbols)).values()) {
				if(i != null) {
					insts.put(i, null);
				}
			}
			
			return this;
		}

		@Override
		public Builder<V> filter(Instrument... instruments) {
			
			if(instruments == null) {
				return this;
			}
			
			for(final Instrument i : instruments) {
				if(i != null) {
					insts.put(i, null);
				}
			}
			return this;
		}

		@Override
		public Builder<V> filter(Exchange... exchanges) {
			
			if(exchanges == null) {
				return this;
			}
			
			for(final Exchange e : exchanges) {
				if(e != null) {
					exchs.put(e, null);
				}
			}
			
			return this;
		}

		@Override
		public Builder<V> filter(
				Class<com.barchart.feed.api.market.MarketMessage<?>>... messageTypes) {
			
			if(messageTypes == null) {
				return this;
			}
			
			for(Class<com.barchart.feed.api.market.MarketMessage<?>> m : messageTypes) {
				if(m != null) {
					events.put(eventMap.get(m), null);
				}
			}
			return this;
		}

		@Override
		public Builder<V> filter(Filter<?>... filters) {
			
			if(filters == null) {
				return this;
			}
			
			for(final Filter<?> f : filters) {
				filMap.put(f, null);
			}
			
			return this;
		}

		@Override
		public MarketAgent build(MarketCallback<V> callback) {
			
			
			
			return null;
		}
		
	}

}
