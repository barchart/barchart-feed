package com.barchart.feed.market.provider;

import org.joda.time.DateTime;

import com.barchart.feed.api.data.MarketTag;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.data.framework.TopOfBook;
import com.barchart.feed.api.fields.MarketField;
import com.barchart.feed.api.message.Snapshot;
import com.barchart.missive.core.ObjectMap;

public class TopOfBookSnapshot extends ObjectMap implements Snapshot<TopOfBook>{

	private final Instrument instrument;
	
	public TopOfBookSnapshot(final Instrument inst) {
		instrument = inst;
	}
	
	@Override
	public Instrument instrument() {
		return instrument;
	}

	@Override
	public MarketTag<TopOfBook> tag() {
		return MarketField.TOP_OF_BOOK;
	}
		
	@Override
	public DateTime time() {
		return null;
	}

}
