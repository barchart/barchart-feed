package com.barchart.feed.series;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Observer;
import rx.util.functions.Function;

public class Test<T> extends Observable<T> implements Observer<T> {
	private boolean isEnabled = true;
	
	private final String name;
	
	private final Map<Thingy, Test<T>> nextNodes = new ConcurrentHashMap<Thingy, Test<T>>();
	
	private final Thread EXEC_THREAD;
	
	public Test(String name) {
		super(null);
		lazyInitSubscribeHandler();
		
		this.name = name;
		EXEC_THREAD = init(name);
	}
	
	public String getName() {
		return name;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public rx.Subscription subscribe(Observer<? super T> observer) {
		if(!EXEC_THREAD.isAlive()) {
			EXEC_THREAD.start();
		}
		
		System.out.println("subscribing " + ((Test<T>)observer).getName() + " to node: " + name);
		return super.subscribe(observer);
	}
	
	public void unsubscribe(Thingy s) {
		System.out.println("unsubscribing " + nextNodes.get(s).getName() + " from node " + getName());
		nextNodes.remove(s);
	}
	
	public Test<T> lookup(Thingy sub) {
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
	public void onNext(T arg0) {
		System.out.println(getName() + " received update: " + arg0);
	}
	
	private void lazyInitSubscribeHandler() {
		try {
			Field f = Test.class.getSuperclass().getDeclaredField("onSubscribe");
			f.setAccessible(true);
			f.set(this, createOnSubscribeFunction());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Function createOnSubscribeFunction() {
		return new Observable.OnSubscribeFunc<Test<T>>() {
			@Override
			public Thingy onSubscribe(Observer<? super Test<T>> arg0) {
				Thingy s = new Thingy(Test.this);
				Test<T> n = null;
				try {
					Field f = arg0.getClass().getDeclaredField("actual");
					f.setAccessible(true);
					n = (Test<T>)f.get(arg0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				nextNodes.put(s, n);
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
						for(Observer<T> o : nextNodes.values()) {
							o.onNext((T)("" + i));
						}
						Thread.sleep(2000);
					}
				}catch(Exception e) {
					//e.printStackTrace();
					System.out.println("was interrupted");
				}
			}
		};
	}
	
}