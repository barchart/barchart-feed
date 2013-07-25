/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.cuvol.api;

import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.base.market.api.MarketEntry;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueConst;

/** stand alone market cumulative volume value */
@NotMutable
public interface MarketCuvolEntry extends Value<MarketCuvolEntry>, MarketEntry, 
		Cuvol.Entry {

	/** logical or relative index of this cuvol entry in the price ladder */
	int place();

	MarketCuvolEntry NULL = new MarketCuvolEntry() {

		@Override
		public MarketCuvolEntry freeze() {
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
		public int place() {
			return 0;
		}
		
	};
}
