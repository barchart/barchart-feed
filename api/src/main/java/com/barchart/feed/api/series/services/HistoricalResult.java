package com.barchart.feed.api.series.services;

import java.util.List;

public interface HistoricalResult {
	public NodeIODescriptor getIODescriptor();
	public List<String> getResult();  
}
