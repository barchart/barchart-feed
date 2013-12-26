package com.barchart.feed.api.series.service;

import java.util.List;

public interface HistoricalResult {
	public Subscription getSubscription();
	public List<String> getResult();  
}
