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

import java.util.EnumMap;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.TimeInterval;
import com.barchart.feed.inst.enums.MarketDisplay.Fraction;
import com.barchart.proto.buf.inst.Calendar;
import com.barchart.proto.buf.inst.InstrumentDefinition;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.proto.buf.inst.PriceFraction;
import com.barchart.util.values.api.PriceValue;

public final class InstrumentProtoBuilder {
	
	private static final EnumMap<Fraction, PriceFraction> fracTypeMap =
			new EnumMap<Fraction, PriceFraction>(Fraction.class);
	
	static {
		fracTypeMap.put(Fraction.DEC_Z00, PriceFraction.FactionDecimal_Z00);
		fracTypeMap.put(Fraction.DEC_N01, PriceFraction.FactionDecimal_N01);
		fracTypeMap.put(Fraction.DEC_N02, PriceFraction.FactionDecimal_N02);
		fracTypeMap.put(Fraction.DEC_N03, PriceFraction.FactionDecimal_N03);
		fracTypeMap.put(Fraction.DEC_N04, PriceFraction.FactionDecimal_N04);
		fracTypeMap.put(Fraction.DEC_N05, PriceFraction.FactionDecimal_N05);
		fracTypeMap.put(Fraction.DEC_N06, PriceFraction.FactionDecimal_N06);
		fracTypeMap.put(Fraction.DEC_N07, PriceFraction.FactionDecimal_N07);
		fracTypeMap.put(Fraction.DEC_N08, PriceFraction.FactionDecimal_N08);
		fracTypeMap.put(Fraction.DEC_N09, PriceFraction.FactionDecimal_N09);
		
		fracTypeMap.put(Fraction.BIN_Z00, PriceFraction.FactionBinary_Z00);
		fracTypeMap.put(Fraction.BIN_N01, PriceFraction.FactionBinary_N01);
		fracTypeMap.put(Fraction.BIN_N02, PriceFraction.FactionBinary_N02);
		fracTypeMap.put(Fraction.BIN_N03, PriceFraction.FactionBinary_N03);
		fracTypeMap.put(Fraction.BIN_N04, PriceFraction.FactionBinary_N04);
		fracTypeMap.put(Fraction.BIN_N05, PriceFraction.FactionBinary_N05);
		fracTypeMap.put(Fraction.BIN_N06, PriceFraction.FactionBinary_N06);
		fracTypeMap.put(Fraction.BIN_N07, PriceFraction.FactionBinary_N07);
		fracTypeMap.put(Fraction.BIN_N08, PriceFraction.FactionBinary_N08);
		fracTypeMap.put(Fraction.BIN_N09, PriceFraction.FactionBinary_N09);
	}
	
	private InstrumentProtoBuilder() {
		
	}
	
	public static InstrumentDefinition build(final Instrument inst) {
		
		if(inst == null || inst.equals(Instrument.NULL_INSTRUMENT)) {
			return null; // Return empty instrument def
		}
		
		final InstrumentDefinition.Builder builder = 
				InstrumentDefinition.newBuilder();
		
		/* market identifier; must be globally unique; */
		builder.setInstrumentId(inst.getGUID().getGUID());
		
		/* vendor */
		if(inst.contains(VENDOR)) {
			builder.setVendor(inst.get(VENDOR).toString());
		}
		
		/* market symbol; can be non unique; */
		if(inst.contains(VENDOR_SYMBOL)) {
			builder.setVendorSymbol(inst.get(VENDOR_SYMBOL).toString());
		}
		/* market free style description; can be used in full text search */
		if(inst.contains(DESCRIPTION)) {
			builder.setDescription(inst.get(DESCRIPTION).toString());
		}
		
		/* market originating exchange identifier */
		if(inst.contains(EXCHANGE_ID)) {
			builder.setExchange(inst.get(EXCHANGE_ID).toString());
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
			builder.setCurrency(inst.get(CURRENCY).name());
		}
		
		/* price step / increment size / tick size */
		if(inst.contains(PRICE_STEP)) {
			final PriceValue step = inst.get(PRICE_STEP);
			step.norm();
			builder.setMinPriceIncrementMantissa(step.mantissa());
			builder.setMinPriceIncrementExponent(step.exponent());
		}
		
		/* value of a future contract / stock share */
		if(inst.contains(POINT_VALUE)) {
			final PriceValue val = inst.get(POINT_VALUE);
			val.norm();
			builder.setPointValueMantissa(val.mantissa());
			builder.setPointValueExponent(val.exponent());
		}
		
		/* display fraction base : decimal(10) vs binary(2), etc. */
		if(inst.contains(DISPLAY_BASE)) {
			builder.setDisplayFractionDenominator((int)inst.get(DISPLAY_BASE).asLong());
		}
		
		/* display fraction exponent */
		if(inst.contains(DISPLAY_EXPONENT)) {
			builder.setDisplayExponent((int)inst.get(DISPLAY_EXPONENT).asLong());
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
		
		/* timezone represented as offset in minutes from utc */
		if(inst.contains(TIME_ZONE_OFFSET)) {
			builder.setTimeZoneOffset((int)inst.get(TIME_ZONE_OFFSET).asLong());
		}
		
		return builder.build();
	}

}
