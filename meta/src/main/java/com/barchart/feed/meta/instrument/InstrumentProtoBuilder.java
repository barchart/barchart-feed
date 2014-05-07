/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.meta.instrument;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.openfeed.InstrumentDefinition;
import org.openfeed.InstrumentDefinition.BookLiquidity;
import org.openfeed.InstrumentDefinition.BookStructure;
import org.openfeed.InstrumentDefinition.Decimal;
import org.openfeed.InstrumentDefinition.EventType;
import org.openfeed.InstrumentDefinition.InstrumentType;
import org.openfeed.InstrumentDefinition.Symbol;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.Instrument.SecurityType;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.model.meta.instrument.Event;
import com.barchart.feed.api.model.meta.instrument.PriceFormat;
import com.barchart.feed.api.model.meta.instrument.SpreadLeg;
import com.barchart.feed.api.model.meta.instrument.SpreadType;
import com.barchart.feed.api.model.meta.instrument.TimeSpan;
import com.barchart.util.value.api.Price;

public final class InstrumentProtoBuilder {

	private static final BiEnumMap<Instrument.SecurityType, InstrumentType> secTypeMap =
			BiEnumMap.create(
					new Instrument.SecurityType[] {
							Instrument.SecurityType.NULL_TYPE,
							Instrument.SecurityType.FOREX,
							Instrument.SecurityType.INDEX,
							Instrument.SecurityType.EQUITY,
							Instrument.SecurityType.FUTURE,
							Instrument.SecurityType.OPTION,
							Instrument.SecurityType.SPREAD
					}, new InstrumentType[] {
							InstrumentType.NO_INSTRUMENT,
							InstrumentType.FOREX_INSTRUMENT,
							InstrumentType.INDEX_INSTRUMENT,
							InstrumentType.EQUITY_INSTRUMENT,
							InstrumentType.FUTURE_INSTRUMENT,
							InstrumentType.OPTION_INSTRUMENT,
							InstrumentType.SPREAD_INSTRUMENT
					});

	private static final BiEnumMap<Instrument.BookLiquidityType, BookLiquidity> liqidityTypeMap =
			BiEnumMap.create(
					new Instrument.BookLiquidityType[] {
							Instrument.BookLiquidityType.NONE,
							Instrument.BookLiquidityType.DEFAULT,
							Instrument.BookLiquidityType.IMPLIED,
							Instrument.BookLiquidityType.COMBINED
					}, new BookLiquidity[] {
							BookLiquidity.NO_BOOK_LIQUIDITY,
							BookLiquidity.DEFAULT_LIQUIDITY,
							BookLiquidity.IMPLIED_LIQUIDITY,
							BookLiquidity.COMBINED_LIQUIDITY
					});

	private static final BiEnumMap<Instrument.BookStructureType, BookStructure> structTypeMap =
			BiEnumMap.create(
					new Instrument.BookStructureType[] {
							Instrument.BookStructureType.NONE,
							Instrument.BookStructureType.PRICE_LEVEL,
							Instrument.BookStructureType.PRICE_VALUE,
							Instrument.BookStructureType.ORDER_NUMBER
					}, new BookStructure[] {
							BookStructure.NO_BOOK_STRUCTURE,
							BookStructure.LEVEL_STRUCTURE,
							BookStructure.PRICE_STRUCTURE,
							BookStructure.ORDER_STRUCTURE
					});

	private static final BiEnumMap<Event.Type, EventType> eventTypeMap =
			BiEnumMap.create(
					new Event.Type[] {
							Event.Type.FIRST_DELIVERY_DATE,
							Event.Type.FIRST_HOLDING_DATE,
							Event.Type.FIRST_NOTICE_DATE,
							Event.Type.FIRST_POSITION_DATE,
							Event.Type.FIRST_TRADE_DATE,
							Event.Type.LAST_DELIVERY_DATE,
							Event.Type.LAST_HOLDING_DATE,
							Event.Type.LAST_NOTICE_DATE,
							Event.Type.LAST_POSITION_DATE,
							Event.Type.LAST_TRADE_DATE,
							Event.Type.SETTLEMENT_DATE
					}, new EventType[] {
							EventType.FIRST_DELIVERY_DATE,
							EventType.FIRST_HOLDING_DATE,
							EventType.FIRST_NOTICE_DATE,
							EventType.FIRST_POSITION_DATE,
							EventType.FIRST_TRADE_DATE,
							EventType.LAST_DELIVERY_DATE,
							EventType.LAST_HOLDING_DATE,
							EventType.LAST_NOTICE_DATE,
							EventType.LAST_POSITION_DATE,
							EventType.LAST_TRADE_DATE,
							EventType.SETTLEMENT_DATE
					});

	private static final BiEnumMap<PriceFormat.SubFormat, InstrumentDefinition.PriceFormat.SubFormat> subFormatMap =
			BiEnumMap.create(
					new PriceFormat.SubFormat[] {
							PriceFormat.SubFormat.DECIMAL,
							PriceFormat.SubFormat.FLAT,
							PriceFormat.SubFormat.FRACTIONAL
					}, new InstrumentDefinition.PriceFormat.SubFormat[] {
							InstrumentDefinition.PriceFormat.SubFormat.DECIMAL,
							InstrumentDefinition.PriceFormat.SubFormat.FLAT,
							InstrumentDefinition.PriceFormat.SubFormat.FRACTIONAL
					});

	private static final BiEnumMap<Instrument.OptionStyle, InstrumentDefinition.OptionStyle> optionStyleMap =
			BiEnumMap.create(
					new Instrument.OptionStyle[] {
							Instrument.OptionStyle.DEFAULT,
							Instrument.OptionStyle.AMERICAN,
							Instrument.OptionStyle.EUROPEAN
					}, new InstrumentDefinition.OptionStyle[] {
							InstrumentDefinition.OptionStyle.DEFAULT_STYLE,
							InstrumentDefinition.OptionStyle.AMERICAN_STYLE,
							InstrumentDefinition.OptionStyle.EUROPEAN_STYLE
					});

	private static final BiEnumMap<Instrument.OptionType, InstrumentDefinition.OptionType> optionTypeMap =
			BiEnumMap.create(
					new Instrument.OptionType[] {
							Instrument.OptionType.CALL,
							Instrument.OptionType.PUT
					}, new InstrumentDefinition.OptionType[] {
							InstrumentDefinition.OptionType.CALL_OPTION,
							InstrumentDefinition.OptionType.PUT_OPTION
					});

	private static final BiEnumMap<SpreadType, InstrumentDefinition.SpreadType> spreadTypeMap =
			BiEnumMap.create(
					new SpreadType[] {
					}, new InstrumentDefinition.SpreadType[] {
					});

	private InstrumentProtoBuilder() {

	}

	public static InstrumentDefinition buildInstDef(final Instrument inst) {

		// Shortcut
		if (inst instanceof ProtobufInstrument) {
			return ((ProtobufInstrument) inst).definition();
		}

		if (inst == null || inst.equals(Instrument.NULL)) {
			throw new IllegalArgumentException("Instrument cannot be null");
		}

		final InstrumentDefinition.Builder builder = InstrumentDefinition.newBuilder();

		/* market identifier; must be globally unique; */
		if (!inst.id().isNull()) {
			builder.setMarketId(Long.parseLong(inst.id().id()));
		}

		/* type of security, Forex, Equity, etc. */
		if (inst.securityType() != SecurityType.NULL_TYPE) {
			builder.setInstrumentType(secTypeMap.getValue(inst.securityType()));
		}

		/* liquidy type, default / implied / combined */
		builder.setBookLiquidity(liqidityTypeMap.getValue(inst.liquidityType()));

		/* structure of book */
		builder.setBookStructure(structTypeMap.getValue(inst.bookStructure()));

		/* book depth */
		if (!inst.maxBookDepth().isNull()) {
			builder.setBookDepth((int) inst.maxBookDepth().asDouble());
		}

		/* vendor */
		if (!inst.vendor().isNull()) {
			builder.setVendorId(inst.vendor().id());
		}

		/* market symbol; can be non unique; */
		if (inst.symbol() != null) {
			builder.setSymbol(inst.symbol());
		}

		if (inst.vendorSymbols().size() > 0) {
			for (final Map.Entry<VendorID, String> entry : inst.vendorSymbols().entrySet()) {
				builder.addSymbols(Symbol.newBuilder()
						.setVendor(entry.getKey().id())
						.setSymbol(entry.getValue())
						.build());
			}
		}

		/* market free style description; can be used in full text search */
		if (inst.description() != null) {
			builder.setDescription(inst.description());
		}

		/* stock vs future vs etc. */
		if (inst.CFICode() != null) {
			builder.setCfiCode(inst.CFICode());
		}

		/* market trading currency */
		if (inst.currencyCode() != null) {
			builder.setCurrencyCode(inst.currencyCode());
		}

		/* market originating exchange identifier */
		if (inst.exchangeCode() != null) {
			builder.setExchangeCode(inst.exchangeCode());
		}

		/* price step / increment size / tick size */
		if (!inst.tickSize().isNull()) {
			builder.setMinimumPriceIncrement(build(inst.tickSize()));
		}

		/* value of a future contract / stock share */
		if (!inst.pointValue().isNull()) {
			builder.setContractPointValue(build(inst.pointValue()));
		}

		if (!inst.schedule().isNull()) {

			final InstrumentDefinition.Schedule.Builder sb = InstrumentDefinition.Schedule.newBuilder();

			for (final TimeSpan span : inst.schedule()) {
				sb.addSessions(InstrumentDefinition.TimeSpan.newBuilder()
						.setTimeStart(span.start().getMillis())
						.setTimeFinish(span.stop().getMillis()).build());
			}

			builder.setSchedule(sb.build());

		}

		if (!inst.calendar().isNull()) {

			final InstrumentDefinition.Calendar.Builder cb = InstrumentDefinition.Calendar.newBuilder();

			for (final Event evt : inst.calendar().events()) {
				cb.addEvents(InstrumentDefinition.Event.newBuilder()
						.setType(eventTypeMap.getValue(evt.type()))
						.setDate(evt.date().getMillis()).build());
			}

			builder.setCalendar(cb.build());

		}

		if (inst.created() != null) {
			builder.setRecordCreateTime(inst.created().getMillis());
		}

		if (inst.updated() != null) {
			builder.setRecordUpdateTime(inst.updated().getMillis());
		}

		/* time zone name as text */
		if (inst.timeZone() != null) {
			builder.setTimeZoneName(inst.timeZone().getID());
		}

		for (final InstrumentID id : inst.components()) {
			builder.addComponentId(Long.parseLong(id.id()));
		}

		builder.setState(inst.state() == Instrument.State.ACTIVE
				? InstrumentDefinition.State.ACTIVE_STATE
				: InstrumentDefinition.State.PASSIVE_STATE);

		if (!inst.channel().isNull()) {
			builder.setChannel(inst.channel().id());
		}

		if (!inst.underlier().isNull()) {
			builder.addComponentId(Long.parseLong(inst.underlier().id()));
		}

		if (!inst.priceFormat().isNull()) {
			builder.setPriceFormat(InstrumentDefinition.PriceFormat.newBuilder()
					.setDenominator(inst.priceFormat().denominator())
					.setPrecision(inst.priceFormat().precision())
					.setIsFractional(inst.priceFormat().fractional())
					.setSubDenominator(inst.priceFormat().subDenominator())
					.setSubPrecision(inst.priceFormat().subPrecision())
					.setSubFormat(subFormatMap.getValue(inst.priceFormat().subFormat()))
					.build());
		}

		if (!inst.optionStrikePriceFormat().isNull()) {
			builder.setOptionStrikePriceFormat(InstrumentDefinition.PriceFormat.newBuilder()
					.setDenominator(inst.optionStrikePriceFormat().denominator())
					.setPrecision(inst.optionStrikePriceFormat().precision())
					.setIsFractional(inst.optionStrikePriceFormat().fractional())
					.setSubDenominator(inst.optionStrikePriceFormat().subDenominator())
					.setSubPrecision(inst.optionStrikePriceFormat().subPrecision())
					.setSubFormat(subFormatMap.getValue(inst.optionStrikePriceFormat().subFormat()))
					.build());
		}

		if (!inst.transactionPriceConversionFactor().isNull()) {
			builder.setTransactionPriceConversionFactor(
					(int) inst.transactionPriceConversionFactor().asDouble());
		}

		if (!inst.strikePrice().isNull()) {
			builder.setOptionStrike(build(inst.strikePrice()));
		}

		if (!inst.optionStyle().isNull()) {
			builder.setOptionStyle(optionStyleMap.getValue(inst.optionStyle()));
		}

		if (!inst.optionType().isNull()) {
			builder.setOptionType(optionTypeMap.getValue(inst.optionType()));
		}

		for (final SpreadLeg leg : inst.spreadLegs()) {
			builder.addSpreadLeg(InstrumentDefinition.SpreadLeg.newBuilder()
					.setMarketId(Long.parseLong(leg.instrument().id()))
					.setRatio(leg.ratio())
					.build());
		}

		if (!inst.spreadType().isNull()) {
			// TODO update spread mappings
			// builder.setSpreadType(spreadTypeMap.getValue(inst.spreadType()));
		}

		return builder.build();

	}

	private static class BiEnumMap<K extends Enum<K>, V extends Enum<V>> {

		private final Map<K, V> keyMap;
		private final Map<V, K> valMap;

		protected BiEnumMap(final Map<K, V> keyMap, final Map<V, K> valMap) {
			this.keyMap = keyMap;
			this.valMap = valMap;
		}

		protected BiEnumMap(final Class<K> keyType, final Class<V> valType) {
			this.keyMap = new EnumMap<K, V>(keyType);
			this.valMap = new EnumMap<V, K>(valType);
		}

		public static <K extends Enum<K>, V extends Enum<V>> BiEnumMap<K, V> create(final Class<K> keyType,
				final Class<V> valType) {
			return create(keyType, valType, 0);
		}

		public static <K extends Enum<K>, V extends Enum<V>> BiEnumMap<K, V> create(
				final Class<K> keyType, final Class<V> valType, final int offset) {
			return create(keyType, valType, keyType.getEnumConstants(), valType.getEnumConstants(), offset);
		}

		public static <K extends Enum<K>, V extends Enum<V>> BiEnumMap<K, V> create(final K[] keys, final V[] vals) {
			return create(keys, vals, 0);
		}

		@SuppressWarnings("unchecked")
		public static <K extends Enum<K>, V extends Enum<V>> BiEnumMap<K, V> create(final K[] keys, final V[] vals,
				final int offset) {

			if (keys.length != vals.length - offset) {
				throw new IndexOutOfBoundsException("Enum sets are not the same size");
			}

			if (keys.length == 0 || vals.length == 0) {
				return new EmptyBiEnumMap<K, V>();
			}

			return create((Class<K>) keys[0].getClass(),
					(Class<V>) vals[0].getClass(), keys, vals, offset);

		}

		private static <K extends Enum<K>, V extends Enum<V>> BiEnumMap<K, V> create(
				final Class<K> keyType, final Class<V> valType, final K[] keys, final V[] vals, final int offset) {

			if (keys.length != vals.length - offset) {
				throw new IndexOutOfBoundsException("Enum sets are not the same size");
			}

			final int start = offset < 0 ? -offset : 0;

			final BiEnumMap<K, V> map = new BiEnumMap<K, V>(keyType, valType);

			for (int i = start; i < keys.length; i++) {
				map.keyMap.put(keys[i], vals[i + offset]);
				map.valMap.put(vals[i + offset], keys[i]);
			}

			return map;

		}

		public V getValue(final K key) {
			return keyMap.get(key);
		}

		@SuppressWarnings("unused")
		public K getKey(final V val) {
			return valMap.get(val);
		}

		private static class EmptyBiEnumMap<K extends Enum<K>, V extends Enum<V>> extends BiEnumMap<K, V> {

			public EmptyBiEnumMap() {
				super(Collections.<K, V> emptyMap(), Collections.<V, K> emptyMap());
			}

		}

	}

	public static Decimal build(final long mantissa, final int exponent) {
		return Decimal.newBuilder()
				.setMantissa(mantissa)
				.setExponent(exponent)
				.build();
	}

	public static Decimal build(final Price price) {
		return build(price.mantissa(), price.exponent());
	}

}
