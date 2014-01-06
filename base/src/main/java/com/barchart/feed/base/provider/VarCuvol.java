/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.collections.PriceArrayMap;
import com.barchart.feed.base.cuvol.api.MarketCuvolEntry;
import com.barchart.feed.base.cuvol.api.MarketDoCuvol;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.feed.base.values.provider.ValueConst;
import com.barchart.util.common.anno.Mutable;
import com.barchart.util.common.anno.NotThreadSafe;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

@Mutable
@NotThreadSafe
public final class VarCuvol extends NulCuvol implements MarketDoCuvol {

	private final PriceArrayMap<SizeValue> map;

	private PriceValue priceLast = ValueConst.NULL_PRICE;
	private TimeValue timeLast = ValueConst.NULL_TIME;
	private Cuvol.Entry entryLast = Cuvol.Entry.NULL;
	
	private final Instrument instrument;
	
	public VarCuvol(final Instrument instrument, final PriceValue priceStep) {
		this.instrument = instrument;
		if(priceStep == null || priceStep.isNull() || priceStep.mantissa() == 0) {
			System.out.println("Price Step was null for " + instrument.symbol());
		}
		this.map = new PriceArrayMap<SizeValue>(priceStep);
	}

	@Override
	public final void add(final PriceValue price, final SizeValue size, 
			final TimeValue time) {

		assert price != null;
		assert size != null;

		SizeValue volume = map.get(price);

		if (volume == null) {
			volume = size;
		} else {
			volume = volume.add(size);
		}

		map.put(price, volume);

		priceLast = price;
		timeLast = time;
		
		entryLast = entry(ValueConverter.price(price), ValueConverter.size(size), 
				map.getIndex(price));

	}

	private Cuvol.Entry entry(final Price price, final Size size, final int place) {
		return new Entry() {

			@Override
			public boolean isNull() {
				return false;
			}

			@Override
			public Price price() {
				return price;
			}

			@Override
			public Size size() {
				return size;
			}

			@Override
			public int place() {
				return place;
			}
			
		};
	}
	
	@Override
	public final DefCuvol freeze() {

		final SizeValue[] entries = entries();

		for (int k = 0; k < entries.length; k++) {
			SizeValue volume = entries[k];
			if (volume == null) {
				volume = ValueConst.NULL_SIZE;
			}
			entries[k] = volume.freeze();
		}

		final DefCuvol that = new DefCuvol(instrument, entries, 
				priceFirst(), priceStep(), ValueConverter.time(timeLast), 
				entryLast);

		return that;

	}

	@Override
	public final SizeValue[] entries() {

		final int size = map.size();

		final SizeValue[] entries = new SizeValue[size];

		for (int k = 0; k < size; k++) {
			SizeValue volume = map.get(k);
			if (volume == null) {
				volume = ValueConst.NULL_SIZE;
			} else {
				volume = volume.freeze();
			}
			entries[k] = volume;
		}

		return entries;

	}

	@Override
	public final boolean isFrozen() {
		return false;
	}

	@Override
	public final PriceValue priceFirst() {
		return map.keyHead();
	}

	@Override
	public final PriceValue priceStep() {
		return map.keyStep();
	}

	@Override
	public final MarketCuvolEntry getLastEntry() {

		final PriceValue price = priceLast;

		if (price == null) {
			return MarketConst.NULL_CUVOL_ENTRY;
		}
		
		if(map.size() == 0) {
			return MarketConst.NULL_CUVOL_ENTRY;
		}

		final SizeValue size = map.get(price);

		if (size == null) {
			return MarketConst.NULL_CUVOL_ENTRY;
		}

		final int index = map.getIndex(price);

		final MarketCuvolEntry entry = new DefCuvolEntry(index, 
				price, size);

		return entry;

	}

	@Override
	public void clear() {
		map.clear();
	}
	
}
