package com.barchart.feed.series.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.Node;
import com.barchart.feed.api.series.services.Subscription;

public class AnalyticNode extends Node {
	
	/** Holds the keys which describe the input mappings and are used to correlate the inputs with their {@link Subscription}s */
	private Map<String, Subscription> inputKeyMap = new ConcurrentHashMap<String, Subscription>();
	/** Holds the keys which describe the output mappings and are used to correlate the output with their {@link Subscription}s */
	private Map<String, Subscription> outputKeyMap = new ConcurrentHashMap<String, Subscription>();
	
	

	@Override
	public void updateModifiedSpan(Span span, Subscription subscription) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasAllAncestorUpdates() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Span process() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Subscription> getOutputSubscriptions() {
		return new ArrayList<Subscription>(outputKeyMap.values());
	}

	@Override
	public List<Subscription> getInputSubscriptions() {
		return new ArrayList<Subscription>(inputKeyMap.values());
	}

	@Override
	public <E extends TimePoint> TimeSeries<E> getOutputDataSeries(Subscription subscription) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends TimePoint> TimeSeries<E> getInputDataSeries(Subscription subscription) {
		// TODO Auto-generated method stub
		return null;
	}

}
