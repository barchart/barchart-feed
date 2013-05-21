package com.barchart.feed.api.message;

import com.barchart.feed.api.data.Instrument;
import com.barchart.missive.api.TagMap;
import com.barchart.util.value.api.Time;

public interface Message extends TagMap {

	Instrument instrument();
	Time time();
	
}
