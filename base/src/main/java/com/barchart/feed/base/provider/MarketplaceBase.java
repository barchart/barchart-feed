package com.barchart.feed.base.provider;

import com.barchart.feed.api.Agent;
import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.Marketplace;
import com.barchart.feed.api.consumer.MetadataService;
import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.market.api.MarketFactory;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.participant.FrameworkAgent;
import com.barchart.feed.base.provider.MarketDataGetters.MDGetter;
import com.barchart.feed.base.sub.SubscriptionHandler;

public abstract class MarketplaceBase<Message extends MarketMessage> extends MarketProviderBase<Message> 
		 implements Marketplace {

	protected MarketplaceBase(
			final MarketFactory factory, 
			final MetadataService metaService,
			final SubscriptionHandler handler) {
		
		super(factory, metaService, handler);
	}
	
	@Override
	public <V extends MarketData<V>> Agent subscribe(final Class<V> clazz, 
			final MarketObserver<V> callback, final String... symbols) {
		
		final MDGetter<V> getter = MarketDataGetters.get(clazz);
		
		if (getter == null) {
			throw new IllegalArgumentException("Illegal class type " + clazz.getName());
		}
		
		final FrameworkAgent<V> agent = new BaseAgent<V>(this, clazz, getter, callback);
		
		attachAgent(agent);
		
		agent.agent().include(symbols);
		
		return agent.agent();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public <V extends MarketData<V>> Agent subscribe(final Class<V> clazz,
			final MarketObserver<V> callback, final Instrument... instruments) {
		
		final MDGetter<V> getter = MarketDataGetters.get(clazz);
		
		if (getter == null) {
			throw new IllegalArgumentException("Illegal class type " + clazz.getName());
		}
		
		final FrameworkAgent<V> agent = new BaseAgent<V>(this, clazz, getter, callback);
		
		attachAgent(agent);
		
		agent.agent().include(instruments);
		
		return agent.agent();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public <V extends MarketData<V>> Agent subscribe(final Class<V> clazz,
			final MarketObserver<V> callback, final Exchange... exchanges) {
		
		final MDGetter<V> getter = MarketDataGetters.get(clazz);
		
		if (getter == null) {
			throw new IllegalArgumentException("Illegal class type " + clazz.getName());
		}
		
		final FrameworkAgent<V> agent = new BaseAgent<V>(this, clazz, getter, callback);
		
		attachAgent(agent);
		
		agent.agent().include(exchanges);
		
		return agent.agent();
		
	}
	
	@Override	
	public Agent subscribeMarket(final MarketObserver<Market> callback, final String... symbols) {
		
		final MDGetter<Market> getter = MarketDataGetters.get(Market.class);
		
		final FrameworkAgent<Market> agent = new BaseAgent<Market>(this, Market.class, getter, callback);
		
		attachAgent(agent);
		
		agent.agent().include(symbols);
		
		return agent.agent();
		
	}

	@Override
	public Agent subscribeTrade(final MarketObserver<Trade> lastTrade, final String... symbols) {
		
		final MDGetter<Trade> getter = MarketDataGetters.get(Trade.class);
		
		final FrameworkAgent<Trade> agent = new BaseAgent<Trade>(this, Trade.class, getter, lastTrade);
		
		attachAgent(agent);
		
		agent.agent().include(symbols);
		
		return agent.agent();
		
	}

	@Override
	public Agent subscribeBook(final MarketObserver<Book> book, final String... symbols) {

		final MDGetter<Book> getter = MarketDataGetters.get(Book.class);
		
		final FrameworkAgent<Book> agent = new BaseAgent<Book>(this, Book.class, getter, book);
		
		attachAgent(agent);
		
		agent.agent().include(symbols);
		
		return agent.agent();
		
	}

	@Override
	public Agent subscribeCuvol(final MarketObserver<Cuvol> cuvol, final String... symbols) {

		final MDGetter<Cuvol> getter = MarketDataGetters.get(Cuvol.class);
		
		final FrameworkAgent<Cuvol> agent = new BaseAgent<Cuvol>(this, Cuvol.class, getter, cuvol);
		
		attachAgent(agent);
		
		agent.agent().include(symbols);
		
		return agent.agent();
		
	}
	
	@Override
	public <V extends MarketData<V>> Agent newAgent(final Class<V> dataType, final MarketObserver<V> callback) {
		
		final MDGetter<V> getter = MarketDataGetters.get(dataType);
		
		final FrameworkAgent<V> agent = new BaseAgent<V>(this, dataType, getter, callback);
		
		attachAgent(agent);
		
		return agent.agent();
	}
	
	@Override
	public Market snapshot(final Instrument instrument) {
		
		if(marketMap.containsKey(instrument.id())) {
			return marketMap.get(instrument.id()).freeze();
		}
		
		return Market.NULL;  // throw IAR or ISE?
	}
	
	@Override
	public Market snapshot(final String symbol) {
		
		final Instrument inst = instrument(symbol).toBlockingObservable().first().results().get(symbol).get(0);
		
		if(inst.isNull()) {
			return Market.NULL;
		}
		
		return snapshot(inst);
	}
	
}
