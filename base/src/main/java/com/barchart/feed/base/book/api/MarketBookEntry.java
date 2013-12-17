/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book.api;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Book.Entry;
import com.barchart.feed.api.model.data.Book.Side;
import com.barchart.feed.base.market.api.MarketEntry;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.Value;
import com.barchart.feed.base.values.provider.ValueConst;
import com.barchart.util.common.anno.NotMutable;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

@NotMutable
public interface MarketBookEntry extends Value<MarketBookEntry>, MarketEntry, 
		Book.Entry {

	/**
	 * logical position in the bid or ask side;
	 * 
	 * starts with {@link MarketBook#ENTRY_TOP}
	 * */
	int place();
	
	MarketBookEntry NULL = new MarketBookEntry() {

		@Override
		public MarketBookEntry freeze() {
			return this;
		}

		@Override
		public boolean isFrozen() {
			return true;
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public PriceValue priceValue() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public SizeValue sizeValue() {
			return ValueConst.NULL_SIZE;
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
		public Side side() {
			return Side.NULL;
		}

		@Override
		public int level() {
			return 0;
		}

		@Override
		public int compareTo(Entry o) {
			return 0;
		}

		@Override
		public int place() {
			return 0;
		}
		
	};

}
