package com.barchart.feed.api.data.primitive;

public interface SessionPrimitive {
	
	/**
	 * document primitive
	 */
	double openDouble();
	
	/**
	 * document primitive
	 */
	double highDouble();
	
	/**
	 * document primitive
	 */
	double lowDouble();

	/**
	 * document primitive
	 */
	double closeDouble();
	
	/**
	 * document primitive
	 */
	double settleDouble();
	
	/**
	 * document primitive
	 */
	long volumeLong();
	
	/**
	 * document primitive
	 */
	long openInterestLong();

	
}
