package com.barchart.feed.series.analytics;

import com.barchart.feed.api.series.Span;

/**
 * Given by the formula:
 * 
 * Pivot Point (P) = (High + Low + Close)/3
 * Support 1 (S1) = (P x 2) - High
 * Support 2 (S2) = P  -  (High  -  Low)
 * Support 3 (S3) = High + 2 x (P - Low)
 * Resistance 1 (R1) = (P x 2) - Low
 * Resistance 2 (R2) = P + (High  -  Low)
 * Resistance 3 (R3) = Low + 2 x (High - P)
 * 
 * @author David Ray
 */
public class PivotPoint extends AnalyticBase {
	public static final String RESIST3 = "Resist3";
	public static final String RESIST2 = "Resist2";
	public static final String RESIST1 = "Resist1";
	public static final String PIVOT = "PivotPoint";
	public static final String SUPPORT1 = "Support1";
	public static final String SUPPORT2 = "Support2";
	public static final String SUPPORT3 = "Support3";
	
	private static final String[] INPUT_KEYS = new String[] { "Input" };
	private static final String[] OUTPUT_KEYS = 
		new String[] { RESIST3, RESIST2, RESIST1, PIVOT, SUPPORT1, SUPPORT2, SUPPORT3 };
	
	public PivotPoint() {}
	
	public PivotPoint(int numTimeFrames) {
	    
	}
	
	public static String[] getInputKeys() {
		return INPUT_KEYS;
	}

	public static String[] getOutputKeys() {
		return OUTPUT_KEYS;
	}

	@Override
	public Span process(Span span) {
		return null;
	}

    
}
