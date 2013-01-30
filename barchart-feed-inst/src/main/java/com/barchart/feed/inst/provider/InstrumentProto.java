package com.barchart.feed.inst.provider;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.InstrumentField;
import com.barchart.feed.inst.api.InstrumentGUID;
import com.barchart.feed.inst.enums.CodeCFI;
import com.barchart.feed.inst.enums.MarketBookType;
import com.barchart.feed.inst.enums.MarketCurrency;
import com.barchart.feed.inst.enums.MarketDisplay.Fraction;
import com.barchart.missive.core.MissiveException;
import com.barchart.missive.core.Tag;
import com.barchart.proto.buf.inst.BookType;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.proto.buf.inst.PriceFraction;
import com.barchart.util.values.provider.ValueBuilder;
import com.barchart.util.values.provider.ValueConst;
import com.google.protobuf.InvalidProtocolBufferException;

class InstrumentProto extends InstrumentBase implements Instrument {
	
	private static final Logger log = LoggerFactory.getLogger(InstrumentProto.class);
	
	private final InstrumentGUID guid;
	
	@SuppressWarnings("rawtypes")
	private final Map<Tag, Object> map = new HashMap<Tag, Object>();
	
	private static final EnumMap<BookType, MarketBookType> bookTypeMap = 
			new EnumMap<BookType, MarketBookType>(BookType.class);
	
	static {
		bookTypeMap.put(BookType.NoBook, MarketBookType.EMPTY);
		bookTypeMap.put(BookType.DefaultBook, MarketBookType.DEFAULT);
		bookTypeMap.put(BookType.ImpliedBook, MarketBookType.IMPLIED);
		bookTypeMap.put(BookType.CombinedBook, MarketBookType.COMBO);
	}
	
	private static final EnumMap<PriceFraction, Fraction> fracTypeMap =
			new EnumMap<PriceFraction, Fraction>(PriceFraction.class);
	
	static {
		fracTypeMap.put(PriceFraction.FactionDecimal_Z00, Fraction.DEC_Z00);
		fracTypeMap.put(PriceFraction.FactionDecimal_N01, Fraction.DEC_N01);
		fracTypeMap.put(PriceFraction.FactionDecimal_N02, Fraction.DEC_N02);
		fracTypeMap.put(PriceFraction.FactionDecimal_N03, Fraction.DEC_N03);
		fracTypeMap.put(PriceFraction.FactionDecimal_N04, Fraction.DEC_N04);
		fracTypeMap.put(PriceFraction.FactionDecimal_N05, Fraction.DEC_N05);
		fracTypeMap.put(PriceFraction.FactionDecimal_N06, Fraction.DEC_N06);
		fracTypeMap.put(PriceFraction.FactionDecimal_N07, Fraction.DEC_N07);
		fracTypeMap.put(PriceFraction.FactionDecimal_N08, Fraction.DEC_N08);
		fracTypeMap.put(PriceFraction.FactionDecimal_N09, Fraction.DEC_N09);
		
		fracTypeMap.put(PriceFraction.FactionBinary_Z00, Fraction.BIN_Z00);
		fracTypeMap.put(PriceFraction.FactionBinary_N01, Fraction.BIN_N01);
		fracTypeMap.put(PriceFraction.FactionBinary_N02, Fraction.BIN_N02);
		fracTypeMap.put(PriceFraction.FactionBinary_N03, Fraction.BIN_N03);
		fracTypeMap.put(PriceFraction.FactionBinary_N04, Fraction.BIN_N04);
		fracTypeMap.put(PriceFraction.FactionBinary_N05, Fraction.BIN_N05);
		fracTypeMap.put(PriceFraction.FactionBinary_N06, Fraction.BIN_N06);
		fracTypeMap.put(PriceFraction.FactionBinary_N07, Fraction.BIN_N07);
		fracTypeMap.put(PriceFraction.FactionBinary_N08, Fraction.BIN_N08);
		fracTypeMap.put(PriceFraction.FactionBinary_N09, Fraction.BIN_N09);
	}
	
	InstrumentProto(final com.barchart.proto.buf.inst.Instrument i) {
		
		map.put(InstrumentField.ID, ValueBuilder.newText(String.valueOf(i.getTargetId())));
		map.put(InstrumentField.BOOK_TYPE, bookTypeMap.get(i.getBookType()));
		map.put(InstrumentField.BOOK_SIZE, ValueBuilder.newSize(i.getBookSize()));
		map.put(InstrumentField.SYMBOL, ValueBuilder.newText(i.getSymbol()));
		map.put(InstrumentField.DESCRIPTION, ValueBuilder.newText(i.getDescription()));
		
		map.put(InstrumentField.TYPE, CodeCFI.fromCode(i.getCodeCFI()));
		map.put(InstrumentField.CURRENCY, MarketCurrency.fromString(i.getCurrency()));
		
		/* Price Display Fields */
		map.put(InstrumentField.FRACTION, fracTypeMap.get(i.getPriceDisplay().getFraction()));
		
		/* Calendar Fields */
		if(i.hasCalendar()) {
			final Interval instLife = i.getCalendar().getLifeTime();
			map.put(InstrumentField.DATE_START, ValueBuilder.newTime(instLife.getTimeStart()));
			map.put(InstrumentField.DATE_FINISH, ValueBuilder.newTime(instLife.getTimeFinish()));
			
			if(i.getCalendar().getMarketHoursCount() > 1) {
				log.warn("Market hours contains more than one interval");
			}
			
			final Interval mktHours = i.getCalendar().getMarketHoursList().get(0);
			map.put(InstrumentField.TIME_OPEN, ValueBuilder.newTime(mktHours.getTimeStart()));
			map.put(InstrumentField.TIME_CLOSE, ValueBuilder.newTime(mktHours.getTimeFinish()));
		}
		
		/* Will revisit */
		map.put(InstrumentField.PRICE_POINT, ValueConst.NULL_PRICE);
		map.put(InstrumentField.PRICE_STEP, ValueConst.NULL_PRICE);
		map.put(InstrumentField.TIME_ZONE, ValueConst.NULL_TEXT);
		map.put(InstrumentField.GROUP_ID, ValueConst.NULL_TEXT);
		map.put(InstrumentField.EXCHANGE_ID, ValueConst.NULL_TEXT);
		
		guid = new InstrumentGUIDImpl(i.getTargetId());
		
	}
	
	InstrumentProto(final byte[] bytes) throws InvalidProtocolBufferException {
		this(com.barchart.proto.buf.inst.Instrument.parseFrom(bytes));
	}
	
	@Override
	public InstrumentGUID getGUID() {
		return guid;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V get(final Tag<V> tag) throws MissiveException {
		return (V) map.get(tag);
	}

	@Override
	public boolean contains(final Tag<?> tag) {
		return map.containsKey(tag);
	}

	@Override
	public Tag<?>[] tags() {
		return map.keySet().toArray(new Tag<?>[0]);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Instrument freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
