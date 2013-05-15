package com.barchart.feed.api.data.client.object;

import com.barchart.feed.api.data.client.common.InstrumentCommon;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface InstrumentObject extends InstrumentCommon {

	Size maxBookDepth();
	Price tickSize();
	Price pointValue();
	
}
