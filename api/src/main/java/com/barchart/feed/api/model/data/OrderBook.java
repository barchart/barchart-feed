package com.barchart.feed.api.model.data;

import java.util.ArrayList;
import java.util.List;

import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.PriceLevel;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;

public interface OrderBook extends MarketData<OrderBook> {

	TopOfBook topOfBook();

	List<PriceLevel> entryList(MarketSide side);

	PriceLevel lastBookUpdate();
	
	public static final OrderBook NULL_ORDERBOOK = new OrderBook() {

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
		public OrderBook copy() {
			return this;
		}

		@Override
		public TopOfBook topOfBook() {
			return TopOfBook.NULL_TOP_OF_BOOK;
		}

		@Override
		public List<PriceLevel> entryList(MarketSide side) {
			return new ArrayList<PriceLevel>();
		}

		@Override
		public PriceLevel lastBookUpdate() {
			return PriceLevel.NULL_PRICE_LEVEL;
		}
		
	};

}
