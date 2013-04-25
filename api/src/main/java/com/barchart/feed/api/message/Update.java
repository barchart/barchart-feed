package com.barchart.feed.api.message;

import com.barchart.feed.api.data.FrameworkElement;

public interface Update<M extends FrameworkElement<M>> extends MarketMessage<M> {

}
