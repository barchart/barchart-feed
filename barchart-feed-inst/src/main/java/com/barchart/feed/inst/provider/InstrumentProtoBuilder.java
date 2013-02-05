package com.barchart.feed.inst.provider;

import static com.barchart.feed.inst.api.InstrumentField.*;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.proto.buf.inst.Calendar;
import com.barchart.proto.buf.inst.InstrumentDefinition;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.TimeInterval;

public final class InstrumentProtoBuilder {

	private InstrumentProtoBuilder() {

	}

	public static InstrumentDefinition build(final Instrument inst) {

		if (inst == null || inst.equals(Instrument.NULL_INSTRUMENT)) {
			return null; // Return empty instrument def
		}

		final InstrumentDefinition.Builder builder = InstrumentDefinition
				.newBuilder();

		/* market identifier; must be globally unique; */
		builder.setMarketId(inst.getGUID().getGUID());

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

		/* market originating exchange identifier */
		if (inst.contains(EXCHANGE_CODE)) {
			builder.setExchangeCode(inst.get(EXCHANGE_CODE).toString());
		}

		/* book depth */
		if (inst.contains(BOOK_DEPTH)) {
			builder.setBookDepth((int) inst.get(BOOK_DEPTH).asLong());
		}

		/* stock vs future vs etc. */
		if (inst.contains(CFI_CODE)) {
			builder.setCfiCode(inst.get(CFI_CODE).getCode());
		}

		/* price currency */
		if (inst.contains(CURRENCY)) {
			builder.setCurrencyCode(inst.get(CURRENCY).name());
		}

		/* price step / increment size / tick size */
		if (inst.contains(PRICE_STEP)) {
			final PriceValue step = inst.get(PRICE_STEP);
			step.norm();
			builder.setMinimumPriceIncrement(DecimalBuilder.build(step));
		}

		/* value of a future contract / stock share */
		if (inst.contains(POINT_VALUE)) {
			final PriceValue val = inst.get(POINT_VALUE);
			val.norm();
			builder.setContractPointValue(DecimalBuilder.build(val));
		}

		/* display fraction base : decimal(10) vs binary(2), etc. */
		if (inst.contains(DISPLAY_BASE)) {
			builder.setDisplayDenominatorBase((int) inst.get(DISPLAY_BASE)
					.asLong());
		}

		/* display fraction exponent */
		if (inst.contains(DISPLAY_EXPONENT)) {
			builder.setDisplayDenominatorExponent((int) inst.get(
					DISPLAY_EXPONENT).asLong());
		}

		/* Calendar */
		if (inst.contains(LIFETIME) && inst.contains(MARKET_HOURS)) {
			final Calendar.Builder calBuilder = Calendar.newBuilder();
			final Interval.Builder intBuilder = Interval.newBuilder();
			intBuilder.setTimeStart(inst.get(LIFETIME).startAsMillis());
			intBuilder.setTimeFinish(inst.get(LIFETIME).stopAsMillis());

			/* lifetime of instrument */
			calBuilder.setLifeTime(intBuilder.build());

			intBuilder.clear();
			for (final TimeInterval ti : inst.get(MARKET_HOURS)) {
				intBuilder.setTimeStart(ti.startAsMillis());
				intBuilder.setTimeFinish(ti.stopAsMillis());
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
		if (inst.contains(TIME_ZONE)) {
			builder.setTimeZoneOffset((int) inst.get(TIME_ZONE).asLong());
		}

		return builder.build();
	}

}
