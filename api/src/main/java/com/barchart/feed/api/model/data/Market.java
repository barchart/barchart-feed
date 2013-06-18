package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;

public interface Market extends MarketData<Market> {

	Trade lastTrade();

	OrderBook orderBook();

	Cuvol cuvol();
	
	Session session();
	
	public static final Market NULL_MARKET = new Market() {

		@Override
		public Instrument instrument() {
			return Instrument.NULL_INSTRUMENT;
		}

		@Override
		public Time updated() {
			return ValueConst.NULL_TIME;
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public Market copy() {
			return this;
		}

		@Override
		public Trade lastTrade() {
			return Trade.NULL_TRADE;
		}

		@Override
		public OrderBook orderBook() {
			return OrderBook.NULL_ORDERBOOK;
		}

		@Override
		public Cuvol cuvol() {
			return Cuvol.NULL_CUVOL;
		}

		@Override
		public Session session() {
			return Session.NULL_SESSION;
		}
		
	};

}
