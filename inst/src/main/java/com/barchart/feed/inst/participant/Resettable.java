package com.barchart.feed.inst.participant;

import com.barchart.util.common.anno.aQute.bnd.annotation.ProviderType;

/**
 * Reset mutable object to a known state.
 */
@ProviderType
public interface Resettable {
	
	/**
	 * Reset mutable object to a known state.
	 */
	void reset();

}
