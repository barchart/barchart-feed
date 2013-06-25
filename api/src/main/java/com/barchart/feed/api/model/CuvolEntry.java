package com.barchart.feed.api.model;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.impl.ValueConst;

/** FIXME move inside cuvol */
public interface CuvolEntry {

	Price price();

	Size volume();

	int place();

	CuvolEntry NULL_CUVOL_ENTRY = new CuvolEntry() {

		@Override
		public Price price() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Size volume() {
			return ValueConst.NULL_SIZE;
		}

		@Override
		public int place() {
			return 0;
		}

	};

}
