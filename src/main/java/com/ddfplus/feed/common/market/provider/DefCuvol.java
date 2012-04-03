package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;

@NotMutable
class DefCuvol extends NulCuvol {

	private final SizeValue[] entries;

	private final PriceValue priceFirst;
	private final PriceValue priceStep;

	DefCuvol(final SizeValue[] entries, final PriceValue priceFirst,
			final PriceValue priceStep) {

		assert entries != null;
		assert priceFirst != null;
		assert priceStep != null;
		assert priceStep.mantissa() != 0;

		this.entries = entries;
		this.priceFirst = priceFirst;
		this.priceStep = priceStep;

	}

	@Override
	public PriceValue priceFirst() {
		return priceFirst;
	}

	@Override
	public PriceValue priceStep() {
		return priceStep;
	}

	@Override
	public SizeValue[] entries() {
		return entries;
	}

}
