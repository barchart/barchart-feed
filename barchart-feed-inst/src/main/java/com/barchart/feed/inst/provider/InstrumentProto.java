package com.barchart.feed.inst.provider;

import static com.barchart.feed.api.fields.InstrumentField.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.enums.MarketCurrency;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.missive.core.MissiveException;
import com.barchart.missive.core.Tag;
import com.barchart.proto.buf.inst.InstrumentDefinition;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.util.values.api.TimeInterval;
import com.barchart.util.values.provider.ValueBuilder;
import com.barchart.util.values.provider.ValueConst;
import com.google.protobuf.InvalidProtocolBufferException;

class InstrumentProto extends InstrumentBase implements Instrument {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(InstrumentProto.class);

	private final InstrumentGUID guid;

	@SuppressWarnings("rawtypes")
	private final Map<Tag, Object> map = new HashMap<Tag, Object>();

	InstrumentProto(final InstrumentDefinition i) {

		map.put(MARKET_ID,
				ValueBuilder.newText(String.valueOf(i.getMarketId())));

		map.put(VENDOR, ValueBuilder.newText(i.getVendorId()));
		map.put(SYMBOL, ValueBuilder.newText(i.getSymbol()));
		map.put(DESCRIPTION, ValueBuilder.newText(i.getDescription()));
		map.put(EXCHANGE_CODE, ValueBuilder.newText(i.getExchangeCode()));
		map.put(BOOK_DEPTH, ValueBuilder.newSize(i.getBookDepth()));
		map.put(CFI_CODE, ValueBuilder.newText(i.getCfiCode()));
		map.put(CURRENCY, MarketCurrency.fromString(i.getCurrencyCode()));

		map.put(PRICE_STEP, ValueConst.NULL_PRICE);
		map.put(POINT_VALUE, ValueConst.NULL_PRICE);

		/* Price Display Fields */
		map.put(DISPLAY_BASE, ValueBuilder.newSize(i.getDisplayBase()));
		map.put(DISPLAY_EXPONENT, ValueBuilder.newSize(i.getDisplayExponent()));

		/* Calendar Fields */
		if (i.hasCalendar()) {
			final Interval instLife = i.getCalendar().getLifeTime();
			map.put(LIFETIME, ValueBuilder.newTimeInterval(instLife.getTimeStart(),
					instLife.getTimeFinish()));

			final List<Interval> mktHours = i.getCalendar()
					.getMarketHoursList();
			final TimeInterval[] sessions = new TimeInterval[mktHours.size()];

			for (int n = 0; n < mktHours.size(); n++) {
				sessions[n] = ValueBuilder.newTimeInterval(mktHours.get(n).getTimeStart(),
						mktHours.get(n).getTimeFinish());
			}
			map.put(MARKET_HOURS, sessions);
		}

		map.put(TIME_ZONE, ValueBuilder.newSize(i.getTimeZoneOffset()));

		guid = new InstrumentGUIDImpl(String.valueOf(i.getMarketId()));

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
