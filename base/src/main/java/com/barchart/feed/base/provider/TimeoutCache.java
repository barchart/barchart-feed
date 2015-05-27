package com.barchart.feed.base.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.common.identifier.Identifier;

public class TimeoutCache<T extends Identifier<?, ?>> {
	
	protected static final Logger log = LoggerFactory.getLogger(TimeoutCache.class);

	private final ConcurrentMap<T, Integer> timers = new ConcurrentHashMap<T, Integer>();
	private final ConcurrentMap<T, Runnable> tasks = new ConcurrentHashMap<T, Runnable>();
	
	private final int timeout;
	
	public TimeoutCache(final int seconds) {
		this.timeout = seconds;
		
		final Thread checker = new Thread(new Runnable() {

			@Override
			public void run() {
				
				long lastTime = System.currentTimeMillis();
				
				final List<T> toRemove = new ArrayList<T>();
				
				while(true) {
					
					/* Decrement all timers */
					for(final Entry<T, Integer> e : timers.entrySet()) {
						e.setValue(e.getValue() - 1);
						
						/* Check for timeouts */
						if(e.getValue() <= 0) {
							toRemove.add(e.getKey());
						}
					}
					
					/* Clean out timed out keys */
					for(final T key : toRemove) {
						
						timers.remove(key);
						final Runnable runner = tasks.remove(key);
						
						if(runner != null) {
							runner.run();
						}
						
					}
					
					toRemove.clear();
					
					/* Sleep until next second */
					try {
						final long curTime = System.currentTimeMillis();
						final long timeout = Math.max(1000, lastTime + 2000 - curTime);
						lastTime = curTime;
						Thread.sleep(timeout);
					} catch (final InterruptedException e1) {
						break;
					} 
					
				}
				
				log.warn("Timeout Cache Thread Interrupted");
				
			}
			
		});
		
		checker.setDaemon(true);
		checker.start();
		
	}

	public void put(final T key, final Runnable task) {
		
		timers.putIfAbsent(key, timeout + 1);
		tasks.putIfAbsent(key, task);
		
	}
	
	public void remove(final T key) {
		
		timers.remove(key);
		tasks.remove(key);
		
	}
	
	public int size() {
		return tasks.size();
	}
	
}
