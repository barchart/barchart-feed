package com.barchart.feed.series.services;

import java.util.List;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.Node;
import com.barchart.feed.api.series.services.Subscription;

public class BarBuilder extends Node {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Subscription> getInputSubscriptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends TimePoint> TimeSeries<E> getOutputTimeSeries(Subscription subscription) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends TimePoint> TimeSeries<E> getInputTimeSeries(Subscription subscription) {
		// TODO Auto-generated method stub
		return null;
	}

}
