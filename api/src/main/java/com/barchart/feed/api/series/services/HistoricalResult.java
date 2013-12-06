package com.barchart.feed.api.series.services;

import java.util.List;

public interface HistoricalResult {
	public Query getQuery();
	public List<String> getResult();  
}
