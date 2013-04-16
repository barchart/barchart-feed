package com.barchart.feed.base.market.api;

import com.barchart.feed.api.missive.Callback;
import com.barchart.feed.api.missive.Conditional;
import com.barchart.util.values.api.Value;

public interface NEWMarketAgent<Message extends MarketMessage, V extends Value<V>> 
		extends Conditional, Callback<V> {

	public void activate();
	public void deactivate();
	public void dismiss();
	
	// Message should be abstract, aka TagMap.
	// Because it's not, Agent must be parameterized, which is lame
	boolean inspect(Message message);
	
}
