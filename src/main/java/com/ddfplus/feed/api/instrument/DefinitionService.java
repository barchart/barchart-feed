package com.ddfplus.feed.api.instrument;

import com.barchart.util.values.api.TextValue;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;

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
