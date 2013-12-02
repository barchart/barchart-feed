package com.barchart.feed.series;

import rx.Subscription;


public class TestRxNetwork {
	public TestRxNetwork() {
		final Test<String> node = new Test<String>("BOB");
		final Test<String> node2a = new Test<String>("BILL");
		final Test<String> node2b = new Test<String>("DAVID");
		final Subscription n2aSub = node.subscribe(node2a);
		final Subscription n2bSub = node.subscribe(node2b);
		
		(new Thread() {
			Subscription local = n2aSub;
			public void run() {
				try {
					while(true) {
						Thread.sleep(5000);
						local.unsubscribe();
						Thread.sleep(2000);
						local = node.subscribe(node2a);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void main(String[] args) {
		new TestRxNetwork();
	}
}
