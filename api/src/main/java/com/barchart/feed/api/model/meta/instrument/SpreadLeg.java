package com.barchart.feed.api.model.meta.instrument;

import com.barchart.feed.api.model.meta.id.InstrumentID;

/**
 * Represents a leg of a multi-instrument spread.
 */
public interface SpreadLeg {

	/**
	 * The instrument for this leg.
	 */
	InstrumentID instrument();

	/**
	 * The multiplier ratio for this leg. Can be negative.
	 *
	 * For example, a spread with legs of (ZCZ14, -2) and (ZCU14, 1) would be
	 * computed as:
	 *
	 * ZCU14 - (2 * ZCZ14)
	 */
	int ratio();

}
