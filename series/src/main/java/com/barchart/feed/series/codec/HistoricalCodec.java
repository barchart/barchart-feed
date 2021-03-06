package com.barchart.feed.series.codec;

import java.nio.ByteBuffer;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openfeed.AggregationPeriod;
import org.openfeed.MarketEntry;
import org.openfeed.MarketHistoricalSnapshot;
import org.openfeed.util.datetime.ProtoDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.Bar;
import com.barchart.feed.api.series.Bar.GREEK_TYPE;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.series.BarImpl;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.ValueFactory;
import com.google.protobuf.InvalidProtocolBufferException;

public final class HistoricalCodec {

	private static final ValueFactory VALUES = ValueFactoryImpl.getInstance();
	private static final Logger logger = LoggerFactory
			.getLogger(HistoricalCodec.class);

	private HistoricalCodec() {
	}

	/**
	 * Create from protobuf message using UTC timezone.
	 *
	 * TODO return MarketHistoricalStateBar instead
	 */
	public static BarImpl fromProtobuf(final ByteBuffer buf) {
		return fromProtobuf(buf, DateTimeZone.UTC);
	}

	/**
	 * Create from protobuf message using the specified timezone.
	 *
	 * TODO return MarketHistoricalStateBar instead
	 */
	public static BarImpl fromProtobuf(final ByteBuffer buf,
			final DateTimeZone zone) {
		try {
			return fromProtobuf(
					MarketHistoricalSnapshot.parseFrom(buf.array()), zone);
		} catch (final InvalidProtocolBufferException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create from protobuf message using UTC timezone.
	 *
	 * TODO return MarketHistoricalStateBar instead
	 */
	public static BarImpl fromProtobuf(final MarketHistoricalSnapshot buf) {
		return fromProtobuf(buf, DateTimeZone.UTC);
	}

	/**
	 * Create from protobuf message using the specified timezone.
	 *
	 * TODO return MarketHistoricalStateBar instead
	 */
	public static BarImpl fromProtobuf(final MarketHistoricalSnapshot buf,
			final DateTimeZone zone) {

		final BarImpl bar = new BarImpl(buf.hasBaseMarketId() ? new InstrumentID(
				String.valueOf(buf.getBaseMarketId())) : null,
				buf.hasBaseTimeStamp() ? ProtoDateUtil.fromDecimalDateTimeToJoda(buf
						.getBaseTimeStamp(), zone) : null,
						buf.hasAggregation() ? new Period(PeriodType.valueOf(buf.getAggregation().name()), buf
								.getPeriodCount()) : null);

		final List<MarketEntry> entryList = buf.getEntryList();
		for (final MarketEntry entry : entryList) {
			if (entry.hasType()) {
				final Price p = entry.hasPriceMantissa() ? VALUES
						.newPrice(entry.getPriceMantissa(), entry.getPriceExponent()) : Price.NULL;
				final Size s = entry.hasSizeMantissa() ? VALUES
						.newSize(entry.getSizeMantissa(), entry.getSizeExponent()) : Size.NULL;
				switch (entry.getType()) {
					case ASK:
						bar.setAsk(p);
						bar.setAskSize(s);
						break;
					case BID:
						bar.setBid(p);
						bar.setBidSize(s);
						break;
					case CLOSE:
						bar.setClose(p);
						bar.setLastSize(s);
						final DateTime d = entry.hasTimeStamp() ? new DateTime(entry.getTimeStamp(), zone) : null;
						bar.setLastTradeDay(d);
						break;
					case HIGH:
						bar.setHigh(p);
						break;
					case LOW:
						bar.setLow(p);
						break;
					case SETTLE:
						bar.setSettlement(p);
						break;
					case INTEREST:
						bar.setOpenInterest(s);
						break;
					case MIDPOINT:
						bar.setMidpoint(p);
						break;
					case OPEN:
						bar.setOpen(p);
						break;
					case TRADE:
						bar.setTradeCount(s);
						break;
					case TRADED_VALUE:
						bar.setTradedValue(p);
						break;
					case TRADED_VALUE_DOWN:
						bar.setTradedValueDown(p);
						break;
					case TRADED_VALUE_UP:
						bar.setTradedValueUp(p);
						break;
					case VOLUME:
						bar.setVolume(s);
						break;
					case VOLUME_DOWN:
						bar.setVolumeDown(s);
						break;
					case VOLUME_UP:
						bar.setVolumeUp(s);
						break;
					case UNDERLYING:
						bar.setUnderlyingPrice(p);
						break;
					case COMPUTED:
						switch (entry.getDescriptor(0)) {
							case OPTION_DELTA:
								bar.setGreeks(GREEK_TYPE.DELTA, p);
								break;
							case OPTION_GAMMA:
								bar.setGreeks(GREEK_TYPE.GAMMA, p);
								break;
							case OPTION_THETA:
								bar.setGreeks(GREEK_TYPE.THETA, p);
								break;
							case OPTION_VEGA:
								bar.setGreeks(GREEK_TYPE.VEGA, p);
								break;
							case OPTION_RHO:
								bar.setGreeks(GREEK_TYPE.RHO, p);
								break;
							case OPTION_HISTORICAL_TWENTY_DAY_VOLATILITY:
								bar.setHistoricalVolatility(p);
								break;
							case OPTION_THEORETICAL_PRICE:
								bar.setTheoreticalPrice(p);
								break;
							case OPTION_IMPLIED_VOLATILITY:
								bar.setImpliedVolatility(p);
								break;
							case UL_CALLS_VOLUME:
								bar.setCallsVolume(s);
								break;
							case UL_PUTS_VOLUME:
								bar.setPutsVolume(s);
								break;
							case UL_CALLS_OPEN_INTERESTS:
								bar.setCallsOpenInterest(s);
								break;
							case UL_PUTS_OPEN_INTERESTS:
								bar.setPutsOpenInterest(s);
								break;
							default:

						}
					default:
						logger.trace("Unsupported entry type: ", entry.getType());
						break;
				}
			}
		}

		return bar;
	}

	/**
	 * Return a protobuf EOD record for this Bar. If <i>concise</i> is true, the
	 * instrument, period and timestamp will not be included and will be assumed
	 * to be tracked by the context wrapping this record.
	 *
	 * @param concise
	 *            True Omit instrument, timestamp and period fields
	 * @return
	 */
	public static MarketHistoricalSnapshot toProtobuf(final Bar bar,
			final boolean concise) {

		// TODO if MarketHistoricalStateBar, just return internal protobuf

		final MarketHistoricalSnapshot.Builder builder = MarketHistoricalSnapshot
				.newBuilder();

		if (!concise) {
			// base time stamp is always UTC format
			builder.setBaseMarketId(Long.parseLong(bar.getInstrument().id()))
			.setBaseTimeStamp(ProtoDateUtil.fromJodaDateTimeToDecimalDateTime(
					new DateTime(bar.getDate(), DateTimeZone.UTC)))
					.setAggregation(
							AggregationPeriod.valueOf(bar.getPeriod()
									.getPeriodType().name()))
									.setPeriodCount(bar.getPeriod().size());
		}

		if (bar.getOpen() != null && !bar.getOpen().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setPriceMantissa(bar.getOpen().mantissa())
					.setPriceExponent(bar.getOpen().exponent())
					.setType(MarketEntry.Type.OPEN));
		}
		if (bar.getHigh() != null && !bar.getHigh().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setPriceMantissa(bar.getHigh().mantissa())
					.setPriceExponent(bar.getHigh().exponent())
					.setType(MarketEntry.Type.HIGH));
		}
		if (bar.getUnderlying() != null && !bar.getUnderlying().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setPriceMantissa(bar.getUnderlying().mantissa())
					.setPriceExponent(bar.getUnderlying().exponent())
					.setType(MarketEntry.Type.UNDERLYING));
		}
		if (bar.getLow() != null && !bar.getLow().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setPriceMantissa(bar.getLow().mantissa())
					.setPriceExponent(bar.getLow().exponent())
					.setType(MarketEntry.Type.LOW));
		}
		if (bar.getSettlement() != null && !bar.getSettlement().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setPriceMantissa(bar.getSettlement().mantissa())
					.setPriceExponent(bar.getSettlement().exponent())
					.setType(MarketEntry.Type.SETTLE));
		}
		if (bar.getClose() != null && !bar.getClose().isNull()) {
			final MarketEntry.Builder entryBuilder = MarketEntry.newBuilder()
					.setPriceMantissa(bar.getClose().mantissa())
					.setPriceExponent(bar.getClose().exponent());
			if (bar.getLastSize() != null && !bar.getLastSize().isNull()) {
				entryBuilder.setSizeMantissa(bar.getLastSize().mantissa())
				.setSizeExponent(bar.getLastSize().exponent());
			}
			if (bar.getLastTradeDay() != null) {
				entryBuilder.setTimeStamp(bar.getLastTradeDay().getMillis());
			}
			builder.addEntry(entryBuilder.setType(MarketEntry.Type.CLOSE));
		}
		if (bar.getVolume() != null && !bar.getVolume().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setSizeMantissa(bar.getVolume().mantissa())
					.setSizeExponent(bar.getVolume().exponent())
					.setType(MarketEntry.Type.VOLUME));
		}
		if (bar.getVolumeUp() != null && !bar.getVolumeUp().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setSizeMantissa(bar.getVolumeUp().mantissa())
					.setSizeExponent(bar.getVolumeUp().exponent())
					.setType(MarketEntry.Type.VOLUME_UP));
		}
		if (bar.getVolumeDown() != null && !bar.getVolumeDown().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setSizeMantissa(bar.getVolumeDown().mantissa())
					.setSizeExponent(bar.getVolumeDown().exponent())
					.setType(MarketEntry.Type.VOLUME_DOWN));
		}
		if (bar.getTradeCount() != null && !bar.getTradeCount().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setSizeMantissa(bar.getTradeCount().mantissa())
					.setSizeExponent(bar.getTradeCount().exponent())
					.setType(MarketEntry.Type.TRADE));
		}
		if (bar.getOpenInterest() != null && !bar.getOpenInterest().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setSizeMantissa(bar.getOpenInterest().mantissa())
					.setSizeExponent(bar.getOpenInterest().exponent())
					.setType(MarketEntry.Type.INTEREST));
		}
		if (bar.getMidpoint() != null && !bar.getMidpoint().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setPriceMantissa(bar.getMidpoint().mantissa())
					.setPriceExponent(bar.getMidpoint().exponent())
					.setType(MarketEntry.Type.MIDPOINT));
		}
		if (bar.getBid() != null && !bar.getBid().isNull()) {
			final MarketEntry.Builder entryBuilder = MarketEntry.newBuilder()
					.setPriceMantissa(bar.getBid().mantissa())
					.setPriceExponent(bar.getBid().exponent());
			if (bar.getBidSize() != null && !bar.getBidSize().isNull()) {
				entryBuilder.setSizeMantissa(bar.getBidSize().mantissa())
				.setSizeExponent(bar.getBidSize().exponent());
			}
			builder.addEntry(entryBuilder.setType(MarketEntry.Type.BID));
		}
		if (bar.getAsk() != null && !bar.getAsk().isNull()) {
			final MarketEntry.Builder entryBuilder = MarketEntry.newBuilder()
					.setPriceMantissa(bar.getAsk().mantissa())
					.setPriceExponent(bar.getAsk().exponent());
			if (bar.getAskSize() != null && !bar.getAskSize().isNull()) {
				entryBuilder.setSizeMantissa(bar.getAskSize().mantissa())
				.setSizeExponent(bar.getAskSize().exponent());
			}
			builder.addEntry(entryBuilder.setType(MarketEntry.Type.ASK));
		}
		if (bar.getTradedValue() != null && !bar.getTradedValue().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setPriceMantissa(bar.getTradedValue().mantissa())
					.setPriceExponent(bar.getTradedValue().exponent())
					.setType(MarketEntry.Type.TRADED_VALUE));
		}
		if (bar.getTradedValueUp() != null && !bar.getTradedValueUp().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setPriceMantissa(bar.getTradedValueUp().mantissa())
					.setPriceExponent(bar.getTradedValueUp().exponent())
					.setType(MarketEntry.Type.TRADED_VALUE_UP));
		}
		if (bar.getTradedValueDown() != null
				&& !bar.getTradedValueDown().isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setPriceMantissa(bar.getTradedValueDown().mantissa())
					.setPriceExponent(bar.getTradedValueDown().exponent())
					.setType(MarketEntry.Type.TRADED_VALUE_DOWN));
		}

		options(bar, builder);

		return builder.build();

	}

	private static void options(Bar bar, MarketHistoricalSnapshot.Builder builder) {

		entry(bar.getGreeks(GREEK_TYPE.DELTA), builder, MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.OPTION_DELTA);
		entry(bar.getGreeks(GREEK_TYPE.GAMMA), builder, MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.OPTION_GAMMA);
		entry(bar.getGreeks(GREEK_TYPE.THETA), builder, MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.OPTION_THETA);
		entry(bar.getGreeks(GREEK_TYPE.VEGA), builder, MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.OPTION_VEGA);
		entry(bar.getGreeks(GREEK_TYPE.RHO), builder, MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.OPTION_RHO);

		entry(bar.getHistoricalVolatility(), builder, MarketEntry.Type.COMPUTED,
				MarketEntry.Descriptor.OPTION_HISTORICAL_TWENTY_DAY_VOLATILITY);
		entry(bar.getImpliedVolatility(), builder, MarketEntry.Type.COMPUTED,
				MarketEntry.Descriptor.OPTION_IMPLIED_VOLATILITY);
		entry(bar.getTheoreticalPrice(), builder, MarketEntry.Type.COMPUTED,
				MarketEntry.Descriptor.OPTION_THEORETICAL_PRICE);

		entry(bar.getCallsVolume(), builder, MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.UL_CALLS_VOLUME);
		entry(bar.getCallsOpenInterest(), builder, MarketEntry.Type.COMPUTED,
				MarketEntry.Descriptor.UL_CALLS_OPEN_INTERESTS);
		entry(bar.getPutsVolume(), builder, MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.UL_PUTS_VOLUME);
		entry(bar.getPutsOpenInterest(), builder, MarketEntry.Type.COMPUTED,
				MarketEntry.Descriptor.UL_PUTS_OPEN_INTERESTS);

	}

	private static void entry(Price p, MarketHistoricalSnapshot.Builder builder, MarketEntry.Type t, MarketEntry.Descriptor d) {
		if (p != null && !p.isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setPriceMantissa(p.mantissa())
					.setPriceExponent(p.exponent())
					.setType(t).addDescriptor(d));
		}
	}

	private static void entry(Size s, MarketHistoricalSnapshot.Builder builder, MarketEntry.Type t,
			MarketEntry.Descriptor d) {
		if (s != null && !s.isNull()) {
			builder.addEntry(MarketEntry.newBuilder()
					.setSizeMantissa(s.mantissa())
					.setSizeExponent(s.exponent())
					.setType(t).addDescriptor(d));
		}
	}
}
