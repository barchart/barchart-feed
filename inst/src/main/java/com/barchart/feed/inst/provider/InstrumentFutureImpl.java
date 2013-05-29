/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.consumer.data.Instrument;
import com.barchart.feed.api.consumer.inst.InstrumentGUID;
import com.barchart.feed.api.framework.inst.InstrumentFuture;
import com.barchart.feed.api.framework.inst.MetadataContext;
import com.barchart.missive.api.Tag;
import com.barchart.missive.core.MissiveException;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Histogram;

public class InstrumentFutureImpl extends InstrumentBase implements InstrumentFuture {
	
	private static final Logger log = LoggerFactory
			.getLogger(InstrumentFutureImpl.class);

	private static final Histogram hist = Metrics.newHistogram(
			InstrumentFutureImpl.class, "LookupTime");
	
	private volatile Future<Instrument> task;
	
	private final InstrumentGUID guid;
	private final MetadataContext ctx;
	private volatile Instrument inst = null;
	private final long startTime;
	
	public InstrumentFutureImpl(final InstrumentGUID guid, final MetadataContext ctx, 
			final ExecutorService executor) {
		this.guid = guid;
		this.ctx = ctx;
		task = executor.submit(new LookupCallable(guid));
		startTime = System.currentTimeMillis();
	}
	
//	@Override
//	public <V> V get(final Tag<V> tag) throws MissiveException {
//		populate();
//		return inst.get(tag);
//	}
//
//	@Override
//	public boolean contains(final Tag<?> tag) {
//		populate();
//		return inst.contains(tag);
//	}

//	@Override
//	public Tag<?>[] tags() {
//		populate();
//		return inst.tags();
//	}
//
//	@Override
//	public int size() {
//		populate();
//		return inst.size();
//	}

	@Override
	public boolean isFrozen() {
		if(isDone() || isCancelled()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean cancel(final boolean mayInterruptIfRunning) {
		if(task.cancel(mayInterruptIfRunning)) {
			inst = Instrument.NULL_INSTRUMENT;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isCancelled() {
		return task.isCancelled();
	}

	@Override
	public boolean isDone() {
		return task.isDone();
	}

	@Override
	public Instrument get() throws InterruptedException, ExecutionException {
		return task.get();
	}

	@Override
	public Instrument get(final long timeout, final TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return task.get(timeout, unit);
	}
	
	private class LookupCallable implements Callable<Instrument> {

		final InstrumentGUID guid;
		
		LookupCallable(final InstrumentGUID guid) {
			this.guid = guid;
		}
		
		@Override
		public Instrument call() throws Exception {
			
			//TODO review 
//			final InstrumentEntity res = ctx.lookup(guid);
//			hist.update(System.currentTimeMillis() - startTime);
//			
//			return res;
			return null;
		}
		
	}
	
	private void populate() {
		
		if(!isDone() || !isCancelled()) {
			try {
				task.get();
			} catch (Exception e) {
				log.error(e.getMessage());
			} 
		}
		
	}

}
