package com.barchart.feed.api.market;

public interface Filter<M> {
	
	boolean filter(M m);

}
