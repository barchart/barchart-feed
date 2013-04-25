package com.barchart.feed.api.data.framework;

import com.barchart.feed.api.data.FrameworkElement;
import com.barchart.feed.api.data.client.TradeObject;
import com.barchart.missive.api.TagMap;

public interface Trade extends TagMap, TradeObject, FrameworkElement<Trade> {

}
