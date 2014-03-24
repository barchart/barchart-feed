package com.barchart.feed.inst.participant;

import org.openfeed.InstrumentDefinition;

/**
 * Market component which can handle instrument definition.
 */
public interface Instrumentable {
	
	/**
	 * Process incoming market instrument definition.
	 */
	void process(InstrumentDefinition value);

	/**
	 * Generate market instrument definition snapshot.
	 */
	InstrumentDefinition definition();

}
