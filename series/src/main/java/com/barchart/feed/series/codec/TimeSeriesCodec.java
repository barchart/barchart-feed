package com.barchart.feed.series.codec;

import java.nio.ByteBuffer;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.Bar;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.proto.buf.TimeSeriesRecord;
import com.barchart.feed.series.BarImpl;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.ValueFactory;
import com.google.protobuf.InvalidProtocolBufferException;

public final class TimeSeriesCodec {

	private static final ValueFactory VALUES = ValueFactoryImpl.getInstance();

	private TimeSeriesCodec() {
	}

	/**
	 * Create from protobuf message.
	 */
	public static BarImpl fromProtobuf(final ByteBuffer buf) {
		try {
			return fromProtobuf(TimeSeriesRecord.parseFrom(buf.array()));
		} catch (final InvalidProtocolBufferException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create from protobuf message.
	 */
	public static BarImpl fromProtobuf(final TimeSeriesRecord buf) {

		return new BarImpl(
				buf.hasInstrument() ? new InstrumentID(String.valueOf(buf.getInstrument())) : null,
				buf.hasTimestamp() ? new DateTime(buf.getTimestamp()) : null,
				buf.hasPeriod() ? new Period(PeriodType.valueOf(buf.getPeriod().name()), buf.getPeriodCount()) : null,
				buf.hasOpenMantissa() ? VALUES.newPrice(buf.getOpenMantissa(), buf.getOpenExponent()) : null,
				buf.hasHighMantissa() ? VALUES.newPrice(buf.getHighMantissa(), buf.getHighExponent()) : null,
				buf.hasLowMantissa() ? VALUES.newPrice(buf.getLowMantissa(), buf.getLowExponent()) : null,
				buf.hasCloseMantissa() ? VALUES.newPrice(buf.getCloseMantissa(), buf.getCloseExponent()) : null,
				buf.hasVolumeMantissa() ? VALUES.newSize(buf.getVolumeMantissa(), buf.getVolumeExponent()) : null,
				buf.hasVolumeUpMantissa() ? VALUES.newSize(buf.getVolumeUpMantissa(), buf.getVolumeExponent()) : null,
				buf.hasVolumeDownMantissa() ? VALUES.newSize(buf.getVolumeDownMantissa(), buf.getVolumeExponent())
						: null,
				buf.hasTickCount() ? VALUES.newSize(buf.getTickCount()) : null,
				buf.hasOpenInterestMantissa() ? VALUES.newSize(buf.getOpenInterestMantissa(),
						buf.getOpenInterestExponent()) : null,
				buf.hasMidpointMantissa() ? VALUES.newPrice(buf.getMidpointMantissa(), buf.getMidpointExponent())
						: null,
				buf.hasBidMantissa() ? VALUES.newPrice(buf.getBidMantissa(), buf.getBidExponent()) : null,
				buf.hasBidSizeMantissa() ? VALUES.newSize(buf.getBidSizeMantissa(), buf.getBidSizeExponent()) : null,
				buf.hasAskMantissa() ? VALUES.newPrice(buf.getAskMantissa(), buf.getAskExponent()) : null,
				buf.hasAskSizeMantissa() ? VALUES.newSize(buf.getAskSizeMantissa(), buf.getAskSizeExponent()) : null,
				buf.hasTradedValueMantissa() ? VALUES.newPrice(buf.getTradedValueMantissa(),
						buf.getTradedValueExponent()) : null,
				buf.hasTradedValueUpMantissa() ? VALUES.newPrice(buf.getTradedValueUpMantissa(),
						buf.getTradedValueUpExponent()) : null,
				buf.hasTradedValueDownMantissa() ? VALUES.newPrice(buf.getTradedValueDownMantissa(),
						buf.getTradedValueDownExponent()) : null);

	}

	/**
	 * Return a protobuf EOD record for this Bar. If <i>concise</i> is true, the
	 * instrument, period and timestamp will not be included and will be assumed
	 * to be tracked by the context wrapping this record.
	 *
	 * @param concise True Omit instrument, timestamp and period fields
	 * @return
	 */
	public static TimeSeriesRecord toProtobuf(final Bar bar, final boolean concise) {

		final TimeSeriesRecord.Builder builder = TimeSeriesRecord.newBuilder();

		if (!concise) {
			builder.setInstrument(Long.parseLong(bar.getInstrument().id()))
					.setTimestamp(bar.getTime().millisecond())
					.setPeriod(TimeSeriesRecord.Period.valueOf(bar.getPeriod().getPeriodType().name()))
					.setPeriodCount(bar.getPeriod().size());
		}

		if (bar.getOpen() != null)
			builder.setOpenMantissa(bar.getOpen().mantissa()).setOpenExponent(bar.getOpen().exponent());
		if (bar.getHigh() != null)
			builder.setHighMantissa(bar.getHigh().mantissa()).setHighExponent(bar.getHigh().exponent());
		if (bar.getLow() != null)
			builder.setLowMantissa(bar.getLow().mantissa()).setLowExponent(bar.getLow().exponent());
		if (bar.getClose() != null)
			builder.setCloseMantissa(bar.getClose().mantissa()).setCloseExponent(bar.getClose().exponent());
		if (bar.getVolume() != null)
			builder.setVolumeMantissa(bar.getVolume().mantissa()).setVolumeExponent(bar.getVolume().exponent());
		if (bar.getVolumeUp() != null)
			builder.setVolumeUpMantissa(bar.getVolumeUp().mantissa()).setVolumeUpExponent(bar.getVolumeUp().exponent());
		if (bar.getVolumeDown() != null)
			builder.setVolumeDownMantissa(bar.getVolumeDown().mantissa()).setVolumeDownExponent(
					bar.getVolumeDown().exponent());
		if (bar.getTickCount() != null)
			builder.setTickCount((int) bar.getTickCount().asDouble());
		if (bar.getOpenInterest() != null)
			builder.setOpenInterestMantissa(bar.getOpenInterest().mantissa()).setOpenInterestExponent(
					bar.getOpenInterest().exponent());
		if (bar.getMidpoint() != null)
			builder.setMidpointMantissa(bar.getMidpoint().mantissa()).setMidpointExponent(bar.getMidpoint().exponent());
		if (bar.getBid() != null)
			builder.setBidMantissa(bar.getBid().mantissa()).setBidExponent(bar.getBid().exponent());
		if (bar.getBidSize() != null)
			builder.setBidSizeMantissa(bar.getBidSize().mantissa()).setBidSizeExponent(bar.getBidSize().exponent());
		if (bar.getAsk() != null)
			builder.setAskMantissa(bar.getAsk().mantissa()).setAskExponent(bar.getAsk().exponent());
		if (bar.getAskSize() != null)
			builder.setAskSizeMantissa(bar.getAskSize().mantissa()).setAskSizeExponent(bar.getAskSize().exponent());
		if (bar.getTradedValue() != null)
			builder.setTradedValueMantissa(bar.getTradedValue().mantissa()).setTradedValueExponent(
					bar.getTradedValue().exponent());
		if (bar.getTradedValueUp() != null)
			builder.setTradedValueUpMantissa(bar.getTradedValueUp().mantissa()).setTradedValueUpExponent(
					bar.getTradedValueUp().exponent());
		if (bar.getTradedValueDown() != null)
			builder.setTradedValueDownMantissa(bar.getTradedValueDown().mantissa()).setTradedValueDownExponent(
					bar.getTradedValueDown().exponent());

		return builder.build();

	}

}
