package com.barchart.feed.api.data;

import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;

public interface Exchange extends MarketData<Exchange> {
	
	String name();
	
	String code();

	public static Exchange NULL_EXCHANGE = new Exchange() {

		@Override
		public Time lastUpdateTime() {
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
		
	};
	
}
