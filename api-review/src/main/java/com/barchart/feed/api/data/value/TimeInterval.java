package com.barchart.feed.api.data.value;

import com.barchart.feed.api.util.Copyable;

public interface TimeInterval extends Copyable<TimeInterval> {
	
	Time start();
	Time stop();

}
