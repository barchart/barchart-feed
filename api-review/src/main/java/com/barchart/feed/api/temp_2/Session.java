package com.barchart.feed.api.temp_2;

import com.barchart.feed.api.data.value.Price;
import com.barchart.feed.api.data.value.Size;
import com.barchart.feed.api.data.value.Time;

/**
 * Look: interface looks clean, documented in single place only via javadoc
 * inheritance.
 */
public interface Session extends SessionEasy, SessionFull {

	@Override
	Price open();

	@Override
	double openDouble();

	@Override
	Time timeOpened();

	@Override
	long timeOpenedLong();

	@Override
	Size volume();

	@Override
	double volumeDouble();

}
