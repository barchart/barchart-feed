package com.ddfplus.feed.common.util.collections;

// enum represented as long bit set
public interface BitSetEnum<E> {

	long NUL = 0L;

	long ONE = 1L;

	// max enum size
	int LIMIT = 64;

	//

	/** ENUM create order */
	int ordinal();

	//

	/**
	 * implements must provide:
	 * 
	 * private final long mask = ONE << index();
	 * 
	 * public final long mask() { return mask; }
	 * 
	 **/
	long mask();

}
