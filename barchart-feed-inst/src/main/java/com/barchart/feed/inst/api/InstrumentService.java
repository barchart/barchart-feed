package com.barchart.feed.inst.api;

import com.barchart.osgi.factory.api.Fidget;

public interface InstrumentService extends Fidget {
	
	/**
	 * 
	 * @param id
	 * @param context
	 * @return
	 */
	Instrument lookup(String id, SymbologyContext context);
	
	/**
	 * 
	 * @param guid
	 * @return
	 */
	Instrument lookup(InstrumentGUID guid);

}
