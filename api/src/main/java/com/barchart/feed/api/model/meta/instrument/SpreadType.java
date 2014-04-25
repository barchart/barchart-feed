/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.model.meta.instrument;

import com.barchart.util.common.math.MathExtra;
import com.barchart.util.value.api.Existential;

/**
 * Spread type.
 */
public enum SpreadType implements Existential {

	NULL("**", "Null"),

	DEFAULT("SP", "Standard"),

	GENERIC("GN", "Generic"),

	CRACK("CR", "Crack Spread"),

	XMAS_TREE("XT", "Xmas Tree"),

	STRADDLE_STRIP("SS", "Straddle Strips"),

	RISK_REVERSAL("RR", "Risk Reversal"),

	STRIP_SR("SR", "Strip"),

	IRON_CONDOR("IC", "Iron Condor"),

	HORIZ_STRADDLE("HS", "Horizontal Straddle"),

	DOUBLE("DB", "Double"),

	CONDOR_CO("CO", "Condor"),

	CONDITIONAL_CURVE("CC", "Conditional Curve"),

	BUTTERFLY_BO("BO", "Butterfly"),

	BOX("BX", "Box"),

	EQ_PUT_VERTICAL("VP", "Equity Put Vertical"),

	EQ_CALL_VERTICAL("VC", "Equity Call Vertical"),

	VERTICAL("VT", "Vertical"),

	STRANGLE("SG", "Strangle"),

	STRADDLE("ST", "Straddle"),

	HORIZ_CALENDAR("HO", "Horizontal Calendar"),

	DIAG_CALENDAR("DO", "Diagonal Calendar"),

	BUNDLE_SPREAD("BS", "Bundle Spread"),

	BUNDLE("FB", "Bundle"),

	PACK_SPREAD("PS", "Pack Spread"),

	DOUBLE_BUTTERFLY("DF", "Double Butterfly"),

	PACK_BUTTERFLY("PB", "Pack Butterfly"),

	MONTH_PACK("MP", "Month Pack"),

	PACK("PK", "Pack"),

	CRACK1("C1", "Crack"),

	INTERCOMMODITY("IS", "Inter-Commodity"),

	STRIP_FS("FS", "Strip"),

	CONDOR_CF("CF", "Condor"),

	BUTTERFLY_BF("BF", "Butterfly"),

	EQUITIES("EQ", "Equities"),

	REDUCED_TICK("RT", "Reduced Tick"),

	FOREX("FX", "Foreign Exchange"),

	THREE_WAY("3W", "3-Way"),

	RATIO_2_3("23", "Ratio 2x3"),

	RATIO_1_3("13", "Ratio 1x3"),

	RATIO_1_2("12", "Ratio 1x2"),

	UNKNOWN("??", "Unknown");

	public final byte ord;

	/** The code. */
	public final String code;
	public final String name;

	private SpreadType(final String code, final String name) {
		this.ord = (byte) ordinal();
		assert code.length() == 2;
		this.code = code;
		this.name = name;
	}

	private final static SpreadType[] ENUM_VALUES = values();

	/**
	 * Values unsafe.
	 *
	 * @return the dD f_ spread type[]
	 */
	@Deprecated
	public final static SpreadType[] valuesUnsafe() {
		return ENUM_VALUES;
	}

	static {
		// validate use of byte ord
		MathExtra.castIntToByte(ENUM_VALUES.length);
	}

	/**
	 * From ord.
	 *
	 * @param ord the ord
	 * @return the dD f_ spread type
	 */
	public final static SpreadType fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

	/**
	 * From code.
	 *
	 * @param code the code
	 * @return the dD f_ spread type
	 */
	public final static SpreadType fromCode(final String code) {
		for (final SpreadType known : ENUM_VALUES) {
			if (known.code == code) {
				return known;
			}
		}
		return UNKNOWN;
	}

	/**
	 * Checks if is known.
	 *
	 * @return true, if is known
	 */
	public final boolean isKnown() {
		return this != UNKNOWN;
	}

	@Override
	public boolean isNull() {
		return this == NULL;
	}

}
