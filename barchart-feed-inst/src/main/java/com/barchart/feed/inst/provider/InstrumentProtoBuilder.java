package com.barchart.feed.inst.provider;

import static com.barchart.feed.inst.api.InstrumentField.BOOK_DEPTH;
import static com.barchart.feed.inst.api.InstrumentField.CFI_CODE;
import static com.barchart.feed.inst.api.InstrumentField.CURRENCY;
import static com.barchart.feed.inst.api.InstrumentField.DESCRIPTION;
import static com.barchart.feed.inst.api.InstrumentField.DISPLAY_BASE;
import static com.barchart.feed.inst.api.InstrumentField.DISPLAY_EXPONENT;
import static com.barchart.feed.inst.api.InstrumentField.EXCHANGE_ID;
import static com.barchart.feed.inst.api.InstrumentField.LIFETIME;
import static com.barchart.feed.inst.api.InstrumentField.MARKET_HOURS;
import static com.barchart.feed.inst.api.InstrumentField.POINT_VALUE;
import static com.barchart.feed.inst.api.InstrumentField.PRICE_STEP;
import static com.barchart.feed.inst.api.InstrumentField.VENDOR;
import static com.barchart.feed.inst.api.InstrumentField.VENDOR_SYMBOL;
import static com.barchart.feed.inst.api.InstrumentField.TIME_ZONE_OFFSET;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.TimeInterval;
import com.barchart.proto.buf.inst.Calendar;
import com.barchart.proto.buf.inst.InstrumentDefinition;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.util.values.api.PriceValue;

public final class InstrumentProtoBuilder {
	
	private InstrumentProtoBuilder() {
		
	}
	
	public static InstrumentDefinition build(final Instrument inst) {
		
		if(inst == null || inst.equals(Instrument.NULL_INSTRUMENT)) {
			return null; // Return empty instrument def
		}
		
		final InstrumentDefinition.Builder builder = 
				InstrumentDefinition.newBuilder();
		
		/* market identifier; must be globally unique; */
		builder.setMarketId(inst.getGUID().getGUID());
		
		/* vendor */
		if(inst.contains(VENDOR)) {
			builder.setVendorId(inst.get(VENDOR).toString());
		}
		
		/* market symbol; can be non unique; */
		if(inst.contains(VENDOR_SYMBOL)) {
			builder.setSymbol(inst.get(VENDOR_SYMBOL).toString());
		}
		/* market free style description; can be used in full text search */
		if(inst.contains(DESCRIPTION)) {
			builder.setDescription(inst.get(DESCRIPTION).toString());
		}
		
		/* market originating exchange identifier */
		if(inst.contains(EXCHANGE_ID)) {
			builder.setExchangeCode(inst.get(EXCHANGE_ID).toString());
		}
		
		/* book depth */
		if(inst.contains(BOOK_DEPTH)) {
			builder.setBookDepth((int)inst.get(BOOK_DEPTH).asLong());
		}
		
		/* stock vs future vs etc. */
		if(inst.contains(CFI_CODE)) {
			builder.setCfiCode(inst.get(CFI_CODE).getCode());
		}
		
		/* price currency */
		if(inst.contains(CURRENCY)) {
			builder.setCurrencyCode(inst.get(CURRENCY).name());
		}
		
		/* price step / increment size / tick size */
		if(inst.contains(PRICE_STEP)) {
			final PriceValue step = inst.get(PRICE_STEP);
			step.norm();
			builder.setMinimumPriceIncrement(DecimalBuilder.build(step));
		}
		
		/* value of a future contract / stock share */
		if(inst.contains(POINT_VALUE)) {
			final PriceValue val = inst.get(POINT_VALUE);
			val.norm();
			builder.setContractPointValue(DecimalBuilder.build(val));
		}
		
		/* display fraction base : decimal(10) vs binary(2), etc. */
		if(inst.contains(DISPLAY_BASE)) {
			builder.setDisplayDenominatorBase((int)inst.get(DISPLAY_BASE).asLong());
		}
		
		/* display fraction exponent */
		if(inst.contains(DISPLAY_EXPONENT)) {
			builder.setDisplayDenominatorExponent((int)inst.get(DISPLAY_EXPONENT).asLong());
		}
		
		/* Calendar */
		if(inst.contains(LIFETIME) && inst.contains(MARKET_HOURS)) {
			final Calendar.Builder calBuilder = Calendar.newBuilder();
			final Interval.Builder intBuilder = Interval.newBuilder();
			intBuilder.setTimeStart(inst.get(LIFETIME).getBegin());
			intBuilder.setTimeFinish(inst.get(LIFETIME).getEnd());
			
			/* lifetime of instrument */
			calBuilder.setLifeTime(intBuilder.build());
			
			intBuilder.clear();
			for(final TimeInterval ti : inst.get(MARKET_HOURS)) {
				intBuilder.setTimeStart(ti.getBegin());
				intBuilder.setTimeFinish(ti.getEnd());
				calBuilder.addMarketHours(intBuilder.build());
				intBuilder.clear();
			}
			
			/* array of intervals of market hours in a normal week, denoted in minutes from Sunday morning */
			builder.setCalendar(calBuilder.build());
		}
		
		/* timezone represented as offset in millis from utc */
		if(inst.contains(TIME_ZONE_OFFSET)) {
			builder.setTimeZoneOffset((int)inst.get(TIME_ZONE_OFFSET).asLong());
		}
		
		return builder.build();
	}

}
