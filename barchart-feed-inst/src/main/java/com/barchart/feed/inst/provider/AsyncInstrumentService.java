package com.barchart.feed.inst.provider;

import java.util.concurrent.Executor;

import com.barchart.feed.api.inst.InstrumentFuture;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.feed.api.inst.MetadataContext;

public class AsyncInstrumentService {

	private final MetadataContext ctx;
	private final Executor executor;
	
	public AsyncInstrumentService(final MetadataContext ctx, final Executor executor) {
		
		this.ctx = ctx;
		this.executor = executor;
		
		
	}
	
	public InstrumentFuture lookupAsync(final InstrumentGUID guid) {
		
		
		
		return null;
		
	}
	
	
}
