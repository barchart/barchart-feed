package com.barchart.feed.base.market.api;

import com.barchart.feed.api.framework.inst.Instrument;
import com.barchart.util.values.api.Value;

public interface NEWMarketMaker<Message extends MarketMessage> {
	
	boolean isRegistered(NEWMarketAgent<Message, ?> agent);
	<V extends Value<V>> boolean register(NEWMarketAgent<Message, V> agent);
	<V extends Value<V>> boolean unregister(NEWMarketAgent<Message, V> agent);
	
	int marketCount();
	boolean isRegistered(Instrument instrument);
	
	/* Market Maker Provider */
	void clearAll();
	
	void make(Message message);

	/* Still Needed??? */
	void add(MarketRegListener listener);
	void remove(MarketRegListener listener);
	void notifyRegListeners();
	
	void register(Instrument instrument);
	void unregister(Instrument instrument);
	
}
