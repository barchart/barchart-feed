package com.barchart.feed.api.series.service;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.network.Assembler;

public interface SeriesFeedService {
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
