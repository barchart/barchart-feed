/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.bar.enums;

import static com.barchart.feed.base.market.enums.MarketEvent.NEW_BAR_CURRENT;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_BAR_CURRENT_EXT;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_BAR_CURRENT_NET;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_BAR_CURRENT_PIT;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_BAR_PREVIOUS;
import static com.barchart.feed.base.market.enums.MarketField.BAR_CURRENT;
import static com.barchart.feed.base.market.enums.MarketField.BAR_CURRENT_EXT;
import static com.barchart.feed.base.market.enums.MarketField.BAR_CURRENT_NET;
import static com.barchart.feed.base.market.enums.MarketField.BAR_CURRENT_PIT;
import static com.barchart.feed.base.market.enums.MarketField.BAR_PREVIOUS;

import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.values.api.Value;

/**
 * type of O-H-L-C market bars;
 * 
 * current = this trading day; previous = past trading day
 */
public enum MarketBarType implements Value<MarketBarType> {

	/** place holder for errors / defaults */
	NULL_BAR_TYPE(BAR_CURRENT, NEW_BAR_CURRENT),

	/* current */

	/** combo = net + pit */
	CURRENT(BAR_CURRENT, NEW_BAR_CURRENT),

	/** electronic */
	@Deprecated
	CURRENT_NET(BAR_CURRENT_NET, NEW_BAR_CURRENT_NET), //

	/** manual/pit */
	@Deprecated
	CURRENT_PIT(BAR_CURRENT_PIT, NEW_BAR_CURRENT_PIT), //

	/** extra, such as form-t */
	CURRENT_EXT(BAR_CURRENT_EXT, NEW_BAR_CURRENT_EXT), //

	/* previous */

	PREVIOUS(BAR_PREVIOUS, NEW_BAR_PREVIOUS), // combo

	;

	public final MarketField<MarketBar> field;

	public final MarketEvent event;

	MarketBarType(final MarketField<MarketBar> field, final MarketEvent event) {
		this.field = field;
		this.event = event;
	}

	private final static MarketBarType[] ENUM_VALUES = values();

	private final static int ENUM_SIZE = ENUM_VALUES.length;

	public static final int size() {
		return ENUM_SIZE;
	}

	@Override
	public MarketBarType freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NULL_BAR_TYPE;
	}

}
