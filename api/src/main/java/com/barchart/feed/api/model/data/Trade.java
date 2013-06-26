package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.Tuple;

public interface Trade extends MarketData<Trade>, Tuple {

	@Override
	Price price();

	@Override
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
			return Time.NULL;
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
			return Price.NULL;
		}

		@Override
		public Size size() {
			return Size.NULL;
		}

		@Override
		public Time time() {
			return Time.NULL;
		}

	};

}
