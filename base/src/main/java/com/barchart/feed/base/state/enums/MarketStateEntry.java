/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.state.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.collections.BitSetEnum;

public enum MarketStateEntry implements BitSetEnum<MarketStateEntry> {

	/**
	 * @deprecated IS_SETTLED should be inferred from the values in
	 * the CURRENT and PREVIOUS bars.
	 */
	@Deprecated IS_SETTLED,        	// Generic Settled Flag
	IS_PUBLISH_REALTIME_SNAPSHOT, // Publishing Realtime, or Other
	IS_PUBLISH_REALTIME,  			// Publishing Realtime, or Other
	IS_PUBLISH_DELAYED,

	;

	private static final Logger log = LoggerFactory
			.getLogger(MarketStateEntry.class);

	private final long mask = ONE << ordinal();

	@Override
	public final long mask() {
		return mask;
	}

	private static final MarketStateEntry[] ENUM_VALUES = values();

	private static final int ENUM_SIZE = ENUM_VALUES.length;

	public static final int size() {
		return ENUM_SIZE;
	}

	static {
		log.debug("market state count : {}", ENUM_SIZE);
	}

	/** convenience array wrapper */
	public static MarketStateEntry[] in(final MarketStateEntry... events) {
		return events;
	}

}
