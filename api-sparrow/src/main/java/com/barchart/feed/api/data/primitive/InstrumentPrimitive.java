package com.barchart.feed.api.data.primitive;

import com.barchart.feed.api.data.common.InstrumentCommon;

public interface InstrumentPrimitive extends InstrumentCommon {

	long maxBookDepthLong();
	double tickSizeDouble();
	double pointValueDouble();
	
}
