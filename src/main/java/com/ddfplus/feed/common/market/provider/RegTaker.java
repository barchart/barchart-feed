package com.ddfplus.feed.common.market.provider;

import static com.ddfplus.feed.api.market.enums.MarketField.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.anno.ThreadSafe;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.api.market.MarketTaker;
import com.ddfplus.feed.api.market.enums.MarketEvent;
import com.ddfplus.feed.api.market.enums.MarketField;
import com.ddfplus.feed.common.util.concurrent.Runner;
import com.ddfplus.feed.common.util.concurrent.RunnerLoop;

@NotMutable
@ThreadSafe
class RegTaker<V extends Value<V>> implements RunnerLoop<MarketEvent> {

	private static final Logger log = LoggerFactory.getLogger(RegTaker.class);

	private final MarketTaker<V> taker;

	private final MarketField<V> field;

	private final EventSet eventSet;

	private final MarketInstrument[] instruments;

	RegTaker(final MarketTaker<V> taker) {

		this.taker = taker;

		this.field = taker.bindField();

		this.eventSet = new EventSet(taker.bindEvents());

		this.instruments = taker.bindInstruments().clone();

	}

	final MarketTaker<V> getTaker() {
		return taker;
	}

	final void fire(final RegCenter regCenter, final MarketEvent event) {

		final MarketInstrument inst = regCenter.cache(INSTRUMENT);

		final V value = regCenter.cache(field);

		// System.out.println("RegTaker Fire : " + event + " inst" + inst);

		taker.onMarketEvent(event, inst, value);

	}

	@Override
	public final <R> void runLoop(final Runner<R, MarketEvent> task,
			final List<R> list) {

		eventSet.runLoop(task, list);

	}

	final MarketInstrument[] instruments() {
		return instruments;
	}

	static final boolean isValid(final MarketTaker<?> taker) {

		if (taker == null) {
			log.debug("invalid : taker == null");
			return false;
		}

		final MarketEvent[] events = taker.bindEvents();

		if (events == null || events.length == 0) {
			log.debug("invalid : taker.bindEvents()");
			return false;
		}

		final MarketField<?> field = taker.bindField();

		if (field == null) {
			log.debug("invalid : taker.bindField()");
			return false;
		}

		final MarketInstrument[] insts = taker.bindInstruments();

		if (insts == null || insts.length == 0) {
			log.debug("invalid : bindInstruments()");
			return false;
		}

		return true;

	}

}
