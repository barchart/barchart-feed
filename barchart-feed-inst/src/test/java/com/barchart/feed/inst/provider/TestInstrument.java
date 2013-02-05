package com.barchart.feed.inst.provider;

import static com.barchart.feed.inst.api.InstrumentField.BOOK_DEPTH;
import static com.barchart.feed.inst.api.InstrumentField.CURRENCY;
import static com.barchart.feed.inst.api.InstrumentField.DESCRIPTION;
import static com.barchart.feed.inst.api.InstrumentField.EXCHANGE_CODE;
import static com.barchart.feed.inst.api.InstrumentField.FIELDS;
import static com.barchart.feed.inst.api.InstrumentField.MARKET_ID;
import static com.barchart.feed.inst.api.InstrumentField.POINT_VALUE;
import static com.barchart.feed.inst.api.InstrumentField.PRICE_STEP;
import static com.barchart.feed.inst.api.InstrumentField.SYMBOL;
import static com.barchart.feed.inst.api.InstrumentField.TIME_ZONE;
import static com.barchart.feed.inst.api.InstrumentField.CFI_CODE;
import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static com.barchart.util.values.provider.ValueBuilder.newSize;
import static com.barchart.util.values.provider.ValueBuilder.newText;
import static com.barchart.util.values.provider.ValueBuilder.newTime;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.enums.CodeCFI;
import com.barchart.feed.inst.enums.MarketCurrency;
import com.barchart.feed.inst.enums.MarketDisplay.Fraction;
import com.barchart.missive.core.TagMapSafe;
import com.barchart.missive.hash.HashTagMapSafe;
import com.barchart.proto.buf.inst.Calendar;
import com.barchart.proto.buf.inst.InstrumentDefinition;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeValue;

public class TestInstrument {

	public static final SizeValue ID_V = newSize(123456);
	public static final TextValue GROUP_ID_V = newText("groupID");
	public static final TextValue EXCHANGE_ID_V = newText("exchangeID");
	public static final TextValue SYMBOL_V = newText("symbol");
	public static final TextValue DESCRIPTION_V = newText("description");
	public static final SizeValue BOOK_SIZE_V = newSize(10);
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
		map.set(MARKET_ID, ID_V);
		map.set(EXCHANGE_CODE, EXCHANGE_ID_V);
		map.set(SYMBOL, SYMBOL_V);
		map.set(DESCRIPTION, DESCRIPTION_V);
		map.set(BOOK_DEPTH, BOOK_SIZE_V);
		map.set(PRICE_STEP, PRICE_STEP_V);
		map.set(POINT_VALUE, PRICE_POINT_V);
		map.set(PRICE_STEP, PRICE_STEP_V);
		map.set(CURRENCY, CURRENCY_V);
		map.set(CFI_CODE, TYPE_V);
//		map.set(TIME_ZONE_OFFSET, TIME_ZONE_V);
//		map.set(TIME_OPEN, TIME_OPEN_V);
//		map.set(TIME_CLOSE, TIME_CLOSE_V);
//		map.set(DATE_START, DATE_START_V);
//		map.set(DATE_FINISH, DATE_FINISH_V);
	}
	
	public static final Instrument TEST_INST_BARCHART = new InstrumentImpl(map);
	
	static InstrumentDefinition.Builder instBuilder;
	
	static {
		
		Interval.Builder intervalBuilder = Interval.newBuilder();
		intervalBuilder.setTimeStart(DATE_START_V.asMillisUTC());
		intervalBuilder.setTimeFinish(DATE_FINISH_V.asMillisUTC());
		
		Calendar.Builder calBuilder = Calendar.newBuilder();
		calBuilder.setLifeTime(intervalBuilder.build());
		
		intervalBuilder.clear();
		intervalBuilder.setTimeStart(TIME_OPEN_V.asMillisUTC());
		intervalBuilder.setTimeFinish(TIME_CLOSE_V.asMillisUTC());
		calBuilder.addMarketHours(intervalBuilder.build()); 
		
		
		instBuilder = InstrumentDefinition.newBuilder();
//		instBuilder.setSourceId(SYMBOL_V.toString());
//		instBuilder.setTargetId(Long.parseLong(ID_V.toString()));
//		instBuilder.setInstType(InstType.FutureInst);
//		instBuilder.setBookType(BookType.DefaultBook);
//		instBuilder.setBookSize(10);
//		instBuilder.setSymbol(SYMBOL_V.toString());
//		instBuilder.setDescription(DESCRIPTION_V.toString());
//		instBuilder.setPriceDisplay(priceDisplayBuilder.build());
//		instBuilder.setCalendar(calBuilder.build());
//		instBuilder.setCodeCFI(TYPE_V.name());
		instBuilder.setCurrencyCode(CURRENCY_V.name());
		instBuilder.setRecordCreateTime(1234);
		instBuilder.setRecordUpdateTime(1235);
	}
	
	public static final InstrumentDefinition TEST_INST_PROTO = instBuilder.build();
	
}
