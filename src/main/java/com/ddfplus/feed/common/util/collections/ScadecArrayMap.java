package com.ddfplus.feed.common.util.collections;

import java.util.Map;

import com.barchart.util.values.lang.ScaledDecimal;

public interface ScadecArrayMap<T extends ScaledDecimal<T, ?>, V>
		extends Map<T, V> {

	int limit();

	//

	T keyStep();

	T keyHead();

	T keyTail();

	//

	int ERROR_INDEX = -1;

	int getIndex(T key);

	V get(int index);

	//

}
