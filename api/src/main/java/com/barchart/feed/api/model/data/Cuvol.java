package com.barchart.feed.api.model.data;

import java.util.ArrayList;
import java.util.List;

import com.barchart.feed.api.model.CuvolEntry;
import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;

public interface Cuvol extends MarketData<Cuvol> {

	// interface Entry {
	// }

	// List<Entry> entryList();

	Price firstPrice();

	Price tickSize();

	List<CuvolEntry> cuvolList();

	CuvolEntry lastCuvolUpdate();

	public static final Cuvol NULL_CUVOL = new Cuvol() {

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
		public Cuvol copy() {
			return this;
		}

		@Override
		public Price firstPrice() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Price tickSize() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public List<CuvolEntry> cuvolList() {
			return new ArrayList<CuvolEntry>();
		}

		@Override
		public CuvolEntry lastCuvolUpdate() {
			return CuvolEntry.NULL_CUVOL_ENTRY;
		}

	};

}
