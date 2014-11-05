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
				throw new UnsupportedOperationException();
			}

			@Override
			public Size size() {
				throw new UnsupportedOperationException();
			}

			@Override
			public int place() {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public String toString() {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean isNull() {
				return true;
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
		public String toString() {
			return "NULL CUVOL";
		}

	};

}
