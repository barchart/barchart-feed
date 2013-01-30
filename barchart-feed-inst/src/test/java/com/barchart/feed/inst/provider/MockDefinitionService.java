/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import static com.barchart.feed.inst.api.InstrumentField.BOOK_SIZE;
import static com.barchart.feed.inst.api.InstrumentField.FRACTION;
import static com.barchart.feed.inst.api.InstrumentField.ID;
import static com.barchart.feed.inst.api.InstrumentField.PRICE_STEP;
import static com.barchart.feed.inst.api.InstrumentField.SYMBOL;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.InstrumentConst;
import com.barchart.feed.inst.api.InstrumentGUID;
import com.barchart.feed.inst.api.InstrumentService;
import com.barchart.feed.inst.enums.MarketDisplay.Fraction;
import com.barchart.missive.core.Tag;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.provider.ValueBuilder;

public class MockDefinitionService implements InstrumentService<CharSequence> {

	public static final InstrumentGUID INST_GUID_1 = new InstrumentGUIDImpl(1);
	public static final InstrumentGUID INST_GUID_2 = new InstrumentGUIDImpl(2);
	public static final InstrumentGUID INST_GUID_3 = new InstrumentGUIDImpl(3);
	
	public static final TextValue INST_SYMBOL_1 = ValueBuilder.newText("one");
	public static final TextValue INST_SYMBOL_2 = ValueBuilder.newText("two");
	public static final TextValue INST_SYMBOL_3 = ValueBuilder.newText("three");
	
	final Map<InstrumentGUID, Instrument> guidMap = 
			new ConcurrentHashMap<InstrumentGUID, Instrument>();
	final Map<TextValue, Instrument> symbolMap = 
			new ConcurrentHashMap<TextValue, Instrument>();
		
	@SuppressWarnings("rawtypes")
	public MockDefinitionService() {
		
		Map<Tag, Object> tagmap1 = new HashMap<Tag, Object>();
		
		tagmap1.put(ID, ValueBuilder.newText("1"));
		tagmap1.put(SYMBOL, INST_SYMBOL_1);
		tagmap1.put(FRACTION, Fraction.DEC_N01);
		tagmap1.put(PRICE_STEP, ValueBuilder.newPrice(1, -1));
		tagmap1.put(BOOK_SIZE, ValueBuilder.newSize(10));
		
		guidMap.put(INST_GUID_1, InstrumentFactory.build(tagmap1));
		symbolMap.put(INST_SYMBOL_1, InstrumentFactory.build(tagmap1));
		
		Map<Tag, Object> tagmap2 = new HashMap<Tag, Object>();
		
		tagmap2.put(ID, ValueBuilder.newText("2"));
		tagmap2.put(SYMBOL, INST_SYMBOL_2);
		tagmap2.put(FRACTION, Fraction.DEC_N02);
		tagmap2.put(PRICE_STEP, ValueBuilder.newPrice(25, -2));
		tagmap2.put(BOOK_SIZE, ValueBuilder.newSize(10));
		
		guidMap.put(INST_GUID_2, InstrumentFactory.build(tagmap2));
		symbolMap.put(INST_SYMBOL_2, InstrumentFactory.build(tagmap2));
		
		Map<Tag, Object> tagmap3 = new HashMap<Tag, Object>();
		
		tagmap3.put(ID, ValueBuilder.newText("3"));
		tagmap3.put(SYMBOL,  INST_SYMBOL_3);
		tagmap3.put(FRACTION, Fraction.BIN_N03);
		tagmap3.put(PRICE_STEP, ValueBuilder.newPrice(125, -3));
		tagmap3.put(BOOK_SIZE, ValueBuilder.newSize(10));
		
		guidMap.put(INST_GUID_3, InstrumentFactory.build(tagmap3));
		symbolMap.put(INST_SYMBOL_3, InstrumentFactory.build(tagmap3));
		
	}
	
	@Override
	public Instrument lookup(final CharSequence symbol) {
		if(symbolMap.containsKey(symbol)) {
			return symbolMap.get(symbol);
		}
		return InstrumentConst.NULL_INSTRUMENT;
	}

	@Override
	public Future<Instrument> lookupAsync(final CharSequence symbol) {
		// TODO Auto-generated method stub
		return null;
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

}
