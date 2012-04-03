/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.api.instrument;

import com.barchart.feed.base.api.instrument.values.MarketInstrument;
import com.barchart.util.values.api.TextValue;

/**
 * resolve text symbol names into objects;
 */
public interface DefinitionService<T extends MarketInstrument> {

	/**
	 * perform symbol name resolution;
	 * 
	 * @return NULL_INSTRUMENT if symbol is not found;
	 **/
	T lookup(TextValue id);

	/** clear lookup cache; */
	void clear();

	/* TODO */
	// void cacheExpiration(policy);

}
