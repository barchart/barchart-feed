/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import static com.barchart.feed.inst.InstrumentField.BOOK_DEPTH;
import static com.barchart.feed.inst.InstrumentField.BOOK_LIQUIDITY;
import static com.barchart.feed.inst.InstrumentField.BOOK_STRUCTURE;
import static com.barchart.feed.inst.InstrumentField.CFI_CODE;
import static com.barchart.feed.inst.InstrumentField.CURRENCY_CODE;
import static com.barchart.feed.inst.InstrumentField.DESCRIPTION;
import static com.barchart.feed.inst.InstrumentField.DISPLAY_FRACTION;
import static com.barchart.feed.inst.InstrumentField.EXCHANGE_CODE;
import static com.barchart.feed.inst.InstrumentField.FIELDS;
import static com.barchart.feed.inst.InstrumentField.GUID;
import static com.barchart.feed.inst.InstrumentField.LIFETIME;
import static com.barchart.feed.inst.InstrumentField.MARKET_GUID;
import static com.barchart.feed.inst.InstrumentField.MARKET_HOURS;
import static com.barchart.feed.inst.InstrumentField.POINT_VALUE;
import static com.barchart.feed.inst.InstrumentField.SECURITY_TYPE;
import static com.barchart.feed.inst.InstrumentField.SYMBOL;
import static com.barchart.feed.inst.InstrumentField.TICK_SIZE;
import static com.barchart.feed.inst.InstrumentField.TIME_ZONE_NAME;
import static com.barchart.feed.inst.InstrumentField.TIME_ZONE_OFFSET;
import static com.barchart.feed.inst.InstrumentField.VENDOR;
import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static com.barchart.util.values.provider.ValueBuilder.newSize;
import static com.barchart.util.values.provider.ValueBuilder.newText;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.enums.MarketCurrency;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.missive.api.TagMapSafe;
import com.barchart.missive.core.ObjectMapFactory;
import com.barchart.missive.hash.HashTagMapSafe;
import com.barchart.proto.buf.inst.BookLiquidity;
import com.barchart.proto.buf.inst.BookStructure;
import com.barchart.proto.buf.inst.Calendar;
import com.barchart.proto.buf.inst.Decimal;
import com.barchart.proto.buf.inst.InstrumentDefinition;
import com.barchart.proto.buf.inst.InstrumentType;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.FactoryLoader;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.values.api.PriceValue;

public final class InstrumentProtoBuilder {
	
	private static final Logger log = LoggerFactory
			.getLogger(InstrumentProtoBuilder.class);
	
	private static final Factory factory = FactoryLoader.load();

	private static final BiEnumMap<Instrument.SecurityType, InstrumentType> secTypeMap = new BiEnumMap<Instrument.SecurityType, InstrumentType>(
			new Instrument.SecurityType[] { Instrument.SecurityType.NULL_TYPE, Instrument.SecurityType.FOREX,
					Instrument.SecurityType.INDEX, Instrument.SecurityType.EQUITY,
					Instrument.SecurityType.FUTURE, Instrument.SecurityType.OPTION,
					Instrument.SecurityType.SPREAD }, new InstrumentType[] {
					InstrumentType.NO_TYPE_INST, InstrumentType.FOREX_INST,
					InstrumentType.INDEX_INST, InstrumentType.EQUITY_INST,
					InstrumentType.FUTURE_INST, InstrumentType.OPTION_INST,
					InstrumentType.SPREAD_INST });

	private static final BiEnumMap<Instrument.BookLiquidityType, BookLiquidity> liqidityTypeMap = new BiEnumMap<Instrument.BookLiquidityType, BookLiquidity>(
			new Instrument.BookLiquidityType[] { Instrument.BookLiquidityType.NONE,
					Instrument.BookLiquidityType.DEFAULT, Instrument.BookLiquidityType.IMPLIED,
					Instrument.BookLiquidityType.COMBINED }, new BookLiquidity[] {
					BookLiquidity.NO_BOOK_LIQUIDITY,
					BookLiquidity.DEFAULT_LIQUIDITY,
					BookLiquidity.IMPLIED_LIQUIDITY,
					BookLiquidity.COMBINED_LIQUIDITY });

	private static final BiEnumMap<Instrument.BookStructureType, BookStructure> structTypeMap = new BiEnumMap<Instrument.BookStructureType, BookStructure>(
			new Instrument.BookStructureType[] { Instrument.BookStructureType.NONE,
					Instrument.BookStructureType.PRICE_LEVEL,
					Instrument.BookStructureType.PRICE_VALUE,
					Instrument.BookStructureType.ORDER_NUMBER }, new BookStructure[] {
					BookStructure.NO_BOOK_STRUCTURE,
					BookStructure.PRICE_LEVEL_STRUCTURE,
					BookStructure.PRICE_VALUE_STRUCTURE,
					BookStructure.ORDER_NUMBER_STRUCTURE });

	private InstrumentProtoBuilder() {

	}

	public static InstrumentDefinition buildInstDef(final InstrumentBase inst) {

		if (inst == null || inst.equals(Instrument.NULL)) {
			return null; // Return empty instrument def
		}

		final InstrumentDefinition.Builder builder = InstrumentDefinition
				.newBuilder();

		/* market identifier; must be globally unique; */
		builder.setMarketId(Long.parseLong(inst.get(MARKET_GUID).toString()));

		/* type of security, Forex, Equity, etc. */
		if (inst.contains(SECURITY_TYPE)) {
			builder.setInstrumentType(secTypeMap.getValue(inst
					.get(SECURITY_TYPE)));
		}

		/* liquidy type, default / implied / combined */
		if (inst.contains(BOOK_LIQUIDITY)) {
			builder.setBookLiquidity(liqidityTypeMap.getValue(inst
					.get(BOOK_LIQUIDITY)));
		}

		/* structure of book */
		if (inst.contains(BOOK_STRUCTURE)) {
			builder.setBookStructure(structTypeMap.getValue(inst
					.get(BOOK_STRUCTURE)));
		}

		/* book depth */
		if (inst.contains(BOOK_DEPTH)) {
			builder.setBookDepth((int) inst.get(BOOK_DEPTH).asLong());
		}

		/* vendor */
		if (inst.contains(VENDOR)) {
			builder.setVendorId(inst.get(VENDOR).toString());
		}

		/* market symbol; can be non unique; */
		if (inst.contains(SYMBOL)) {
			builder.setSymbol(inst.get(SYMBOL).toString());
		}

		/* market free style description; can be used in full text search */
		if (inst.contains(DESCRIPTION)) {
			builder.setDescription(inst.get(DESCRIPTION).toString());
		}

		/* stock vs future vs etc. */
		if (inst.contains(CFI_CODE)) {
			builder.setCfiCode(inst.get(CFI_CODE).toString());
		}

		/* price currency */
		if (inst.contains(CURRENCY_CODE)) {
			builder.setCurrencyCode(inst.get(CURRENCY_CODE).name());
		}

		/* market originating exchange identifier */
		if (inst.contains(EXCHANGE_CODE)) {
			builder.setExchangeCode(inst.get(EXCHANGE_CODE).toString());
		}

		/* price step / increment size / tick size */
		if (inst.contains(TICK_SIZE)) {
			final PriceValue step = inst.get(TICK_SIZE).norm();
			builder.setMinimumPriceIncrement(build(step));
		}

		/* value of a future contract / stock share */
		if (inst.contains(POINT_VALUE)) {
			final PriceValue val = inst.get(POINT_VALUE).norm();
			builder.setContractPointValue(build(val));
		}

		/* display fraction base : decimal(10) vs binary(2), etc. */
		if (inst.contains(DISPLAY_FRACTION)) {
			builder.setDisplayBase((int) inst.get(DISPLAY_FRACTION).base());
			builder.setDisplayExponent(inst.get(DISPLAY_FRACTION).exponent());
		}

		/* Calendar */
		if (inst.contains(LIFETIME) && inst.contains(MARKET_HOURS)) {
			final Calendar.Builder calBuilder = Calendar.newBuilder();
			final Interval.Builder intBuilder = Interval.newBuilder();
			intBuilder.setTimeStart(inst.get(LIFETIME).start().millisecond());
			intBuilder.setTimeFinish(inst.get(LIFETIME).stop().millisecond());

			/* lifetime of instrument */
			calBuilder.setLifeTime(intBuilder.build());

			intBuilder.clear();
			for (final TimeInterval ti : inst.get(MARKET_HOURS)) {
				intBuilder.setTimeStart(ti.start().millisecond());
				intBuilder.setTimeFinish(ti.stop().millisecond());
				calBuilder.addMarketHours(intBuilder.build());
				intBuilder.clear();
			}

			/*
			 * array of intervals of market hours in a normal week, denoted in
			 * minutes from Sunday morning
			 */
			builder.setCalendar(calBuilder.build());
		}

		/* timezone represented as offset in minutes from utc */
		if (inst.contains(TIME_ZONE_OFFSET)) {
			builder.setTimeZoneOffset((int) inst.get(TIME_ZONE_OFFSET).asLong());
		}

		/* time zone name as text */
		if (inst.contains(TIME_ZONE_NAME)) {
			builder.setTimeZoneName(inst.get(TIME_ZONE_NAME).toString());
		}

		return builder.build();
	}

	public static Instrument buildInstrument(final InstrumentDefinition instDef) {

		final TagMapSafe map = new HashTagMapSafe(FIELDS);

		if (instDef.hasMarketId()) {
			map.set(GUID, new InstrumentGUID(String.valueOf(instDef.getMarketId())));
			map.set(MARKET_GUID, newText(String.valueOf(instDef.getMarketId())));
		} else {
			log.warn("Inst def had no market id, returning null instrument: \n{}", instDef.toString());
			return Instrument.NULL;
		}

		if (instDef.hasInstrumentType()) {
			map.set(SECURITY_TYPE,
					secTypeMap.getKey(instDef.getInstrumentType()));
		}

		if (instDef.hasBookLiquidity()) {
			map.set(BOOK_LIQUIDITY,
					liqidityTypeMap.getKey(instDef.getBookLiquidity()));
		}

		if (instDef.hasBookStructure()) {
			map.set(BOOK_STRUCTURE,
					structTypeMap.getKey(instDef.getBookStructure()));
		}

		if (instDef.hasBookDepth()) {
			map.set(BOOK_DEPTH, newSize(instDef.getBookDepth()));
		}

		if (instDef.hasVendorId()) {
			map.set(VENDOR, newText(instDef.getVendorId()));
		}

		if (instDef.hasSymbol()) {
			map.set(SYMBOL, newText(instDef.getSymbol()));
		}

		if (instDef.hasDescription()) {
			map.set(DESCRIPTION, newText(instDef.getDescription()));
		}

		if (instDef.hasCfiCode()) {
			map.set(CFI_CODE, newText(instDef.getCfiCode()));
		}

		if (instDef.hasCurrencyCode()) {
			map.set(CURRENCY_CODE,
					MarketCurrency.fromString(instDef.getCurrencyCode()));
		}

		if (instDef.hasExchangeCode()) {
			map.set(EXCHANGE_CODE, newText(instDef.getExchangeCode()));
		}

		if (instDef.hasMinimumPriceIncrement()) {
			map.set(TICK_SIZE,
					priceFromDecimal(instDef.getMinimumPriceIncrement()));
		}

		if (instDef.hasContractPointValue()) {
			map.set(POINT_VALUE,
					priceFromDecimal(instDef.getContractPointValue()));
		}

		if (instDef.hasDisplayBase() && instDef.hasDisplayExponent()) {
			map.set(DISPLAY_FRACTION,
					factory.newFraction(instDef.getDisplayBase(),
							instDef.getDisplayExponent()));
		}

		if (instDef.hasCalendar()) {
			final Interval i = instDef.getCalendar().getLifeTime();
			
			if(i.getTimeFinish() > 0) {
				map.set(LIFETIME, factory.newTimeInterval(i.getTimeStart(), i.getTimeFinish()));
			} else {
				map.set(LIFETIME, TimeInterval.NULL);
			}
			
			final List<Interval> ints = instDef.getCalendar()
					.getMarketHoursList();
			final TimeInterval[] tints = new TimeInterval[ints.size()];
			for (int n = 0; n < ints.size(); n++) {
				tints[n] = factory.newTimeInterval(
						ints.get(n).getTimeStart(), ints.get(n).getTimeFinish());
			}
			map.set(MARKET_HOURS, factory.newSchedule(tints));
		}

		if (instDef.hasTimeZoneOffset()) {
			map.set(TIME_ZONE_OFFSET, newSize(instDef.getTimeZoneOffset()));
		}

		if (instDef.hasTimeZoneName()) {
			map.set(TIME_ZONE_NAME, newText(instDef.getTimeZoneName()));
		}
		
		InstrumentImpl inst = ObjectMapFactory.build(InstrumentImpl.class, map);
		
		final List<Long> ids = instDef.getComponentIdList();
		for(final Long id : ids) {
			inst.componentLegs.add(new InstrumentGUID(String.valueOf(id)));
		}
		
		return inst;
	}

	static PriceValue priceFromDecimal(final Decimal d) {
		return newPrice(d.getMantissa(), d.getExponent());
	}
	
	// TODO Map ordinal values
	private static class BiEnumMap<K extends Enum<K>, V extends Enum<V>> {

		private final K[] keys;
		private final V[] vals;

		public BiEnumMap(final K[] keys, final V[] vals) {
			this.keys = keys;
			this.vals = vals;
		}

		public V getValue(final K key) {
			return vals[key.ordinal()];
		}

		public K getKey(final V val) {
			return keys[val.ordinal()];
		}

	}
	
	public static Decimal build(final long mantissa, final int exponent) {

		final Decimal.Builder builder = Decimal.newBuilder();
		
		builder.clear();

		builder.setMantissa(mantissa);
		builder.setExponent(exponent);

		return builder.build();

	}

	public static Decimal build(final PriceValue price) {
		
		final Decimal.Builder builder = Decimal.newBuilder();
		
		builder.clear();

		builder.setMantissa(price.mantissa());
		builder.setExponent(price.exponent());

		return builder.build();
	}

}
