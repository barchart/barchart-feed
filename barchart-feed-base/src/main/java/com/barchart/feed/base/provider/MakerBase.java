/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

/** TODO review and remove synchronized */
@ThreadSafe
public abstract class MakerBase<Message extends MarketMessage> implements
		MarketMakerProvider<Message> {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected final MarketFactory factory;

	final ConcurrentMap<MarketInstrument, MarketDo> marketMap = //
	new ConcurrentHashMap<MarketInstrument, MarketDo>();

	final ConcurrentMap<MarketTaker<?>, RegTaker<?>> takerMap = //
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

	
	// register super taker
	// make and store regTaker
	// add taker to takerMap
	// for each market in marketmap safeRegister taker 
	// no registration listeners
	
	// ########################

	@Override
	public synchronized final <V extends Value<V>> boolean register(
			final MarketTaker<V> taker) {

		if (!RegTaker.isValid(taker)) {
			return false;
		}

		RegTaker<?> regTaker = takerMap.get(taker);

		final boolean wasAdded = (regTaker == null);

		while (regTaker == null) {
			regTaker = new RegTaker<V>(taker);
			takerMap.putIfAbsent(taker, regTaker);
			regTaker = takerMap.get(taker);
		}

		if (wasAdded) {
			final Set<MarketDo> ms = new HashSet<MarketDo>();
			for (final MarketInstrument inst : regTaker.getInstruments()) {

				if (!isValid(inst)) {
					continue;
				}

				if (!isRegistered(inst)) {
					register(inst);
				}

				final MarketDo market = marketMap.get(inst);

				market.runSafe(safeRegister, regTaker);

				ms.add(market);

			}
			
			notifyRegListeners(ms);
		} else {
			log.warn("already registered : {}", taker);
		}

		return wasAdded;

	}

	private final MarketSafeRunner<Void, RegTaker<?>> safeUpdate = //
	new MarketSafeRunner<Void, RegTaker<?>>() {
		@Override
		public Void runSafe(final MarketDo market, final RegTaker<?> regTaker) {
			market.regUpdate(regTaker);
			return null;
		}
	};

	/** TODO optimize for speed */
	@Override
	public synchronized final <V extends Value<V>> boolean update(
			final MarketTaker<V> taker) {

		if (!RegTaker.isValid(taker)) {
			// debug logged already
			return false;
		}

		final RegTaker<?> regTaker = takerMap.get(taker);

		if (regTaker == null) {
			log.warn("Taker not registered : {}", taker);
			return false;
		}

		//

		final Set<MarketInstrument> updateSet = new HashSet<MarketInstrument>();
		final Set<MarketInstrument> registerSet = new HashSet<MarketInstrument>();
		final Set<MarketInstrument> unregisterSet = new HashSet<MarketInstrument>();
		final Set<MarketInstrument> changeNotifySet = new HashSet<MarketInstrument>();

		{

			final MarketInstrument[] pastArray = regTaker.getInstruments();
			final MarketInstrument[] nextArray = taker.bindInstruments();

			final Set<MarketInstrument> pastSet = new HashSet<MarketInstrument>(
					Arrays.asList(pastArray));
			final Set<MarketInstrument> nextSet = new HashSet<MarketInstrument>(
					Arrays.asList(nextArray));

			/** past & next */
			updateSet.addAll(pastSet);
			updateSet.retainAll(nextSet);

			/** next - past */
			registerSet.addAll(nextSet);
			registerSet.removeAll(updateSet);

			/** past - next */
			unregisterSet.addAll(pastSet);
			unregisterSet.removeAll(updateSet);

			/** past + next */
			changeNotifySet.addAll(updateSet);
			changeNotifySet.addAll(registerSet);
			changeNotifySet.addAll(unregisterSet);

		}

		//

		// log.debug("updateSet : {}", updateSet);
		// log.debug("registerSet : {}", registerSet);
		// log.debug("unregisterSet : {}", unregisterSet);
		// log.debug("changeNotifySet : {}", changeNotifySet);

		//

		/** unregister : based on past */
		for (final MarketInstrument inst : unregisterSet) {

			final MarketDo market = marketMap.get(inst);

			market.runSafe(safeUnregister, regTaker);

		}

		/** update : based on merge of next and past */
		for (final MarketInstrument inst : updateSet) {

			final MarketDo market = marketMap.get(inst);

			market.runSafe(safeUpdate, regTaker);

		}

		/** past = next */
		regTaker.bind();

		/** register : based on next */
		for (final MarketInstrument inst : registerSet) {

			if (!isValid(inst)) {
				continue;
			}

			if (!isRegistered(inst)) {
				register(inst);
			}

			final MarketDo market = marketMap.get(inst);

			market.runSafe(safeRegister, regTaker);

		}

		final Set<MarketDo> ms = new HashSet<MarketDo>();
		/** remove / notify */
		for (final MarketInstrument inst : changeNotifySet) {

			final MarketDo market = marketMap.get(inst);

			if (!market.hasRegTakers()) {
				unregister(inst);
			}
			
			ms.add(market);

		}
		
		notifyRegListeners(ms);

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

		final boolean wasRemoved = (regTaker != null);

		if (wasRemoved) {
			
			final Set<MarketDo> ms = new HashSet<MarketDo>();
			for (final MarketInstrument inst : regTaker.getInstruments()) {

				if (!isValid(inst)) {
					continue;
				}

				final MarketDo market = marketMap.get(inst);

				if(market==null){
					log.error("Failed to get MarketDo for " + inst.get(
							InstrumentField.ID).toString());
					continue;
				}
				
				market.runSafe(safeUnregister, regTaker);

				if (!market.hasRegTakers()) {
					unregister(inst);
				}

				ms.add(market);

			}
			
			notifyRegListeners(ms);
			
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
	public void make(final Message message) {

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

	protected MarketSafeRunner<Void, Message> safeMake = //
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
		throw new UnsupportedOperationException("TODO");
	}

	// ########################

	@Override
	public synchronized void clearAll() {
		marketMap.clear();
		takerMap.clear();
	}

	// ########################

	protected boolean isValid(final MarketDo market) {

		if (market == null) {
			log.debug("market == null");
			return false;
		}

		return true;

	}

	protected boolean isValid(final MarketInstrument instrument) {

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

	@Override
	public final synchronized boolean register(final MarketInstrument instrument) {

		if (!isValid(instrument)) {
			return false;
		}

		MarketDo market = marketMap.get(instrument);

		final boolean wasAdded = (market == null);

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

	@Override
	public final synchronized boolean unregister(
			final MarketInstrument instrument) {

		if (!isValid(instrument)) {
			return false;
		}

		final MarketDo market = marketMap.remove(instrument);

		final boolean wasRemoved = (market != null);

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

	protected final void notifyRegListeners(final Set<MarketDo> markets) {

		final Map<MarketInstrument, Set<MarketEvent>> insts = 
				new HashMap<MarketInstrument, Set<MarketEvent>>();
		
		for(final MarketDo m : markets) {
			insts.put(m.get(MarketField.INSTRUMENT), m.regEvents());
		}
		
		for (final MarketRegListener listener : listenerList) {
			try {
				listener.onRegistrationChange(insts);
			} catch (final Exception e) {
				log.error("", e);
			}
		}

	}

	@Override
	public void notifyRegListeners() {
		
		final Set<MarketDo> ms = new HashSet<MarketDo>();
		
		for (final MarketDo market : marketMap.values()) {
			ms.add(market);
		}
		
		notifyRegListeners(ms);
	}

	@Override
	public void appendMarketProvider(final MarketFactory marketFactory) {
		throw new UnsupportedOperationException("TODO");
	}

	protected RegTaker<?> getRegTaker(final MarketTaker<?> taker) {
		return takerMap.get(taker);
	}

	protected MarketDo getMarket(final MarketInstrument inst) {
		return marketMap.get(inst);
	}

}
