package com.barchart.feed.api.connection;

import com.barchart.util.value.api.Time;

public interface TimestampListener {

	void listen(Time timestamp);
	
}
