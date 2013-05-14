package com.barchart.feed.api.temp_1;

import com.barchart.feed.api.data.value.Price;
import com.barchart.feed.api.data.value.Size;
import com.barchart.feed.api.data.value.Time;

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
