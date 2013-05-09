package com.barchart.feed.api.temp;

import com.barchart.feed.api.data.temp.PriceValue;
import com.barchart.feed.api.data.temp.SizeValue;
import com.barchart.feed.api.data.temp.TimeValue;

public interface Session {

	interface Easy {

		double open();

		double volume();

		long timeOpened();

	}

	interface Formal {

		PriceValue open();

		SizeValue volume();

		TimeValue timeOpened();

	}

	Easy easy();

	Formal formal();

}
