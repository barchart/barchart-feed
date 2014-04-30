/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import org.openfeed.InstrumentDefinition;
import org.openfeed.InstrumentDefinition.BookLiquidity;
import org.openfeed.InstrumentDefinition.BookStructure;
import org.openfeed.InstrumentDefinition.Calendar;
import org.openfeed.InstrumentDefinition.Decimal;
import org.openfeed.InstrumentDefinition.InstrumentType;
import org.openfeed.InstrumentDefinition.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;

public final class InstrumentProtoBuilder {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(InstrumentProtoBuilder.class);
	
	@SuppressWarnings("unused")
	private static final ValueFactory factory = ValueFactoryImpl.instance;

	private static final BiEnumMap<Instrument.SecurityType, InstrumentType> secTypeMap = 
			new BiEnumMap<Instrument.SecurityType, InstrumentType>(
				new Instrument.SecurityType[] { Instrument.SecurityType.NULL_TYPE, Instrument.SecurityType.FOREX,
					Instrument.SecurityType.INDEX, Instrument.SecurityType.EQUITY,
					Instrument.SecurityType.FUTURE, Instrument.SecurityType.OPTION}, new InstrumentType[] {
					InstrumentType.NO_INSTRUMENT, InstrumentType.FOREX_INSTRUMENT,
					InstrumentType.INDEX_INSTRUMENT, InstrumentType.EQUITY_INSTRUMENT,
					InstrumentType.FUTURE_INSTRUMENT, InstrumentType.OPTION_INSTRUMENT,
					InstrumentType.SPREAD_INSTRUMENT });

	private static final BiEnumMap<Instrument.BookLiquidityType, BookLiquidity> liqidityTypeMap = 
			new BiEnumMap<Instrument.BookLiquidityType, BookLiquidity>(
				new Instrument.BookLiquidityType[] { Instrument.BookLiquidityType.NONE,
					Instrument.BookLiquidityType.DEFAULT, Instrument.BookLiquidityType.IMPLIED,
					Instrument.BookLiquidityType.COMBINED }, new BookLiquidity[] {
					BookLiquidity.NO_BOOK_LIQUIDITY,
					BookLiquidity.DEFAULT_LIQUIDITY,
					BookLiquidity.IMPLIED_LIQUIDITY,
					BookLiquidity.COMBINED_LIQUIDITY });

	private static final BiEnumMap<Instrument.BookStructureType, BookStructure> structTypeMap = 
			new BiEnumMap<Instrument.BookStructureType, BookStructure>(
				new Instrument.BookStructureType[] { Instrument.BookStructureType.NONE,
					Instrument.BookStructureType.PRICE_LEVEL,
					Instrument.BookStructureType.PRICE_VALUE,
					Instrument.BookStructureType.ORDER_NUMBER }, new BookStructure[] {
					BookStructure.NO_BOOK_STRUCTURE,
					BookStructure.LEVEL_STRUCTURE,
					BookStructure.PRICE_STRUCTURE,
					BookStructure.ORDER_STRUCTURE });

	private InstrumentProtoBuilder() {

	}

	public static InstrumentDefinition buildInstDef(final Instrument inst) {

		if (inst == null || inst.equals(Instrument.NULL)) {
			return null; // Return empty instrument def
		}

		final InstrumentDefinition.Builder builder = InstrumentDefinition
				.newBuilder();

		/* market identifier; must be globally unique; */
		builder.setMarketId(Long.parseLong(inst.marketGUID()));

		/* type of security, Forex, Equity, etc. */
		builder.setInstrumentType(secTypeMap.getValue(inst.securityType()));

		/* liquidy type, default / implied / combined */
		builder.setBookLiquidity(liqidityTypeMap.getValue(inst.liquidityType()));

		/* structure of book */
		builder.setBookStructure(structTypeMap.getValue(inst.bookStructure()));

		/* book depth */
		if (inst.maxBookDepth() != Size.NULL) {
			builder.setBookDepth((int) inst.maxBookDepth().asDouble());
		}

		/* vendor */
		builder.setVendorId(inst.instrumentDataVendor());

		/* market symbol; can be non unique; */
		builder.setSymbol(inst.symbol());

		/* market free style description; can be used in full text search */
		builder.setDescription(inst.description());

		/* stock vs future vs etc. */
		builder.setCfiCode(inst.CFICode());

		/* market originating exchange identifier */
		builder.setExchangeCode(inst.exchangeCode());

		/* price step / increment size / tick size */
		if (inst.tickSize() != Price.NULL) {
			builder.setMinimumPriceIncrement(build(inst.tickSize()));
		}

		/* value of a future contract / stock share */
		if (inst.pointValue() != Price.NULL) {
			builder.setContractPointValue(build(inst.pointValue()));
		}

		/* display fraction base : decimal(10) vs binary(2), etc. */
		if (!inst.displayFraction().isNull()) {
			builder.setDisplayBase((int) inst.displayFraction().base());
			builder.setDisplayExponent(inst.displayFraction().exponent());
		}

		/* Calendar */
		if (inst.lifetime() != TimeInterval.NULL && inst.marketHours() != Schedule.NULL) {
			final Calendar.Builder calBuilder = Calendar.newBuilder();
			final Interval.Builder intBuilder = Interval.newBuilder();
			intBuilder.setTimeStart(inst.lifetime().start().millisecond());
			intBuilder.setTimeFinish(inst.lifetime().stop().millisecond());

			intBuilder.clear();
			for (final TimeInterval ti : inst.marketHours()) {
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

		/* time zone name as text */
		builder.setTimeZoneName(inst.timeZoneName());

		return builder.build();
	}

//	public static Instrument buildInstrument(final InstrumentDefinition instDef) {
//
//		final TagMapSafe map = new HashTagMapSafe(FIELDS);
//
//		if (instDef.hasMarketId()) {
//			map.set(GUID, new InstrumentGUID(String.valueOf(instDef.getMarketId())));
//			map.set(MARKET_GUID, newText(String.valueOf(instDef.getMarketId())));
//		} else {
//			log.warn("Inst def had no market id, returning null instrument: \n{}", instDef.toString());
//			return Instrument.NULL;
//		}
//
//		if (instDef.hasInstrumentType()) {
//			map.set(SECURITY_TYPE,
//					secTypeMap.getKey(instDef.getInstrumentType()));
//		}
//
//		if (instDef.hasBookLiquidity()) {
//			map.set(BOOK_LIQUIDITY,
//					liqidityTypeMap.getKey(instDef.getBookLiquidity()));
//		}
//
//		if (instDef.hasBookStructure()) {
//			map.set(BOOK_STRUCTURE,
//					structTypeMap.getKey(instDef.getBookStructure()));
//		}
//
//		if (instDef.hasBookDepth()) {
//			map.set(BOOK_DEPTH, newSize(instDef.getBookDepth()));
//		}
//
//		if (instDef.hasVendorId()) {
//			map.set(VENDOR, newText(instDef.getVendorId()));
//		}
//
//		if (instDef.hasSymbol()) {
//			map.set(SYMBOL, newText(instDef.getSymbol()));
//		}
//
//		if (instDef.hasDescription()) {
//			map.set(DESCRIPTION, newText(instDef.getDescription()));
//		}
//
//		if (instDef.hasCfiCode()) {
//			map.set(CFI_CODE, newText(instDef.getCfiCode()));
//		}
//
//		if (instDef.hasExchangeCode()) {
//			map.set(EXCHANGE, Exchanges.fromCode(instDef.getExchangeCode()));
//			map.set(EXCHANGE_CODE, newText(instDef.getExchangeCode()));
//		} else {
//			map.set(EXCHANGE, Exchange.NULL);
//			map.set(EXCHANGE_CODE, newText(Exchanges.NULL_CODE));
//		}
//
//		if (instDef.hasMinimumPriceIncrement()) {
//			map.set(TICK_SIZE,
//					priceFromDecimal(instDef.getMinimumPriceIncrement()));
//		}
//
//		if (instDef.hasContractPointValue()) {
//			map.set(POINT_VALUE,
//					priceFromDecimal(instDef.getContractPointValue()));
//		}
//
//		if (instDef.hasDisplayBase() && instDef.hasDisplayExponent()) {
//			map.set(DISPLAY_FRACTION,
//					factory.newFraction(instDef.getDisplayBase(),
//							instDef.getDisplayExponent()));
//		}
//
//		if (instDef.hasCalendar()) {
//			final Interval i = instDef.getCalendar().getLifeTime();
//			
//			if(i.getTimeFinish() > 0) {
//				map.set(LIFETIME, factory.newTimeInterval(i.getTimeStart(), i.getTimeFinish()));
//			} else {
//				map.set(LIFETIME, TimeInterval.NULL);
//			}
//			
//			final List<Interval> ints = instDef.getCalendar()
//					.getMarketHoursList();
//			final TimeInterval[] tints = new TimeInterval[ints.size()];
//			for (int n = 0; n < ints.size(); n++) {
//				tints[n] = factory.newTimeInterval(
//						ints.get(n).getTimeStart(), ints.get(n).getTimeFinish());
//			}
//			map.set(MARKET_HOURS, factory.newSchedule(tints));
//		}
//
//		// TODO TimeZoneOffset Removed?
////		if (instDef.hasTimeZoneOffset()) {
////			map.set(TIME_ZONE_OFFSET, newSize(instDef.getTimeZoneOffset()));
////		}
//
//		if (instDef.hasTimeZoneName()) {
//			map.set(TIME_ZONE_NAME, newText(instDef.getTimeZoneName()));
//		}
//		
//		InstrumentImpl inst = ObjectMapFactory.build(InstrumentImpl.class, map);
//		
//		final List<Long> ids = instDef.getComponentIdList();
//		for(final Long id : ids) {
//			inst.componentLegs.add(new InstrumentBase.InstIdentifier(String.valueOf(id)));
//		}
//		
//		return inst;
//	}

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

		@SuppressWarnings("unused")
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

	public static Decimal build(final Price price) {
		
		final Decimal.Builder builder = Decimal.newBuilder();
		
		builder.clear();

		builder.setMantissa(price.mantissa());
		builder.setExponent(price.exponent());

		return builder.build();
	}

}
