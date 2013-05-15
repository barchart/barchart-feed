package com.barchart.feed.api.data.client.primitive;

import com.barchart.feed.api.data.client.common.InstrumentCommon;

public interface InstrumentPrimitive extends InstrumentCommon {

	long maxBookDepthLong();
	double tickSizeDouble();
	double pointValueDouble();
	
}
