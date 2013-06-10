/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.inst;

import java.util.Collection;
import java.util.Map;

import com.barchart.feed.api.data.Instrument;

public interface InstrumentService<V> {
	
	Instrument lookup(V symbol);
	
	InstrumentFuture lookupAsync(V symbol);
	
	Map<V, Instrument> lookup(Collection<? extends V> symbols);
	
	InstrumentFutureMap<V> lookupAsync(Collection<? extends V> symbols);
	
}
