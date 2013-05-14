package com.barchart.feed.api.data.client.simple;

public interface SessionSimple {
	
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
