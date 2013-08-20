package com.barchart.feed.api.util;

public interface Observable<V> {
	
	void subscribe(Observer<V> observer);

}
