package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.Metadata;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;

public interface Exchange extends MarketData<Exchange>, Metadata {
	
	String name();
	
	String code();

	public static Exchange NULL_EXCHANGE = new Exchange() {

		@Override
		public Time updated() {
			return ValueConst.NULL_TIME;
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public Exchange copy() {
			return this;
		}

		@Override
		public String name() {
			return "NULL_EXCHANGE";
		}

		@Override
		public String code() {
			return "NULL_EXCHANGE";
		}

		@Override
		public Instrument instrument() {
			return Instrument.NULL_INSTRUMENT;
		}
		
	};
	
}
