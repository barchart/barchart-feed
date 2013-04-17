package com.barchart.feed.api.missive;

import com.barchart.missive.api.Tag;

public interface Condition<V> {

	Tag<V> tag();
	V[] values();
	
}