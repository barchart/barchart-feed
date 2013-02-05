package com.barchart.feed.inst.provider;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.inst.InstrumentFuture;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.feed.api.inst.MetadataContext;
import com.barchart.missive.core.MissiveException;
import com.barchart.missive.core.Tag;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Histogram;

public class InstrumentFutureImpl extends InstrumentBase implements InstrumentFuture {
	
	private static final Logger log = LoggerFactory
			.getLogger(InstrumentFutureImpl.class);

	private static final Histogram hist = Metrics.newHistogram(
			InstrumentFutureImpl.class, "LookupTime");
	
	private volatile FutureTask<Instrument> task;
	
	private final InstrumentGUID guid;
	private final MetadataContext ctx;
	private volatile Instrument inst = null;
	private final long startTime;
	
	public InstrumentFutureImpl(final InstrumentGUID guid, final MetadataContext ctx, 
			final Executor executor) {
		this.guid = guid;
		this.ctx = ctx;
		task = new FutureTask<Instrument>(lookup, this);
		startTime = System.currentTimeMillis();
		executor.execute(task);
	}
	
	@Override
	public InstrumentGUID getGUID() {
		return guid;
	}

	@Override
	public <V> V get(final Tag<V> tag) throws MissiveException {
		populate();
		return inst.get(tag);
	}

	@Override
	public boolean contains(final Tag<?> tag) {
		populate();
		return inst.contains(tag);
	}

	@Override
	public Tag<?>[] tags() {
		populate();
		return inst.tags();
	}

	@Override
	public int size() {
		populate();
		return inst.size();
	}

	@Override
	public Instrument freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		if(isDone() || isCancelled()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isNull() {
		return inst == Instrument.NULL_INSTRUMENT;
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
	
	private Runnable lookup = new Runnable() {

		@Override
		public void run() {
			inst = ctx.lookup(guid);
			hist.update(System.currentTimeMillis() - startTime);
		}
		
	};
	
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
