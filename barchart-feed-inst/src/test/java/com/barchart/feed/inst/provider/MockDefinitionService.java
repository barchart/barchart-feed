/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import static com.barchart.feed.inst.api.InstrumentField.BOOK_DEPTH;
import static com.barchart.feed.inst.api.InstrumentField.MARKET_ID;
import static com.barchart.feed.inst.api.InstrumentField.PRICE_STEP;
import static com.barchart.feed.inst.api.InstrumentField.SYMBOL;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.InstrumentFuture;
import com.barchart.feed.inst.api.InstrumentGUID;
import com.barchart.feed.inst.api.InstrumentService;
import com.barchart.feed.inst.api.MetadataContext;
import com.barchart.feed.inst.enums.MarketDisplay.Fraction;
import com.barchart.missive.core.Tag;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.provider.ValueBuilder;

public class MockDefinitionService implements InstrumentService<CharSequence> {
	
	private static final Logger log = LoggerFactory
			.getLogger(MockDefinitionService.class);

	public static final InstrumentGUID INST_GUID_1 = new InstrumentGUIDImpl(1);
	public static final InstrumentGUID INST_GUID_2 = new InstrumentGUIDImpl(2);
	public static final InstrumentGUID INST_GUID_3 = new InstrumentGUIDImpl(3);
	
	public static final TextValue INST_SYMBOL_1 = ValueBuilder.newText("one");
	public static final TextValue INST_SYMBOL_2 = ValueBuilder.newText("two");
	public static final TextValue INST_SYMBOL_3 = ValueBuilder.newText("three");
	
	final Map<InstrumentGUID, Instrument> guidMap = 
			new ConcurrentHashMap<InstrumentGUID, Instrument>();
	final Map<TextValue, InstrumentGUID> symbolMap = 
			new ConcurrentHashMap<TextValue, InstrumentGUID>();
		
	@SuppressWarnings("rawtypes")
	public MockDefinitionService() {
		
		Map<Tag, Object> tagmap1 = new HashMap<Tag, Object>();
		
		tagmap1.put(MARKET_ID, ValueBuilder.newText("1"));
		tagmap1.put(SYMBOL, INST_SYMBOL_1);
		tagmap1.put(PRICE_STEP, ValueBuilder.newPrice(1, -1));
		tagmap1.put(BOOK_DEPTH, ValueBuilder.newSize(10));
		
		guidMap.put(INST_GUID_1, InstrumentFactory.build(tagmap1));
		symbolMap.put(INST_SYMBOL_1, INST_GUID_1);
		
		Map<Tag, Object> tagmap2 = new HashMap<Tag, Object>();
		
		tagmap2.put(MARKET_ID, ValueBuilder.newText("2"));
		tagmap2.put(SYMBOL, INST_SYMBOL_2);
		tagmap2.put(PRICE_STEP, ValueBuilder.newPrice(25, -2));
		tagmap2.put(BOOK_DEPTH, ValueBuilder.newSize(10));
		
		guidMap.put(INST_GUID_2, InstrumentFactory.build(tagmap2));
		symbolMap.put(INST_SYMBOL_2, INST_GUID_2);
		
		Map<Tag, Object> tagmap3 = new HashMap<Tag, Object>();
		
		tagmap3.put(MARKET_ID, ValueBuilder.newText("3"));
		tagmap3.put(SYMBOL,  INST_SYMBOL_3);
		tagmap3.put(PRICE_STEP, ValueBuilder.newPrice(125, -3));
		tagmap3.put(BOOK_DEPTH, ValueBuilder.newSize(10));
		
		guidMap.put(INST_GUID_3, InstrumentFactory.build(tagmap3));
		symbolMap.put(INST_SYMBOL_3, INST_GUID_3);
		
	}
	
	@Override
	public Instrument lookup(final CharSequence symbol) {
		if(symbolMap.containsKey(symbol)) {
			return guidMap.get(symbolMap.get(symbol));
		}
		return Instrument.NULL_INSTRUMENT;
	}

	@Override
	public InstrumentFuture lookupAsync(final CharSequence symbol) {
		
		InstrumentGUID guid = symbolMap.get(symbol);
		if(guid == null) {
			guid = InstrumentGUID.NULL_INSTRUMENT_GUID;
		}
		
		return new InstrumentFutureImpl(guid, randomDelayContext, executor);
		
	}

	@Override
	public Map<CharSequence, Instrument> lookup(final Collection<? extends CharSequence> symbols) {
		
		final Map<CharSequence, Instrument> insts = new HashMap<CharSequence, Instrument>();
		for(final CharSequence symbol : symbols) {
			insts.put(symbol,lookup(symbol));
		}
		
		return insts;
	}

	@Override
	public Map<CharSequence, Future<Instrument>> lookupAsync(
			final Collection<? extends CharSequence> symbols) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Executor executor = new Executor() {

		private final AtomicLong counter = new AtomicLong(0);

		final String name = "# DDF Client - " + counter.getAndIncrement();

		@Override
		public void execute(final Runnable task) {
			log.debug("executing new runnable = " + task.toString());
			new Thread(task, name).start();
		}

	};
	
	private MetadataContext randomDelayContext = new MetadataContext() {

		@Override
		public Instrument lookup(final InstrumentGUID guid) {
			try {
				Thread.sleep((long) (Math.random() * 100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return guidMap.get(guid);
			
		}
		
	};

}