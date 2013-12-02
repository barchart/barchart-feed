package com.barchart.feed.series;


public class Thingy implements rx.Subscription {
	private Test<?> publisher;
	
	public <T> Thingy(Test<T> publisher) {
		this.publisher = publisher;
	}
	
	@Override
	public void unsubscribe() {
		publisher.unsubscribe(this);
	}

}
