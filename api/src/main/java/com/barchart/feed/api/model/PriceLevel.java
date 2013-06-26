package com.barchart.feed.api.model;

import com.barchart.feed.api.model.data.Book;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface PriceLevel {

	Price price();

	Size size();

	Book.Side side();

	int level();

	boolean isNull();

	final PriceLevel NULL_PRICE_LEVEL = new PriceLevel() {

		@Override
		public Price price() {
			return Price.NULL;
		}

		@Override
		public Size size() {
			return Size.NULL;
		}

		@Override
		public Book.Side side() {
			return Book.Side.NULL;
		}

		@Override
		public int level() {
			return 0;
		}

		@Override
		public boolean isNull() {
			return true;
		}

	};

}
