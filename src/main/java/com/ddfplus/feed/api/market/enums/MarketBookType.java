package com.ddfplus.feed.api.market.enums;

import com.barchart.util.math.MathExtra;
import com.barchart.util.values.api.Value;

public enum MarketBookType implements Value<MarketBookType> {

	/** no size book */
	EMPTY, //

	/** only default sizes */
	DEFAULT, //

	/** only implied sizes */
	IMPLIED, //

	/** both default + implied sizes */
	COMBO, //

	;

	public final byte ord = (byte) ordinal();

	private static final MarketBookType[] ENUM_VALUES = values();

	public static final MarketBookType fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

	static {
		// validate use of byte ordinal
		MathExtra.castIntToByte(ENUM_VALUES.length);
	}

	@Override
	public MarketBookType freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == EMPTY;
	}

}
