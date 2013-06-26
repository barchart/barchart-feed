package com.barchart.feed.api.model;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

/** FIXME move inside cuvol */
interface CuvolEntry {

	Price price();

	Size volume();

	int place();

	CuvolEntry NULL_CUVOL_ENTRY = new CuvolEntry() {

		@Override
		public Price price() {
			return Price.NULL;
		}

		@Override
		public Size volume() {
			return Size.NULL;
		}

		@Override
		public int place() {
			return 0;
		}

	};

}
