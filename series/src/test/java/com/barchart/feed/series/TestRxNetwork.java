package com.barchart.feed.series;

import org.joda.time.DateTime;

import rx.Subscription;


public class TestRxNetwork {
	public TestRxNetwork() {
		test2();
	}
	
	public void test() {
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
	
	public void test2() {
		final Test2 node = new Test2("BOB");
		final Test2 node2a = new Test2("BILL");
		final Test2 node2b = new Test2("DAVID");
		final Subscription n2aSub = node.subscribe(node2a);
		final Subscription n2bSub = node.subscribe(node2b);
		
		(new Thread() {
			Subscription local = n2aSub;
			public void run() {
				try {
					DateTime nextDate = new DateTime();
					while(true) {
						nextDate = nextDate.plusDays(1);
						Thread.sleep(5000);
						local.unsubscribe();
						node.onNext(nextDate);
						System.out.println("");
						
						nextDate = nextDate.plusDays(1);
						Thread.sleep(2000);
						local = node.subscribe(node2a);
						node.onNext(nextDate);
						System.out.println("");
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
