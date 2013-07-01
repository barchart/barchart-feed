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

import com.barchart.feed.api.util.Identifier;

public interface SymbologyContext<V> {

	Identifier lookup(V symbol);
	
	Map<V, Identifier> lookup(Collection<? extends V> symbols);
	
	List<Identifier> search(CharSequence symbol);
	
	List<Identifier> search(CharSequence symbol, int limit, int offset);
	
}
