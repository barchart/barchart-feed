/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.meta.instrument;

import java.util.Map;

import org.openfeed.InstrumentDefinition;
import org.openfeed.InstrumentDefinition.BookLiquidity;
import org.openfeed.InstrumentDefinition.BookStructure;
import org.openfeed.InstrumentDefinition.Decimal;
import org.openfeed.InstrumentDefinition.EventType;
import org.openfeed.InstrumentDefinition.InstrumentType;
import org.openfeed.InstrumentDefinition.Symbol;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.Instrument.State;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.model.meta.instrument.Event;
import com.barchart.feed.api.model.meta.instrument.PriceFormat;
import com.barchart.feed.api.model.meta.instrument.SpreadLeg;
import com.barchart.feed.api.model.meta.instrument.SpreadType;
import com.barchart.feed.api.model.meta.instrument.TimeSpan;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public final class InstrumentProtoBuilder {

	private static final BiEnumMap<Instrument.SecurityType, InstrumentType> secTypeMap =
			new BiEnumMap<Instrument.SecurityType, InstrumentType>(
					new Instrument.SecurityType[] {
							Instrument.SecurityType.NULL_TYPE, Instrument.SecurityType.FOREX,
							Instrument.SecurityType.INDEX, Instrument.SecurityType.EQUITY,
							Instrument.SecurityType.FUTURE, Instrument.SecurityType.OPTION
					}, new InstrumentType[] {
							InstrumentType.NO_INSTRUMENT, InstrumentType.FOREX_INSTRUMENT,
							InstrumentType.INDEX_INSTRUMENT, InstrumentType.EQUITY_INSTRUMENT,
							InstrumentType.FUTURE_INSTRUMENT, InstrumentType.OPTION_INSTRUMENT,
							InstrumentType.SPREAD_INSTRUMENT
					});

	private static final BiEnumMap<Instrument.BookLiquidityType, BookLiquidity> liqidityTypeMap =
			new BiEnumMap<Instrument.BookLiquidityType, BookLiquidity>(
					new Instrument.BookLiquidityType[] {
							Instrument.BookLiquidityType.NONE,
							Instrument.BookLiquidityType.DEFAULT, Instrument.BookLiquidityType.IMPLIED,
							Instrument.BookLiquidityType.COMBINED
					}, new BookLiquidity[] {
							BookLiquidity.NO_BOOK_LIQUIDITY,
							BookLiquidity.DEFAULT_LIQUIDITY,
							BookLiquidity.IMPLIED_LIQUIDITY,
							BookLiquidity.COMBINED_LIQUIDITY
					});

	private static final BiEnumMap<Instrument.BookStructureType, BookStructure> structTypeMap =
			new BiEnumMap<Instrument.BookStructureType, BookStructure>(
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
			new BiEnumMap<Event.Type, EventType>(
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
			new BiEnumMap<PriceFormat.SubFormat, InstrumentDefinition.PriceFormat.SubFormat>(
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
			new BiEnumMap<Instrument.OptionStyle, InstrumentDefinition.OptionStyle>(
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
			new BiEnumMap<Instrument.OptionType, InstrumentDefinition.OptionType>(
					new Instrument.OptionType[] {
							Instrument.OptionType.CALL,
							Instrument.OptionType.PUT
					}, new InstrumentDefinition.OptionType[] {
							InstrumentDefinition.OptionType.CALL_OPTION,
							InstrumentDefinition.OptionType.PUT_OPTION
					});

	private static final BiEnumMap<SpreadType, InstrumentDefinition.SpreadType> spreadTypeMap =
			new BiEnumMap<SpreadType, InstrumentDefinition.SpreadType>(
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
			return null; // Return empty instrument def
		}

		final InstrumentDefinition.Builder builder = InstrumentDefinition
				.newBuilder();

		/* market identifier; must be globally unique; */
		builder.setMarketId(Long.parseLong(inst.id().id()));

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
		builder.setVendorId(inst.vendor().id());

		/* market symbol; can be non unique; */
		builder.setSymbol(inst.symbol());

		if (inst.vendorSymbols().size() > 0) {
			for (final Map.Entry<VendorID, String> entry : inst.vendorSymbols().entrySet()) {
				builder.addSymbols(Symbol.newBuilder()
						.setVendor(entry.getKey().id())
						.setSymbol(entry.getValue())
						.build());
			}
		}

		/* market free style description; can be used in full text search */
		builder.setDescription(inst.description());

		/* stock vs future vs etc. */
		builder.setCfiCode(inst.CFICode());

		/* market trading currency */
		builder.setCurrencyCode(inst.currencyCode());

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

		builder.setRecordCreateTime(inst.created().getMillis());

		builder.setRecordUpdateTime(inst.updated().getMillis());

		/* time zone name as text */
		builder.setTimeZoneName(inst.timeZone().getID());

		for (final InstrumentID id : inst.components()) {
			builder.addComponentId(Long.parseLong(id.id()));
		}

		builder.setState(inst.state() == State.ACTIVE
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

		builder.setTransactionPriceConversionFactor(
				(int) inst.transactionPriceConversionFactor().asDouble());

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
			builder.setSpreadType(spreadTypeMap.getValue(inst.spreadType()));
		}

		return builder.build();

	}

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
		return Decimal.newBuilder()
				.setMantissa(mantissa)
				.setExponent(exponent)
				.build();
	}

	public static Decimal build(final Price price) {
		return build(price.mantissa(), price.exponent());
	}

}
