/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.cuvol.api;

import java.util.Collections;
import java.util.List;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Time;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.provider.ValueConst;

public interface MarketDoCuvol extends MarketCuvol {

	void add(PriceValue price, SizeValue size, TimeValue time);

	MarketCuvolEntry getLastEntry();

	void clear();

	MarketDoCuvol NULL = new MarketDoCuvol() {

		@Override
		public PriceValue priceFirst() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public PriceValue priceStep() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public SizeValue[] entries() {
			return new SizeValue[0];
		}

		@Override
		public MarketCuvol freeze() {
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
		public Price firstPrice() {
			return Price.NULL;
		}

		@Override
		public Price tickSize() {
			return Price.NULL;
		}

		@Override
		public List<Entry> entryList() {
			return Collections.emptyList();
		}

		@Override
		public Entry lastCuvolUpdate() {
			return Entry.NULL;
		}

		@Override
		public Instrument instrument() {
			return Instrument.NULL;
		}

		@Override
		public Time updated() {
			return Time.NULL;
		}

		@Override
		public void add(PriceValue price, SizeValue size, TimeValue time) {
			
		}

		@Override
		public MarketCuvolEntry getLastEntry() {
			return MarketCuvolEntry.NULL;
		}

		@Override
		public void clear() {
			
		}
		
	};
}
