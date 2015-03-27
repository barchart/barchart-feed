package com.barchart.feed.meta.instrument;

import java.nio.channels.UnresolvedAddressException;

import org.openfeed.InstrumentDefinition;

import com.barchart.feed.api.model.meta.instrument.PriceFormat;
import com.barchart.util.value.api.Price;

public class DefaultPriceFormat implements PriceFormat {

	private final int denominator;
	private final int precision;
	private final boolean fractional;

	private final int subDenominator;
	private final int subPrecision;
	private final SubFormat subFormat;

	public DefaultPriceFormat(
			final int denominator_, 
			final int precision_, 
			final boolean fractional_) {
		this(denominator_, precision_, fractional_, 0, 0, SubFormat.FLAT);
	}

	public DefaultPriceFormat(
			final int denominator_, 
			final int precision_, 
			final boolean fractional_,
			final int subDenominator_, 
			final int subPrecision_, 
			final SubFormat subFormat_) {

		denominator = denominator_;
		precision = precision_;
		fractional = fractional_;
		subDenominator = subDenominator_;
		subPrecision = subPrecision_;
		subFormat = subFormat_;

	}
	
	public DefaultPriceFormat(final InstrumentDefinition.PriceFormat format) {
		
		if(format == null || !format.isInitialized()) {
			throw new IllegalArgumentException("Bad format proto");
		}
		
		denominator = format.getDenominator();
		precision = format.getPrecision();
		fractional = format.getIsFractional();
		subDenominator = format.getSubDenominator();
		subPrecision = format.getSubPrecision();
		
		switch(format.getSubFormat()) {
		default:
			throw new IllegalArgumentException("Unknown Price Format " + format.getSubFormat());
		case DECIMAL:
			subFormat = SubFormat.DECIMAL;
			break;
		case FLAT:
			subFormat = SubFormat.FLAT;
			break;
		case FRACTIONAL:
			subFormat = SubFormat.FRACTIONAL;
			break;
			
		}
		
	}

	@Override
	public int denominator() {
		return denominator;
	}

	@Override
	public int precision() {
		return precision;
	}

	@Override
	public boolean fractional() {
		return fractional;
	}

	@Override
	public int subDenominator() {
		return subDenominator;
	}

	@Override
	public int subPrecision() {
		return subPrecision;
	}

	@Override
	public SubFormat subFormat() {
		return subFormat;
	}

	@Override
	public String format(final Price price) {
		// TODO
		return null;
	}

	@Override
	public Price parse(final String formatted) {
		// TODO
		return Price.NULL;
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
