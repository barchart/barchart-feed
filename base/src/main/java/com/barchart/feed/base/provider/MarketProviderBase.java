package com.barchart.feed.base.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;

import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.connection.Connection.Monitor;
import com.barchart.feed.api.connection.TimestampListener;
import com.barchart.feed.api.consumer.ConsumerAgent;
import com.barchart.feed.api.consumer.MarketService;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.base.market.api.MarketDo;
import com.barchart.feed.base.market.api.MarketFactory;
import com.barchart.feed.base.market.api.MarketMakerProvider;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.market.api.MarketRegListener;
import com.barchart.feed.base.market.api.MarketSafeRunner;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.participant.FrameworkAgent;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.impl.ValueConst;
import com.barchart.util.values.api.Value;

public abstract class MarketProviderBase<Message extends MarketMessage> 
		implements MarketService, MarketMakerProvider<Message> {
	
	protected static final Logger log = LoggerFactory.getLogger(
			MarketProviderBase.class);

	protected final MarketFactory factory;
	
	protected final ConcurrentMap<InstrumentID, MarketDo> marketMap = 
			new ConcurrentHashMap<InstrumentID, MarketDo>();
	
	protected final ConcurrentMap<String, InstrumentID> symbolMap = 
			new ConcurrentHashMap<String, InstrumentID>();

	private final ConcurrentMap<FrameworkAgent<?>, Boolean> agents = 
			new ConcurrentHashMap<FrameworkAgent<?>, Boolean>();
	
	protected MarketProviderBase(final MarketFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public <V extends MarketData<V>> ConsumerAgent register(
			MarketObserver<V> callback, Class<V> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<Market> snapshot(InstrumentID instrument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startup() {
		
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void bindConnectionStateListener(Monitor listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bindTimestampListener(TimestampListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Observable<Result<Instrument>> instrument(String... symbols) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<Result<Instrument>> instrument(SearchContext ctx,
			String... symbols) {
		// TODO Auto-generated method stub
		return null;
	}

	// ######################## // ########################
	
	@Override
	public boolean register(Instrument instrument) {
		
		if (!isValid(instrument)) {
			return false;
		}

		MarketDo market = marketMap.get(instrument.id());

		final boolean wasAdded = (market == null);

		while (market == null) {
			market = factory.newMarket(instrument);
			market.setInstrument(instrument);
			marketMap.putIfAbsent(instrument.id(), market);
			market = marketMap.get(instrument.id());
		}

		if (wasAdded) {

			for (final FrameworkAgent<?> agent : agents.keySet()) {
				marketMap.get(instrument.id()).attachAgent(agent);
			}

			symbolMap.put(instrument.symbol(), instrument.id());
			
		} else {
			log.warn("already registered : {}", instrument.id());
		}

		return wasAdded;
	}

	@Override
	public boolean unregister(Instrument instrument) {
		
		if (!isValid(instrument)) {
			return false;
		}

		final MarketDo market = marketMap.remove(instrument.id());

		final boolean wasRemoved = (market != null);

		if (wasRemoved) {

			for (final FrameworkAgent<?> agent : agents.keySet()) {
				marketMap.get(instrument.id()).detachAgent(agent);
			}

			symbolMap.remove(instrument.symbol());
			
		} else {
			log.warn("was not registered : {}", instrument);
		}

		return wasRemoved;
	}

	// ######################## // ########################

	@Override
	public int marketCount() {
		return marketMap.size();
	}

	@Override
	public boolean isRegistered(Instrument instrument) {
		return marketMap.containsKey(instrument.id());
	}
	
	@Override
	public void clearAll() {
		marketMap.clear();
		symbolMap.clear();
	}
	
	@Override
	public void make(final Message message) {

		final Instrument instrument = message.getInstrument();

		if (!isValid(instrument)) {
			return;
		}

		final MarketDo market = marketMap.get(instrument);

		if (!isValid(market)) {
			return;
		}

		market.runSafe(safeMake, message);

	}

	protected MarketSafeRunner<Void, Message> safeMake = new MarketSafeRunner<Void, Message>() {
		@Override
		public Void runSafe(final MarketDo market, final Message message) {
			make(message, market);
			market.fireEvents();
			return null;
		}
	};
	
	protected abstract void make(Message message, MarketDo market);
	
	@Override
	public <S extends Instrument, V extends Value<V>> V take(S instrument,
			MarketField<V> field) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void appendMarketProvider(final MarketFactory marketFactory) {
		throw new UnsupportedOperationException("TODO");
	}

	// ######################## // ########################
	
	protected boolean isValid(final MarketDo market) {

		if (market == null) {
			log.debug("market == null");
			return false;
		}

		return true;

	}

	
	protected boolean isValid(final Instrument instrument) {

		if (instrument == null) {
			log.error("instrument == null");
			return false;
		}

		if (instrument.isNull()) {
			log.error("instrument.isNull()");
			return false;
		}

		final Price priceStep = instrument.tickSize();

		if (priceStep.isZero()) {
			log.error("priceStep.isZero()");
			return false;
		}

		final Fraction fraction = instrument.displayFraction();
		
		if(fraction == null || fraction == ValueConst.NULL_FRACTION) {
			log.error("fraction.isNull()");
			return false;
		}

		return true;

	}
	
	/* ***** ***** Unsupported ***** ***** */

	@Override
	public synchronized final void copyTo(
			final MarketMakerProvider<Message> maker,
			final MarketField<?>... fields) {
		throw new UnsupportedOperationException("TODO");
	}
	
	@Override
	public <V extends Value<V>> boolean register(MarketTaker<V> taker) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <V extends Value<V>> boolean unregister(MarketTaker<V> taker) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <V extends Value<V>> boolean update(MarketTaker<V> taker) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRegistered(MarketTaker<?> taker) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(MarketRegListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(MarketRegListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void notifyRegListeners() {
		throw new UnsupportedOperationException();
	}
	
}
