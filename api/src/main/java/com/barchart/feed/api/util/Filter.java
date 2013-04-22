package com.barchart.feed.api.util;

public interface Filter<M> {
	
	boolean filter(M m);

}
