/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.barchart.feed.api.util.InstrumentGUID;

public interface SymbologyContext<V> {

	InstrumentGUID lookup(V symbol);
	
	Map<V, InstrumentGUID> lookup(Collection<? extends V> symbols);
	
	List<InstrumentGUID> search(CharSequence symbol);
	
	List<InstrumentGUID> search(CharSequence symbol, int limit, int offset);
	
}
