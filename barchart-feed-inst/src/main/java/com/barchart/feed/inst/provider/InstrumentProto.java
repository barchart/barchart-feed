package com.barchart.feed.inst.provider;

import static com.barchart.feed.inst.api.InstrumentField.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.InstrumentField;
import com.barchart.feed.inst.api.InstrumentGUID;
import com.barchart.feed.inst.api.TimeInterval;
import com.barchart.feed.inst.enums.CodeCFI;
import com.barchart.feed.inst.enums.MarketCurrency;
import com.barchart.missive.core.MissiveException;
import com.barchart.missive.core.Tag;
import com.barchart.proto.buf.inst.InstrumentDefinition;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.util.values.provider.ValueBuilder;
import com.barchart.util.values.provider.ValueConst;
import com.google.protobuf.InvalidProtocolBufferException;

class InstrumentProto extends InstrumentBase implements Instrument {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(InstrumentProto.class);
	
	private final InstrumentGUID guid;
	
	@SuppressWarnings("rawtypes")
	private final Map<Tag, Object> map = new HashMap<Tag, Object>();
	
	InstrumentProto(final InstrumentDefinition i) {
		
		map.put(GUID, ValueBuilder.newText(String.valueOf(i.getInstrumentId())));
		map.put(VENDOR, ValueBuilder.newText(i.getVendor()));
		map.put(VENDOR_SYMBOL, ValueBuilder.newText(i.getVendorSymbol()));
		map.put(DESCRIPTION, ValueBuilder.newText(i.getDescription()));
		map.put(EXCHANGE_ID, ValueBuilder.newText(i.getExchange()));
		map.put(BOOK_DEPTH, ValueBuilder.newSize(i.getBookDepth()));
		map.put(CFI_CODE, CodeCFI.fromCode(i.getCfiCode()));
		map.put(CURRENCY, MarketCurrency.fromString(i.getCurrency()));
		
		map.put(PRICE_STEP, ValueConst.NULL_PRICE);
		map.put(POINT_VALUE, ValueConst.NULL_PRICE);
		
		/* Price Display Fields */
		map.put(DISPLAY_BASE, ValueBuilder.newSize(i.getDisplayFractionDenominator()));
		map.put(DISPLAY_EXPONENT, ValueBuilder.newSize(i.getDisplayExponent()));
		
		/* Calendar Fields */
		if(i.hasCalendar()) {
			final Interval instLife = i.getCalendar().getLifeTime();
			map.put(LIFETIME, new TimeInterval(instLife.getTimeStart(), instLife.getTimeFinish()));
			
			final List<Interval> mktHours = i.getCalendar().getMarketHoursList();
			final TimeInterval[] sessions = new TimeInterval[mktHours.size()];
			
			for(int n = 0; n < mktHours.size(); n++) {
				sessions[n] = new TimeInterval(mktHours.get(n).getTimeStart(), mktHours.get(n).getTimeFinish());
			}
			map.put(MARKET_HOURS, sessions);
		}
		
		map.put(TIME_ZONE_OFFSET, ValueBuilder.newSize(i.getTimeZoneOffset()));
		
		guid = new InstrumentGUIDImpl(i.getInstrumentId());
		
	}
	
	InstrumentProto(final byte[] bytes) throws InvalidProtocolBufferException {
		this(InstrumentDefinition.parseFrom(bytes));
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
