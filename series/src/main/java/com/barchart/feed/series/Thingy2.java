package com.barchart.feed.series;


public class Thingy2 implements rx.Subscription {
	private Test2 publisher;
	
	public Thingy2(Test2 publisher) {
		this.publisher = publisher;
	}
	
	@Override
	public void unsubscribe() {
		publisher.unsubscribe(this);
	}

}
