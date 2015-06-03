package com.barchart.feed.base.participant;

import java.util.Set;

import com.barchart.feed.api.Agent;
import com.barchart.feed.api.AgentID;
import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.consumer.AgentLifecycle;
import com.barchart.feed.api.filter.Filter;
import com.barchart.feed.api.filter.FilterUpdatable;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.model.meta.id.MetadataID;

public interface FrameworkAgent<V extends MarketData<V>> extends AgentLifecycle, FilterUpdatable, Filter  {

	enum AgentType {
		MARKET, BOOK, TRADE, CUVOL, SESSION
	}
	
	AgentType agentType();
	
	Agent agent();
	
	AgentID id(); 
	
	Class<V> type();

	MarketObserver<V> callback();

	V data(Market market);

	Set<MetadataID<?>> subscriptionIDs();

}
