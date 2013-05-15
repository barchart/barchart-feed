package com.barchart.feed.api.data.object;

import com.barchart.feed.api.data.common.InstrumentCommon;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface InstrumentObject extends InstrumentCommon {

	Size maxBookDepth();
	Price tickSize();
	Price pointValue();
	
}
