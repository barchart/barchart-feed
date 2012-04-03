package com.ddfplus.feed.common.util.collections;

import com.barchart.util.values.api.PriceValue;

public class PriceRingBuffer<V> extends ScadecRingBufferSimple<PriceValue, V> {

	public PriceRingBuffer(final int size, final PriceValue keyStep) {
		super(size, keyStep);
	}

}
