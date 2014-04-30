package com.barchart.feed.meta.service;

import com.barchart.feed.api.model.meta.Vendor;
import com.barchart.feed.api.model.meta.id.ExchangeID;
import com.barchart.feed.api.model.meta.id.VendorID;

public class DefaultLookupSymbol implements LookupSymbol {

	private final VendorID vendor;
	private final ExchangeID exchange;
	private final String symbol;

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

		if (symbol_ == null)
			throw new IllegalArgumentException("Symbol cannot be null");

		vendor = vendor_;
		exchange = exchange_;
		symbol = symbol_;

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
						ExchangeID.NULL,
						parts[0].toUpperCase());

			case 2:
				return new DefaultLookupSymbol(
						defaultVendor,
						new ExchangeID(parts[0].toUpperCase()),
						parts[1].toUpperCase());

			case 3:
				return new DefaultLookupSymbol(
						new VendorID(parts[0].toUpperCase()),
						new ExchangeID(parts[1].toUpperCase()),
						parts[2].toUpperCase());

			default:
				throw new IllegalArgumentException("Symbol format not recognized: " + symbol);

		}

	}

	@Override
	public boolean equals(final Object that) {

		if (that instanceof LookupSymbol) {

			final LookupSymbol lookup = (LookupSymbol) that;

			return (lookup.vendor() == vendor || (vendor != null && vendor.equals(lookup.vendor())))
					&& (lookup.exchange() == exchange || (exchange != null && exchange.equals(lookup.exchange())))
					&& symbol.equals(lookup.symbol());

		}

		return false;

	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return (vendor != null ? vendor.toString() : "*") + ":"
				+ (exchange != null ? exchange.toString() : "*") + ":"
				+ symbol;
	}

}
