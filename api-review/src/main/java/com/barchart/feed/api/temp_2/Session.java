package com.barchart.feed.api.temp_2;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

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
