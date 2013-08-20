package com.barchart.feed.api.util;

public interface Observable<T> {

	void subscribe(Observer<T> observer);
	
}
