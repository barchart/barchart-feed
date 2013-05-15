package com.barchart.feed.api.message;

import org.joda.time.DateTime;

import com.barchart.feed.api.data.Instrument;
import com.barchart.missive.api.TagMap;

public interface Message extends TagMap {

	Instrument instrument();
	DateTime time();
	
}
