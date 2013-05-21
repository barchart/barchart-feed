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
import java.util.concurrent.Future;

import com.barchart.feed.api.data.InstrumentEntity;

public interface InstrumentService<V> {
	
	InstrumentEntity lookup(V symbol);
	
	Future<InstrumentEntity> lookupAsync(V symbol);
	
	Map<V, InstrumentEntity> lookup(Collection<? extends V> symbols);
	
	Map<V, Future<InstrumentEntity>> lookupAsync(Collection<? extends V> symbols);
	
}
