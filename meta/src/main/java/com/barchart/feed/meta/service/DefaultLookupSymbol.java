package com.barchart.feed.meta.service;

import com.barchart.feed.api.model.meta.Vendor;
import com.barchart.feed.api.model.meta.id.ExchangeID;
import com.barchart.feed.api.model.meta.id.VendorID;

public class DefaultLookupSymbol implements LookupSymbol, Comparable<LookupSymbol> {

	private final VendorID vendor;
	private final ExchangeID exchange;
	private final String symbol;

	private final String fullSymbol;

	/**
	 * Representation of a symbol on any exchange from the default data vendor.
	 */
	public DefaultLookupSymbol(final String symbol_) {
		this(Vendor.DEFAULT.id(), null, symbol_);
	}

	/**
	 * Representation of a symbol on the given exchange from the default data
	 * vendor.
	 */
	public DefaultLookupSymbol(final ExchangeID exchange_, final String symbol_) {
		this(Vendor.DEFAULT.id(), exchange_, symbol_);
	}

	/**
	 * Representation of a symbol on the given exchange from the default data
	 * vendor.
	 */
	public DefaultLookupSymbol(final String exchangeMic_, final String symbol_) {
		this(Vendor.DEFAULT.id(), new ExchangeID(exchangeMic_), symbol_);
	}

	/**
	 * Representation of a symbol on the given exchange from the specified data
	 * vendor.
	 */
	public DefaultLookupSymbol(final VendorID vendor_, final ExchangeID exchange_, final String symbol_) {

		if (symbol_ == null || symbol_.isEmpty())
			throw new IllegalArgumentException("Symbol cannot be empty");

		vendor = vendor_ == null ? VendorID.NULL : vendor_;
		exchange = exchange_ == null ? ExchangeID.NULL : exchange_;
		symbol = symbol_;

		fullSymbol = (vendor != null ? vendor.toString() : "*") + ":"
				+ (exchange != null ? exchange.toString() : "*") + ":"
				+ symbol;
	}

	@Override
	public VendorID vendor() {
		return vendor;
	}

	@Override
	public ExchangeID exchange() {
		return exchange;
	}

	@Override
	public String symbol() {
		return symbol;
	}

	public static DefaultLookupSymbol parse(final String symbol) {
		return parse(symbol, Vendor.DEFAULT.id());
	}

	public static DefaultLookupSymbol parse(final String symbol, final VendorID defaultVendor) {

		final String[] parts = symbol.split(":");

		switch (parts.length) {

			case 1:
				return new DefaultLookupSymbol(
						defaultVendor,
						null,
						parts[0].toUpperCase());

			case 2:
				return new DefaultLookupSymbol(
						defaultVendor,
						exchange(parts[0]),
						parts[1].toUpperCase());

			case 3:
				return new DefaultLookupSymbol(
						new VendorID(parts[0].toUpperCase()),
						exchange(parts[1]),
						parts[2].toUpperCase());

			default:
				throw new IllegalArgumentException("Symbol format not recognized: " + symbol);

		}

	}

	private static ExchangeID exchange(final String in) {

		if (in == null || in.isEmpty() || "*".equals(in))
			return null;

		return new ExchangeID(in.toUpperCase());

	}

	@Override
	public boolean equals(final Object that) {
		return that instanceof LookupSymbol && that.toString().equals(fullSymbol);
	}

	@Override
	public int hashCode() {
		return fullSymbol.hashCode();
	}

	@Override
	public String toString() {
		return fullSymbol;
	}

	@Override
	public int compareTo(final LookupSymbol that) {
		return toString().compareTo(that.toString());
	}

}
