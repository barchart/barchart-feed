package com.barchart.feed.api.model.data;

import java.util.ArrayList;
import java.util.List;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.Tuple;

public interface Cuvol extends MarketData<Cuvol> {

	interface Entry extends Tuple {

		@Override
		Price price();

		@Override
		Size size();

		int place();

		Entry NULL = new Entry() {

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
			
			@Override
			public String toString() {
				return "NULL CUVOL ENTRY";
			}

		};

	}

	Price firstPrice();

	Price tickSize();

	List<Entry> entryList();

	Entry lastCuvolUpdate();

	public static final Cuvol NULL = new Cuvol() {

		@Override
		public Instrument instrument() {
			return Instrument.NULL;
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
		public Price firstPrice() {
			return Price.NULL;
		}

		@Override
		public Price tickSize() {
			return Price.NULL;
		}

		@Override
		public List<Entry> entryList() {
			return new ArrayList<Entry>();
		}

		@Override
		public Entry lastCuvolUpdate() {
			return Entry.NULL;
		}

		@Override
		public Cuvol freeze() {
			return this;
		}
		
		@Override
		public String toString() {
			return "NULL CUVOL";
		}

	};

}
