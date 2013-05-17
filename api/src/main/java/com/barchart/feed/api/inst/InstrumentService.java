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

import javax.sound.midi.Instrument;

public interface InstrumentService<V> {
	
	Instrument lookup(V symbol);
	
	Future<Instrument> lookupAsync(V symbol);
	
	Map<V, Instrument> lookup(Collection<? extends V> symbols);
	
	Map<V, Future<Instrument>> lookupAsync(Collection<? extends V> symbols);
	
}
