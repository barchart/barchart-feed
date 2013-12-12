package com.barchart.feed.base.provider;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestSymbology {
	
	@Test
	public void testHistorical() {
		
		/* INDEX */
		assertTrue(Symbology.formatHistoricalSymbol("$DOWI").equals("$DOWI"));
		
		/* EQUITY */
		assertTrue(Symbology.formatHistoricalSymbol("GOOG").equals("GOOG"));
		
		/* FOREX */
		assertTrue(Symbology.formatHistoricalSymbol("^USDEUR").equals("^USDEUR"));
		
		/* FUTURE */
		assertTrue(Symbology.formatHistoricalSymbol("CLZ4").equals("CLZ14"));
		assertTrue(Symbology.formatHistoricalSymbol("CLZ14").equals("CLZ14"));
		assertTrue(Symbology.formatHistoricalSymbol("CLZ2014").equals("CLZ14"));
	
	}

}
