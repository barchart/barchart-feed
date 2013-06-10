/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.inst.InstrumentFuture;
import com.barchart.util.concurrent.FutureCallback;

public class InstrumentFutureImpl implements InstrumentFuture {

	// TODO
	
	@Override
	public InstrumentFuture addResultListener(
			FutureCallback<Instrument> listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instrument getUnchecked() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Instrument get() throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instrument get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
