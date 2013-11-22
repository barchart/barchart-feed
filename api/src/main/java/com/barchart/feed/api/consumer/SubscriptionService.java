package com.barchart.feed.api.consumer;

import java.util.Map;

import com.barchart.feed.api.connection.Subscription;
import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.ExchangeID;
import com.barchart.feed.api.model.meta.id.InstrumentID;

public interface SubscriptionService {

	Map<InstrumentID, Subscription<Instrument>> instruments();
	
	Map<ExchangeID, Subscription<Exchange>> exchanges();
	
	//  Add subscribe / unsubscribe
	
	
}
