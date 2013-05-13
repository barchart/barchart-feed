package com.barchart.feed.api.temp;

import com.barchart.feed.api.data.Price;
import com.barchart.feed.api.data.Size;
import com.barchart.feed.api.data.Time;

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
