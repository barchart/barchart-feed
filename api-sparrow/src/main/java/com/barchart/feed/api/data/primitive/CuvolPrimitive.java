package com.barchart.feed.api.data.primitive;

import java.util.List;

public interface CuvolPrimitive {

	double firstPriceDouble();
	double tickSizeDouble();
	List<Long> cuvolListLong();
	
}
