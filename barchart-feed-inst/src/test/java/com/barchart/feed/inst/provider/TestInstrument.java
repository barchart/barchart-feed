package com.barchart.feed.inst.provider;

import static com.barchart.feed.inst.api.InstrumentField.BOOK_SIZE;
import static com.barchart.feed.inst.api.InstrumentField.BOOK_TYPE;
import static com.barchart.feed.inst.api.InstrumentField.CURRENCY;
import static com.barchart.feed.inst.api.InstrumentField.DATE_FINISH;
import static com.barchart.feed.inst.api.InstrumentField.DATE_START;
import static com.barchart.feed.inst.api.InstrumentField.DESCRIPTION;
import static com.barchart.feed.inst.api.InstrumentField.EXCHANGE_ID;
import static com.barchart.feed.inst.api.InstrumentField.FIELDS;
import static com.barchart.feed.inst.api.InstrumentField.FRACTION;
import static com.barchart.feed.inst.api.InstrumentField.GROUP_ID;
import static com.barchart.feed.inst.api.InstrumentField.ID;
import static com.barchart.feed.inst.api.InstrumentField.PRICE_POINT;
import static com.barchart.feed.inst.api.InstrumentField.PRICE_STEP;
import static com.barchart.feed.inst.api.InstrumentField.SYMBOL;
import static com.barchart.feed.inst.api.InstrumentField.TIME_CLOSE;
import static com.barchart.feed.inst.api.InstrumentField.TIME_OPEN;
import static com.barchart.feed.inst.api.InstrumentField.TIME_ZONE;
import static com.barchart.feed.inst.api.InstrumentField.TYPE;
import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static com.barchart.util.values.provider.ValueBuilder.newSize;
import static com.barchart.util.values.provider.ValueBuilder.newText;
import static com.barchart.util.values.provider.ValueBuilder.newTime;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.enums.CodeCFI;
import com.barchart.feed.inst.enums.MarketBookType;
import com.barchart.feed.inst.enums.MarketCurrency;
import com.barchart.feed.inst.enums.MarketDisplay.Fraction;
import com.barchart.missive.core.TagMapSafe;
import com.barchart.missive.hash.HashTagMapSafe;
import com.barchart.proto.buf.inst.BookType;
import com.barchart.proto.buf.inst.Calendar;
import com.barchart.proto.buf.inst.InstType;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.proto.buf.inst.PriceDisplay;
import com.barchart.proto.buf.inst.PriceFraction;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeValue;

public class TestInstrument {

	public static final TextValue ID_V = newText("123456");
	public static final TextValue GROUP_ID_V = newText("groupID");
	public static final TextValue EXCHANGE_ID_V = newText("exchangeID");
	public static final TextValue SYMBOL_V = newText("symbol");
	public static final TextValue DESCRIPTION_V = newText("description");
	public static final SizeValue BOOK_SIZE_V = newSize(10);
	public static final MarketBookType BOOK_TYPE_V = MarketBookType.DEFAULT;
	public static final PriceValue PRICE_STEP_V = newPrice(25, -1);
	public static final PriceValue PRICE_POINT_V = newPrice(500, 1);
	public static final Fraction FRACTION_V = Fraction.BIN_N01;
	public static final MarketCurrency CURRENCY_V = MarketCurrency.USD;
	public static final CodeCFI TYPE_V = CodeCFI.FUTURE;
	public static final TextValue TIME_ZONE_V = newText("Central");
	public static final TimeValue TIME_OPEN_V = newTime(100000000);
	public static final TimeValue TIME_CLOSE_V = newTime(100000001);
	public static final TimeValue DATE_START_V = newTime(100000002);
	public static final TimeValue DATE_FINISH_V = newTime(100000003);
	
	static final TagMapSafe map = new HashTagMapSafe(FIELDS);
	
	static {
		map.set(ID, ID_V);
		map.set(GROUP_ID, GROUP_ID_V);
		map.set(EXCHANGE_ID, EXCHANGE_ID_V);
		map.set(SYMBOL, SYMBOL_V);
		map.set(DESCRIPTION, DESCRIPTION_V);
		map.set(BOOK_SIZE, BOOK_SIZE_V);
		map.set(BOOK_TYPE, BOOK_TYPE_V);
		map.set(PRICE_STEP, PRICE_STEP_V);
		map.set(PRICE_POINT, PRICE_POINT_V);
		map.set(PRICE_STEP, PRICE_STEP_V);
		map.set(FRACTION, FRACTION_V);
		map.set(CURRENCY, CURRENCY_V);
		map.set(TYPE, TYPE_V);
		map.set(TIME_ZONE, TIME_ZONE_V);
		map.set(TIME_OPEN, TIME_OPEN_V);
		map.set(TIME_CLOSE, TIME_CLOSE_V);
		map.set(DATE_START, DATE_START_V);
		map.set(DATE_FINISH, DATE_FINISH_V);
	}
	
	public static final Instrument TEST_INST_BARCHART = new InstrumentImpl(map);
	
	static com.barchart.proto.buf.inst.Instrument.Builder instBuilder;
	
	static {
		PriceDisplay.Builder priceDisplayBuilder = PriceDisplay.newBuilder();
		priceDisplayBuilder.setPriceFactor(100); // **********???????????
		priceDisplayBuilder.setFraction(PriceFraction.FactionBinary_N01);
		
		Interval.Builder intervalBuilder = Interval.newBuilder();
		intervalBuilder.setTimeStart(DATE_START_V.asMillisUTC());
		intervalBuilder.setTimeFinish(DATE_FINISH_V.asMillisUTC());
		
		Calendar.Builder calBuilder = Calendar.newBuilder();
		calBuilder.setLifeTime(intervalBuilder.build());
		
		intervalBuilder.clear();
		intervalBuilder.setTimeStart(TIME_OPEN_V.asMillisUTC());
		intervalBuilder.setTimeFinish(TIME_CLOSE_V.asMillisUTC());
		calBuilder.addMarketHours(intervalBuilder.build()); 
		
		
		instBuilder = com.barchart.proto.buf.inst.Instrument.newBuilder();
		instBuilder.setSourceId(SYMBOL_V.toString());
		instBuilder.setTargetId(Long.parseLong(ID_V.toString()));
		instBuilder.setInstType(InstType.FutureInst);
		instBuilder.setBookType(BookType.DefaultBook);
		instBuilder.setBookSize(10);
		instBuilder.setSymbol(SYMBOL_V.toString());
		instBuilder.setDescription(DESCRIPTION_V.toString());
		instBuilder.setPriceDisplay(priceDisplayBuilder.build());
		instBuilder.setCalendar(calBuilder.build());
		instBuilder.setCodeCFI(TYPE_V.name());
		instBuilder.setCurrency(CURRENCY_V.name());
		instBuilder.setRecordCreateTime(1234);
		instBuilder.setRecordUpdateTime(1235);
	}
	
	public static final com.barchart.proto.buf.inst.Instrument TEST_INST_PROTO = instBuilder.build();
	
}
