package com.barchart.feed.api.series.service;

import java.util.List;

import com.barchart.feed.api.series.analytics.Subscription;

public interface HistoricalResult {
	public Subscription getSubscription();
	public List<String> getResult();  
}
