package com.barchart.feed.series.services;

import java.util.List;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.Node;
import com.barchart.feed.api.series.services.Subscription;

public class BarBuilder extends Node {
    
    /**
     * Called by ancestors of this {@code Node} in the tree to set
     * the {@link Span} of time modified by that ancestor's 
     * internal processing class.
     * 
     * @param span              the {@link Span} of time processed.
     * @param subscriptions     the List of {@link Subscription}s the ancestor node has processed.
     * @return  
     */
	@Override
	public void updateModifiedSpan(Span span, Subscription subscription) {
		
	}

	/**
     * Returns a flag indicating whether the implementing class has all of its expected input.
     * BarBuilders and their subclasses always return true here because they are guaranteed
     * to have one input and one output.
     * 
     * @return  a flag indicating whether the implementing class has all of its expected input.
     */
	@Override
	public boolean hasAllAncestorUpdates() {
		return true;
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
