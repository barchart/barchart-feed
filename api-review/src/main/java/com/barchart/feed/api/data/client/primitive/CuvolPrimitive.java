package com.barchart.feed.api.data.client.primitive;

import java.util.List;

public interface CuvolPrimitive {

	double firstPriceDouble();
	double tickSizeDouble();
	List<Long> cuvolListLong();
	
}
