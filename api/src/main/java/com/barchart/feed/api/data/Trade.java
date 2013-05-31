package com.barchart.feed.api.data;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface Trade extends MarketData<Trade> {

	Price price();

	double priceDouble();

	Size size();

	long sizeLong();

	Time time();

	long timeLong();

}
