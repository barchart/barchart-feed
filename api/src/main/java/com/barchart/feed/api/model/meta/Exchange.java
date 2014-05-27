package com.barchart.feed.api.model.meta;

import org.joda.time.DateTimeZone;

import com.barchart.feed.api.model.meta.id.ExchangeID;

/**
 * Exchange definition.
 */
public interface Exchange extends Metadata {

	/**
	 * The unique exchange ID. This will usually be based on the MIC.
	 */
	ExchangeID id();

	/**
	 * The MIC as a string.
	 */
	String mic();

	/**
	 * The country that this exchange is based in.
	 */
	String countryCode();

	/**
	 * The primary currency that this exchange trades in.
	 */
	String currencyCode();

	/**
	 * The time zone of the exchange's location. May be different than the
	 * timezone individual instruments on the exchange trade in.
	 */
	DateTimeZone timeZone();

	/**
	 * The standard delay, in seconds, for delayed data from this exchange. 0
	 * means delayed data is not allowed.
	 */
	int standardDelay();

	@Deprecated
	String timeZoneName();

	Exchange NULL = new Exchange() {

		@Override
		public MetaType type() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ExchangeID id() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String mic() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String countryCode() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String currencyCode() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public DateTimeZone timeZone() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String timeZoneName() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String description() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int standardDelay() {
			throw new UnsupportedOperationException();
		}

	};

}
