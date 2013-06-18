package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.PriceLevel;

public interface TopOfBook {

	PriceLevel bid();
	
	PriceLevel ask();
	
	public static final TopOfBook NULL_TOP_OF_BOOK = new TopOfBook() {

		@Override
		public PriceLevel bid() {
			return PriceLevel.NULL_PRICE_LEVEL;
		}

		@Override
		public PriceLevel ask() {
			return PriceLevel.NULL_PRICE_LEVEL;
		}
		
	};
	
}
