package com.barchart.feed.api.series.services;

import com.barchart.feed.api.model.meta.Instrument;

public interface FeedMonitorService {
	/**
	 * Stores the reference to the {@link Assembler} specified and activates
	 * its input data feed.
	 * 
	 * @param	assembler		the {@link Assembler} to register.
	 */
	public void registerAssembler(Assembler assembler);
	
	/**
	 * Removes the reference to the {@link Assembler} specified and deactivates
	 * its input data feed.
	 * 
	 * @param	assembler		the {@link Assembler} to register.
	 */
	public void unregisterAssembler(Assembler assembler);
	
	/**
	 * Returns the {@link Instrument} object corresponding to the
	 * specified symbol string.
	 * 
	 * @param symbol	The symbol
	 * @return			the {@link Instrument} object corresponding to the
	 * 					specified symbol string.
	 */
	public Instrument lookupInstrument(String symbol);
}
