package com.barchart.feed.base.market.api;

import com.barchart.util.values.api.Value;

//extends Conditional, Callback
public interface NEWMarketAgent<Message extends MarketMessage, V extends Value<V>>	{

	public void activate();
	public void deactivate();
	public void dismiss();
	
	// Message should be abstract, aka TagMap.
	// Because it's not, Agent must be parameterized, which is lame
	boolean inspect(Message message);
	
}
