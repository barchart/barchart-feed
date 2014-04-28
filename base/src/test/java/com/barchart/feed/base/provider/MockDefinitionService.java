/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.inst.InstrumentService;
import com.barchart.feed.meta.instrument.DefaultInstrument;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.ValueFactory;

public class MockDefinitionService implements InstrumentService<CharSequence> {

	private static final ValueFactory factory = new ValueFactoryImpl();

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(MockDefinitionService.class);

	public static final long INST_GUID_1 = 1;
	public static final long INST_GUID_2 = 2;
	public static final long INST_GUID_3 = 3;

	public static final String INST_SYMBOL_1 = "one";
	public static final String INST_SYMBOL_2 = "two";
	public static final String INST_SYMBOL_3 = "three";

	final Map<Long, Instrument> guidMap =
			new ConcurrentHashMap<Long, Instrument>();
	final Map<String, Long> symbolMap =
			new ConcurrentHashMap<String, Long>();

	final ExecutorService executor = Executors.newCachedThreadPool();

	@SuppressWarnings("rawtypes")
	public MockDefinitionService() {

		final Instrument inst1 = new MockInstrument(
				INST_GUID_1,
				INST_SYMBOL_1,
				factory.newPrice(1, -1),
				10,
				factory.newFraction(10, -1)
				);
		guidMap.put(INST_GUID_1, inst1);
		symbolMap.put(INST_SYMBOL_1, INST_GUID_1);

		final Instrument inst2 = new MockInstrument(
				INST_GUID_2,
				INST_SYMBOL_2,
				factory.newPrice(25, -2),
				10,
				factory.newFraction(10, -1)
				);

		guidMap.put(INST_GUID_2, inst2);
		symbolMap.put(INST_SYMBOL_2, INST_GUID_2);

		final Instrument inst3 = new MockInstrument(
				INST_GUID_3,
				INST_SYMBOL_3,
				factory.newPrice(125, -3),
				10,
				factory.newFraction(10, -1)
				);

		guidMap.put(INST_GUID_3, inst3);
		symbolMap.put(INST_SYMBOL_3, INST_GUID_3);

	}

	@Override
	public Instrument lookup(final CharSequence symbol) {
		if (symbolMap.containsKey(symbol)) {
			return guidMap.get(symbolMap.get(symbol));
		}
		return Instrument.NULL;
	}

	@Override
	public Map<CharSequence, Instrument> lookup(
			final Collection<CharSequence> symbols) {

		final Map<CharSequence, Instrument> insts =
				new HashMap<CharSequence, Instrument>();

		for (final CharSequence symbol : symbols) {
			insts.put(symbol, lookup(symbol));
		}

		return insts;
	}

	@Override
	public Instrument lookup(final InstrumentID id) {
		// TODO Auto-generated method stub
		return null;
	}

	private static class MockInstrument extends DefaultInstrument {

		private final Fraction displayFraction;

		private MockInstrument(final long id_, final String symbol_, final Price tickSize_, final int bookDepth_,
				final Fraction displayFraction_) {

			super(new InstrumentID(String.valueOf(id_)));

			symbol = symbol_;
			tickSize = tickSize_;
			maxBookDepth = factory.newSize(bookDepth_);
			displayFraction = displayFraction_;

		}

		@Override
		public Fraction displayFraction() {
			return displayFraction;
		}

	}

}
