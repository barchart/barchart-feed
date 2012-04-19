/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.provider;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.thread.Runner;
import com.barchart.util.values.api.Value;

@NotThreadSafe
class RegCenter {

	// parent market
	private final Market market;

	// current events
	private final EventSet eventSet = new EventSet();

	// registered event takers
	private final EventMap<RegTakerList> eventTakerMap = new EventMap<RegTakerList>();

	// field-value cache during event fire
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

	final void eventsClear() {
		eventSet.clear();
	}

	final void eventsAdd(final MarketEvent event) {

		// System.out.println("Adding Event of " + event);

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

				while (list == null) {
					list = new RegTakerList();
					eventTakerMap.put(event, list);
					list = eventTakerMap.get(event);
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

	final void regsClear() {
		eventTakerMap.clear();
	}

	final void fireEvents() {

		// overlap current with registered
		eventSet.bitMaskAnd(eventTakerMap.bitSet());

		// System.out.println("RealFireEvents in RegCenter");

		if (eventSet.isEmpty()) {
			System.out.println("eventSet isEmpty = true, firing no events");
			// return;
		}

		// assert fieldValueMap.isEmpty();

		// see run() below
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

					// System.out.println("running on RegCenter Runner()");

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

	final List<RegTaker<?>> regsTakerList() {

		final List<RegTaker<?>> result = new LinkedList<RegTaker<?>>();

		for (final RegTakerList list : eventTakerMap.values()) {
			result.addAll(list);
		}

		return result;

	}

	final Set<MarketEvent> regEventSet() {
		return eventTakerMap.keySet();
	}

}
