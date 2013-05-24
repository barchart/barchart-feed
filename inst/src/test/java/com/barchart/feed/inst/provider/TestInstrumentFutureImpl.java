/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import java.util.concurrent.ExecutionException;

import com.barchart.feed.api.framework.inst.InstrumentFuture;

public class TestInstrumentFutureImpl {

	public static final int NUM_RUNS = 1000 * 1000;
	
	public static void main(final String[] args) throws InterruptedException, ExecutionException {
		
		final MockDefinitionService defService = new MockDefinitionService();
		
		/* Blocking test */
		for(int i = 0; i < NUM_RUNS; i++) {
			
			InstrumentFuture inst = defService.lookupAsync(MockDefinitionService.INST_SYMBOL_1);
			inst.get();
			
		}
		
		
	}
	
}
