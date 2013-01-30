package com.barchart.feed.inst.provider;

import static com.barchart.feed.inst.api.InstrumentField.*;

import java.util.EnumMap;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.InstrumentConst;
import com.barchart.feed.inst.enums.MarketBookType;
import com.barchart.feed.inst.enums.MarketDisplay.Fraction;
import com.barchart.proto.buf.inst.BookType;
import com.barchart.proto.buf.inst.Calendar;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.proto.buf.inst.PriceDisplay;
import com.barchart.proto.buf.inst.PriceFraction;

public final class InstrumentProtoBuilder {
	
	private static final EnumMap<MarketBookType, BookType> bookTypeMap = 
			new EnumMap<MarketBookType, BookType>(MarketBookType.class);
	
	static {
		bookTypeMap.put(MarketBookType.EMPTY, BookType.NoBook);
		bookTypeMap.put(MarketBookType.DEFAULT, BookType.DefaultBook);
		bookTypeMap.put(MarketBookType.IMPLIED, BookType.ImpliedBook);
		bookTypeMap.put(MarketBookType.COMBO, BookType.CombinedBook);
	}
	
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
	
	public static com.barchart.proto.buf.inst.Instrument build(final Instrument inst) {
		
		if(inst == null || inst.equals(InstrumentConst.NULL_INSTRUMENT)) {
			return null;
		}
		
		final com.barchart.proto.buf.inst.Instrument.Builder builder = 
				com.barchart.proto.buf.inst.Instrument.newBuilder();
		
		builder.setTargetId(inst.getGUID().getGUID());
		
		if(inst.contains(BOOK_TYPE)) {
			builder.setBookType(bookTypeMap.get(inst.get(BOOK_TYPE)));
		}
		
		if(inst.contains(BOOK_SIZE)) {
			builder.setBookSize((int)inst.get(BOOK_SIZE).asLong());
		}
		
		if(inst.contains(SYMBOL)) {
			builder.setSymbol(inst.get(SYMBOL).toString());
		}
		
		if(inst.contains(DESCRIPTION)) {
			builder.setDescription(inst.get(DESCRIPTION).toString());
		}
		
		if(inst.contains(TYPE)) {
			builder.setCodeCFI(inst.get(TYPE).getCode());
		}
		
		if(inst.contains(CURRENCY)) {
			builder.setCurrency(inst.get(CURRENCY).name());
		}
		
		/* Price Display */
		final PriceDisplay.Builder pBuilder = PriceDisplay.newBuilder();
		pBuilder.setFraction(fracTypeMap.get(inst.get(FRACTION)));
		// PriceFactor???
		builder.setPriceDisplay(pBuilder.build());
		
		/* Calendar */
		if(inst.contains(DATE_START) && inst.contains(DATE_FINISH)) {
			final Calendar.Builder calBuilder = Calendar.newBuilder();
			final Interval.Builder intBuilder = Interval.newBuilder();
			intBuilder.setTimeStart(inst.get(DATE_START).asMillisUTC());
			intBuilder.setTimeFinish(inst.get(DATE_FINISH).asMillisUTC());
			
			calBuilder.setLifeTime(intBuilder.build());
			
			intBuilder.clear();
			intBuilder.setTimeStart(inst.get(TIME_OPEN).asMillisUTC());
			intBuilder.setTimeFinish(inst.get(TIME_CLOSE).asMillisUTC());
			
			calBuilder.addMarketHours(0, intBuilder.build());
			builder.setCalendar(calBuilder.build());
		}
		
		// Missing : PRICE_POINT, PRICE_STEP, TIME_ZONE, GROUP_ID, EXCHANGE_ID
		
		return builder.build();
	}

}
