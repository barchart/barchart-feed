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

import com.barchart.feed.api.model.meta.Instrument;

public interface InstrumentService<V> {
	
	List<Instrument> lookup(V symbol);
	
	InstrumentFuture lookupAsync(V symbol);
	
	Map<V, List<Instrument>> lookup(Collection<? extends V> symbols);
	
	InstrumentFutureMap<V> lookupAsync(Collection<? extends V> symbols);
	
}
