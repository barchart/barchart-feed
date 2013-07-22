/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openfeed.proto.inst.Decimal;
import org.openfeed.proto.inst.InstrumentDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.inst.InstrumentFuture;
import com.barchart.feed.inst.InstrumentFutureMap;
import com.barchart.feed.inst.InstrumentService;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.FactoryLoader;

public class MockDefinitionService implements InstrumentService<CharSequence> {
	
	private static final Factory factory = FactoryLoader.load();
	
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
		
		InstrumentDefinition.Builder builder = InstrumentDefinition.newBuilder();
		
		builder.setMarketId(INST_GUID_1);
		builder.setSymbol(INST_SYMBOL_1);
		builder.setMinimumPriceIncrement(Decimal.newBuilder().setMantissa(1).setExponent(-1).build());
		builder.setBookDepth(10);
		builder.setDisplayBase(10);
		builder.setDisplayExponent(-1);
		
		guidMap.put(INST_GUID_1, InstrumentFactory.instrument(builder.buildPartial()));
		symbolMap.put(INST_SYMBOL_1, INST_GUID_1);
		
		builder.clear();
		
		builder.setMarketId(INST_GUID_2);
		builder.setSymbol(INST_SYMBOL_2);
		builder.setMinimumPriceIncrement(Decimal.newBuilder().setMantissa(25).setExponent(-2).build());
		builder.setBookDepth(10);
		builder.setDisplayBase(10);
		builder.setDisplayExponent(-1);
		
		guidMap.put(INST_GUID_2, InstrumentFactory.instrument(builder.buildPartial()));
		symbolMap.put(INST_SYMBOL_2, INST_GUID_2);
		
		builder.clear();
		
		builder.setMarketId(INST_GUID_3);
		builder.setSymbol(INST_SYMBOL_3);
		builder.setMinimumPriceIncrement(Decimal.newBuilder().setMantissa(125).setExponent(-3).build());
		builder.setBookDepth(10);
		builder.setDisplayBase(10);
		builder.setDisplayExponent(-1);
		
		guidMap.put(INST_GUID_3, InstrumentFactory.instrument(builder.buildPartial()));
		symbolMap.put(INST_SYMBOL_3, INST_GUID_3);
		
	}
	
	@Override
	public Instrument lookup(final CharSequence symbol) {
		if(symbolMap.containsKey(symbol)) {
			return guidMap.get(symbolMap.get(symbol));
		}
		return Instrument.NULL;
	}

	@Override
	public InstrumentFuture lookupAsync(final CharSequence symbol) {
		
		// TODO
		throw new UnsupportedOperationException();
		
	}

	@Override
	public Map<CharSequence, Instrument> lookup(
			final Collection<CharSequence> symbols) {
		
		final Map<CharSequence, Instrument> insts = 
				new HashMap<CharSequence, Instrument>();
		
		for(final CharSequence symbol : symbols) {
			insts.put(symbol,lookup(symbol));
		}
		
		return insts;
	}

	@Override
	public InstrumentFutureMap<CharSequence> lookupAsync(
			final Collection<CharSequence> symbols) {
		
		return null;
	}
	
}
