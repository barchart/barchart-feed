package com.barchart.feed.api.util;

public interface Observer<T> {

	void onNext(T t);
	
}
