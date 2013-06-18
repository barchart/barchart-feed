package com.barchart.feed.api.model;

import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.impl.ValueConst;

public interface PriceLevel {

	Price price();

	Size size();

	MarketSide side();

	int level();
	
	public static final PriceLevel NULL_PRICE_LEVEL = new PriceLevel() {

		@Override
		public Price price() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Size size() {
			return ValueConst.NULL_SIZE;
		}

		@Override
		public MarketSide side() {
			return MarketSide.NULL;
		}

		@Override
		public int level() {
			return 0;
		}
		
	};

}
