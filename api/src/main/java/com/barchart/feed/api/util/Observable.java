package com.barchart.feed.api.util;

import com.barchart.util.observer.Observer;

public interface Observable<V> {
	
	void subscribe(Observer<V> observer);

}
