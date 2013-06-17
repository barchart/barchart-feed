package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.MarketData;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface Trade extends MarketData<Trade> {

	Price price();

	Size size();

	Time time();

}
