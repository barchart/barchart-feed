/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.provider;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.instrument.enums.InstrumentField;
import com.barchart.feed.base.instrument.enums.MarketDisplay.Fraction;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.MarketDo;
import com.barchart.feed.base.market.api.MarketFactory;
import com.barchart.feed.base.market.api.MarketMakerProvider;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.market.api.MarketRegListener;
import com.barchart.feed.base.market.api.MarketSafeRunner;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.anno.ThreadSafe;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.Value;

@ThreadSafe
public abstract class MakerBase<Message extends MarketMessage> implements
		MarketMakerProvider<Message> {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected final MarketFactory factory;

	private final ConcurrentMap<MarketInstrument, MarketDo> marketMap = //
	new ConcurrentHashMap<MarketInstrument, MarketDo>();

	private final ConcurrentMap<MarketTaker<?>, RegTaker<?>> takerMap = //
	new ConcurrentHashMap<MarketTaker<?>, RegTaker<?>>();

	private final CopyOnWriteArrayList<MarketRegListener> listenerList = //
	new CopyOnWriteArrayList<MarketRegListener>();

	// ########################

	@Override
	public int marketCount() {
		return marketMap.size();
	}

	@Override
	public boolean isRegistered(final MarketInstrument instrument) {
		return marketMap.containsKey(instrument);
	}

	@Override
	public boolean isRegistered(final MarketTaker<?> taker) {
		return takerMap.containsKey(taker);
	}

	protected MakerBase(final MarketFactory factory) {
		this.factory = factory;
	}

	// ########################

	@Override
	public synchronized final <V extends Value<V>> boolean register(
			final MarketTaker<V> taker) {

		if (!RegTaker.isValid(taker)) {
			return false;
		}

		RegTaker<?> regTaker = takerMap.get(taker);

		final boolean wasAdded = regTaker == null;

		while (regTaker == null) {
			regTaker = new RegTaker<V>(taker);
			takerMap.putIfAbsent(taker, regTaker);
			regTaker = takerMap.get(taker);
		}

		if (wasAdded) {
			for (final MarketInstrument instrument : regTaker.instruments()) {

				if (!isValid(instrument)) {
					continue;
				}

				if (!isRegistered(instrument)) {
					register(instrument);
				}

				final MarketDo market = marketMap.get(instrument);

				market.runSafe(safeRegister, regTaker);

				notifyRegListeners(market);

			}
		} else {
			log.warn("already registered : {}", taker);
		}

		return wasAdded;

	}

	@Override
	public synchronized final <V extends Value<V>> boolean update(
			final MarketTaker<V> taker) {

		if (!RegTaker.isValid(taker)) {
			return false;
		}

		final RegTaker<?> regTaker = takerMap.get(taker);

		if (regTaker == null) {
			log.warn("Taker not registered : {}", taker);
			return false;
		}

		/* Check each instrument in updated taker, make sure subscribed */
		for (final MarketInstrument takerInst : regTaker.instruments()) {

			if (!isValid(takerInst)) {
				continue;
			}

			if (!isRegistered(takerInst)) {
				register(takerInst);
			}

			final MarketDo market = marketMap.get(takerInst);

			// if (!market.hasRegTakers()) {
			// market.runSafe(safeRegister, regTaker);
			//
			// notifyRegListeners(market);
			//
			// } else if (!market.regList().contains(regTaker)) {

			market.runSafe(safeRegister, regTaker);

			notifyRegListeners(market);

			// }

		}

		/* Check for removal */
		for (final Entry<MarketInstrument, MarketDo> entry : marketMap
				.entrySet()) {

			final MarketInstrument inst = entry.getKey();
			final MarketDo market = entry.getValue();

			/* If the Market has the updated taker */
			if (market.hasRegTakers() && market.regList().contains(regTaker)) {

				/* Determine if the instrument is sill required by the taker */
				boolean mustRemove = true;

				for (final MarketInstrument takerInst : regTaker.instruments()) {
					if (inst.equals(takerInst)) {
						mustRemove = false;
					}
				}

				/* If not, then remove */
				if (mustRemove) {
					market.regRemove(regTaker);
				}

			}

		}

		return true;
	}

	private final MarketSafeRunner<Void, RegTaker<?>> safeRegister = //
	new MarketSafeRunner<Void, RegTaker<?>>() {
		@Override
		public Void runSafe(final MarketDo market, final RegTaker<?> regTaker) {
			market.regAdd(regTaker);
			return null;
		}
	};

	@Override
	public synchronized final <V extends Value<V>> boolean unregister(
			final MarketTaker<V> taker) {

		if (!RegTaker.isValid(taker)) {
			return false;
		}

		final RegTaker<?> regTaker = takerMap.remove(taker);

		final boolean wasRemoved = regTaker != null;

		if (wasRemoved) {
			for (final MarketInstrument instrument : regTaker.instruments()) {

				if (!isValid(instrument)) {
					continue;
				}

				final MarketDo market = marketMap.get(instrument);

				market.runSafe(safeUnregister, regTaker);

				if (!market.hasRegTakers()) {
					unregister(instrument);
				}

				notifyRegListeners(market);

			}
		} else {
			log.warn("was not registered : {}", taker);
		}

		return wasRemoved;

	}

	private final MarketSafeRunner<Void, RegTaker<?>> safeUnregister = //
	new MarketSafeRunner<Void, RegTaker<?>>() {
		@Override
		public Void runSafe(final MarketDo market, final RegTaker<?> regTaker) {
			market.regRemove(regTaker);
			return null;
		}
	};

	// ########################

	@Override
	public final void make(final Message message) {

		final MarketInstrument instrument = message.getInstrument();

		if (!isValid(instrument)) {
			return;
		}

		final MarketDo market = marketMap.get(instrument);

		if (!isValid(market)) {
			return;
		}

		market.runSafe(safeMake, message);

	}

	private final MarketSafeRunner<Void, Message> safeMake = //
	new MarketSafeRunner<Void, Message>() {
		@Override
		public Void runSafe(final MarketDo market, final Message message) {
			make(message, market);
			market.fireEvents();
			return null;
		}
	};

	protected abstract void make(Message message, MarketDo market);

	// ########################

	@SuppressWarnings("unchecked")
	@Override
	public final <S extends MarketInstrument, V extends Value<V>> V take(
			final S instrument, final MarketField<V> field) {

		final MarketDo market = marketMap.get(instrument);

		if (market == null) {
			return MarketConst.NULL_MARKET.get(field).freeze();
		}

		return (V) market.runSafe(safeTake, field);

	}

	private final MarketSafeRunner<Value<?>, MarketField<?>> safeTake = //
	new MarketSafeRunner<Value<?>, MarketField<?>>() {
		@Override
		public Value<?> runSafe(final MarketDo market,
				final MarketField<?> field) {
			return market.get(field).freeze();
		}
	};

	// ########################

	@Override
	public synchronized final void copyTo(
			final MarketMakerProvider<Message> maker,
			final MarketField<?>... fields) {
		throw new UnsupportedOperationException("not implemented");
	}

	// ########################

	@Override
	public synchronized void clearAll() {
		marketMap.clear();
		takerMap.clear();
	}

	// ########################

	protected final boolean isValid(final MarketDo market) {

		if (market == null) {
			log.debug("market == null");
			return false;
		}

		return true;

	}

	protected final boolean isValid(final MarketInstrument instrument) {

		if (instrument == null) {
			log.error("instrument == null");
			return false;
		}

		if (instrument.isNull()) {
			log.error("instrument.isNull()");
			return false;
		}

		final PriceValue priceStep = instrument.get(InstrumentField.PRICE_STEP);

		if (priceStep.isZero()) {
			log.error("priceStep.isZero()");
			return false;
		}

		final Fraction fraction = instrument.get(InstrumentField.FRACTION);

		if (fraction.isNull()) {
			log.error("fraction.isNull()");
			return false;
		}

		if (priceStep.exponent() != fraction.decimalExponent) {
			log.error("priceStep.exponent() != fraction.decimalExponent");
			return false;
		}

		return true;

	}

	//

	protected final synchronized boolean register(
			final MarketInstrument instrument) {

		if (!isValid(instrument)) {
			return false;
		}

		MarketDo market = marketMap.get(instrument);

		final boolean wasAdded = market == null;

		while (market == null) {
			market = factory.newMarket();
			market.setInstrument(instrument);
			marketMap.putIfAbsent(instrument, market);
			market = marketMap.get(instrument);
		}

		if (wasAdded) {
			//
		} else {
			log.warn("already registered : {}", instrument);
		}

		return wasAdded;

	}

	protected final synchronized boolean unregister(
			final MarketInstrument instrument) {

		if (!isValid(instrument)) {
			return false;
		}

		final MarketDo market = marketMap.remove(instrument);

		final boolean wasRemoved = market != null;

		if (wasRemoved) {
			//
		} else {
			log.warn("was not registered : {}", instrument);
		}

		return wasRemoved;

	}

	//

	@Override
	public void add(final MarketRegListener listener) {
		listenerList.addIfAbsent(listener);
	}

	@Override
	public void remove(final MarketRegListener listener) {
		listenerList.remove(listener);
	}

	protected final void notifyRegListeners(final MarketDo market) {

		final MarketInstrument instrument = market.get(MarketField.INSTRUMENT);
		final Set<MarketEvent> events = market.regEvents();

		for (final MarketRegListener listener : listenerList) {
			try {
				listener.onRegistrationChange(instrument, events);
			} catch (final Exception e) {
				log.error("", e);
			}
		}

	}

	@Override
	public void notifyRegListeners() {
		for (final MarketDo market : marketMap.values()) {
			notifyRegListeners(market);
		}
	}

	@Override
	public void appendMarketProvider(final MarketFactory marketFactory) {

	}

}
