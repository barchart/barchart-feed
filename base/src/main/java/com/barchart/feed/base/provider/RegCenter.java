/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.thread.Runner;
import com.barchart.util.values.api.Value;

@NotThreadSafe
class RegCenter {

	static final Logger log = LoggerFactory.getLogger(RegCenter.class);

	/** parent market */
	private final Market market;

	/** current events collector */
	final EventSet eventSet = new EventSet();

	/** registered takers for event */
	private final EventMap<RegTakerList> eventTakerMap = new EventMap<RegTakerList>();

	/** field-value cache during event fire */
	private final FieldMap<Value<?>> fieldValueMap = new FieldMap<Value<?>>();

	@SuppressWarnings("unchecked")
	final <X extends Value<X>> X cache(final MarketField<X> field) {

		X value = (X) fieldValueMap.get(field);

		if (value == null) {
			value = market.get(field).freeze();
			fieldValueMap.put(field, value);
		}

		return value;

	}

	RegCenter(final Market market) {
		this.market = market;
	}
	
	final Instrument instrument() {
		return market.instrument();
	}

	final void eventsClear() {
		eventSet.clear();
	}

	final void eventsAdd(final MarketEvent event) {
		eventSet.add(event);
	}

	final void eventsRemove(final MarketEvent event) {
		eventSet.remove(event);
	}

	final void regAdd(final RegTaker<?> regTaker) {

		final Runner<Void, MarketEvent> task = //
		new Runner<Void, MarketEvent>() {
			@Override
			final public Void run(final MarketEvent event) {

				RegTakerList list = eventTakerMap.get(event);

				if (list == null) {
					list = new RegTakerList();
					eventTakerMap.put(event, list);
				}

				list.add(regTaker);

				return null;

			}
		};

		regTaker.runLoop(task, null);

	}

	final void regRemove(final RegTaker<?> regTaker) {

		final Runner<Void, MarketEvent> task = //
		new Runner<Void, MarketEvent>() {
			@Override
			final public Void run(final MarketEvent event) {

				final RegTakerList list = eventTakerMap.get(event);

				if (list != null) {
					list.remove(regTaker);
					if (list.isEmpty()) {
						eventTakerMap.remove(event);
					}
				}

				return null;

			}
		};

		regTaker.runLoop(task, null);

	}

	final void regUpdate(final RegTaker<?> regTaker) {

		/** past snapshot */
		final EventSet pastEvents = new EventSet();
		pastEvents.bitSet(regTaker.getEvents().bitSet());

		/** next snapshot */
		final EventSet nextEvents = new EventSet(//
				regTaker.getTaker().bindEvents());

		final Runner<Void, MarketEvent> task = //
		new Runner<Void, MarketEvent>() {
			@Override
			final public Void run(final MarketEvent event) {

				final boolean isPast = pastEvents.contains(event);
				final boolean isNext = nextEvents.contains(event);

				// log.debug("isPast : {} ; isNext : {}", isPast, isNext);

				final boolean mustNotChange = !(isPast ^ isNext);

				if (mustNotChange) {
					return null;
				}

				RegTakerList list = eventTakerMap.get(event);

				final boolean mustAdd = !isPast && isNext;

				if (mustAdd) {
					if (list == null) {
						list = new RegTakerList();
						eventTakerMap.put(event, list);
					}
					list.add(regTaker);
				} else {
					if (list != null) {
						list.remove(regTaker);
						if (list.isEmpty()) {
							eventTakerMap.remove(event);
						}
					}
				}

				return null;

			}
		};

		/** remote past */
		pastEvents.runLoop(task, null);

		/** add next */
		nextEvents.runLoop(task, null);

	}

	final void regClear() {
		eventTakerMap.clear();
	}

	final void fireEvents() {

		/** overlap current with registered */
		eventSet.bitMaskAnd(eventTakerMap.bitSet());

		eventSet.runLoop(eventTask, null);

		eventSet.clear();

		fieldValueMap.clear();

	}

	private final Runner<Void, MarketEvent> eventTask = //
	new Runner<Void, MarketEvent>() {

		@Override
		final public Void run(final MarketEvent event) {

			final RegTakerList list = eventTakerMap.get(event);

			final Runner<Void, RegTaker<?>> takerTask = //
			new Runner<Void, RegTaker<?>>() {
				@Override
				public Void run(final RegTaker<?> regTaker) {

					regTaker.fire(RegCenter.this, event);

					return null;

				}
			};

			list.runLoop(takerTask, null);

			return null;

		}
	};

	final boolean isEmptyEvents() {
		return eventSet.isEmpty();
	}

	final boolean isEmptyRegs() {
		return eventTakerMap.isEmpty();
	}

	final List<RegTaker<?>> getRegTakerList() {

		final List<RegTaker<?>> result = new ArrayList<RegTaker<?>>();

		for (final RegTakerList list : eventTakerMap.values()) {
			result.addAll(list);
		}

		return result;

	}

	final Set<MarketEvent> getRegEventSet() {
		return eventTakerMap.keySet();
	}

}
