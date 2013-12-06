/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.thread.Runner;
import com.barchart.feed.base.thread.RunnerLoop;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.common.anno.NotMutable;
import com.barchart.util.common.anno.NotThreadSafe;

@NotMutable
@NotThreadSafe
public class RegTaker<V extends Value<V>> implements RunnerLoop<MarketEvent> {

	private static final Logger log = LoggerFactory.getLogger(RegTaker.class);

	private final MarketTaker<V> taker;

	private volatile MarketField<V> field;

	private volatile EventSet eventSet;

	private volatile Instrument[] instruments;

	public RegTaker(final MarketTaker<V> taker) {

		this.taker = taker;

		bind();

	}

	public final void bind() {

		this.field = taker.bindField();

		this.eventSet = new EventSet(taker.bindEvents());

		this.instruments = taker.bindInstruments().clone();

	}

	final MarketTaker<V> getTaker() {
		return taker;
	}

	final void fire(final RegCenter regCenter, final MarketEvent event) {

		final V value = regCenter.cache(field);

		taker.onMarketEvent(event, regCenter.instrument(), value);

	}

	@Override
	public final <R> void runLoop(final Runner<R, MarketEvent> task,
			final List<R> list) {

		eventSet.runLoop(task, list);

	}

	final EventSet getEvents() {
		return eventSet;
	}

	final MarketField<V> getField() {
		return field;
	}

	public final Instrument[] getInstruments() {
		return instruments;
	}

	public static final boolean isValid(final MarketTaker<?> taker) {

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

		final Instrument[] insts = taker.bindInstruments();

		if (insts == null) { // Removed size check
			log.debug("invalid : bindInstruments()");
			return false;
		}

		return true;

	}

	@Override
	public String toString() {

		final StringBuilder text = new StringBuilder(128);

		text.append("filed = ");
		text.append(getField().name());

		// text.append("events = ");
		// text.append(eventSet);

		text.append("instruments = ");
		text.append(Arrays.toString(getInstruments()));

		return text.toString();
	}

}
