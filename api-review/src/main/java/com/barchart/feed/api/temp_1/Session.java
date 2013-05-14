package com.barchart.feed.api.temp_1;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface Session {

	interface Easy {

		double open();

		double volume();

		long timeOpened();

	}

	interface Formal {

		Price open();

		Size volume();

		Time timeOpened();

	}

	Easy easy();

	Formal formal();

}
