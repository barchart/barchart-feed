package com.barchart.feed.series;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class Test2 implements Observer<DateTime> {
	private boolean isEnabled = true;
	
	private final String name;
	
	private DateTime dateModified;
	private DateTime nextDate;
	
	private Observable.OnSubscribeFunc<DateTime> function;
	private Observable<DateTime> observable;
	
	private Object waitLock = new Object();
	
	//private final Map<Thingy2, Observer<? super DateTime>> iter = new ConcurrentHashMap<Thingy2, Observer<? super DateTime>>();
	private final Map<Thingy2, Test2> nextNodes = new ConcurrentHashMap<Thingy2, Test2>();
	
	private final Thread EXEC_THREAD;
	
	Test2(String name) {
		this.name = name;
		EXEC_THREAD = init(name);
	}
	
	public String getName() {
		return name;
	}
	
	public Subscription subscribe(Test2 observer) {
		if(!EXEC_THREAD.isAlive()) {
			EXEC_THREAD.start();
			
			function = createOnSubscribeFunction(); 
			observable = Observable.create(function);
		}
		
		System.out.println("subscribing " + observer.getName() + " to node: " + name);
		
		Thingy2 s = new Thingy2(Test2.this);
		nextNodes.put(s, observer);
		
		return s;//observable.subscribe(observer);
	}
	
	public void unsubscribe(Thingy2 t) {
		System.out.println("un-subscribing " + lookup(t).getName() + " from node: " + name);
//		iter.remove(t);
		nextNodes.remove(t);
	}
	
	public Test2 lookup(Thingy2 sub) {
		return nextNodes.get(sub);
	}
	
	@Override
	public void onCompleted() {
		System.out.println("onCompleted called");
	}

	@Override
	public void onError(Throwable arg0) {
		arg0.printStackTrace();
	}

	@Override
	public void onNext(DateTime arg0) {
		System.out.println(getName() + " received update: " + arg0);
		
		nextDate = arg0;
		synchronized(waitLock) {
			try {
				waitLock.notify();
			}catch(Exception e) {
				System.out.println("exception on thread: " + Thread.currentThread().getName());
				e.printStackTrace();
			}
		}
	}
	
	public Observable.OnSubscribeFunc<DateTime> createOnSubscribeFunction() {
		return new Observable.OnSubscribeFunc<DateTime>() {
			@Override
			public Subscription onSubscribe(Observer<? super DateTime> t1) {
				Thingy2 s = new Thingy2(Test2.this);
//				iter.put(s, t1);
				
				return s;
			}
		};
	}
	
	private Thread init(final String name) {
		return new Thread(name) {
			public void run() {
				try {
					int i = 0;
					while(isEnabled) {
						++i;
						
						if(dateModified == null) {
							dateModified = nextDate = new DateTime();
						}
						
						for(Observer<? super DateTime> o : nextNodes.values()) {
							o.onNext(nextDate);
						}
						
						dateModified = nextDate;
						
						synchronized(waitLock) {
							waitLock.wait();
						}
					}
				}catch(Exception e) {
					//e.printStackTrace();
					System.out.println("was interrupted");
				}
			}
		};
	}
	
}