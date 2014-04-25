package com.barchart.feed.inst.participant;

import com.barchart.feed.api.model.meta.Instrument;

/**
 * Market component which can handle instrument definition.
 */
public interface Instrumentable {

	/**
	 * Process incoming market instrument definition.
	 */
	void process(Instrument value);

}
