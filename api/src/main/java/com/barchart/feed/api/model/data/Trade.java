package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;

public interface Trade extends MarketData<Trade> {

	Price price();

	Size size();

	/**
	 * 
	 * @return The time the trade occured at the exchange.
	 */
	Time time();

	public static final Trade NULL_TRADE = new Trade() {

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
		public Trade copy() {
			return this;
		}

		@Override
		public Price price() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Size size() {
			return ValueConst.NULL_SIZE;
		}

		@Override
		public Time time() {
			return ValueConst.NULL_TIME;
		}
		
	};
	
}
