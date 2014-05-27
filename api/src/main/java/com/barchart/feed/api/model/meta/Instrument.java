package com.barchart.feed.api.model.meta;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.barchart.feed.api.model.meta.id.ChannelID;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.model.meta.instrument.Calendar;
import com.barchart.feed.api.model.meta.instrument.PriceFormat;
import com.barchart.feed.api.model.meta.instrument.Schedule;
import com.barchart.feed.api.model.meta.instrument.SpreadLeg;
import com.barchart.feed.api.model.meta.instrument.SpreadType;
import com.barchart.util.value.api.Existential;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;

public interface Instrument extends Comparable<Instrument>, Metadata {

	enum BookStructureType {

		/** no size book */
		NONE, //

		/**  */
		PRICE_LEVEL, //

		/**  */
		PRICE_VALUE, //

		/**  */
		ORDER_NUMBER, //

		;

		public final byte ord = (byte) ordinal();

		private static final BookStructureType[] ENUM_VALUES = values();

		public static final BookStructureType fromOrd(final byte ord) {
			return ENUM_VALUES[ord];
		}

	}

	enum BookLiquidityType {

		/** no size book */
		NONE, //

		/** only default sizes */
		DEFAULT, //

		/** only implied sizes */
		IMPLIED, //

		/** both default + implied sizes */
		COMBINED, //

		;

		public final byte ord = (byte) ordinal();

		private static final BookLiquidityType[] ENUM_VALUES = values();

		public static final BookLiquidityType fromOrd(final byte ord) {
			return ENUM_VALUES[ord];
		}

		public static final BookLiquidityType fromText(final String type) {
			for (final BookLiquidityType t : values()) {
				if (type.compareTo(t.name()) == 0) {
					return t;
				}
			}
			return NONE;
		}

	}

	enum SecurityType {

		NULL_TYPE("~"), //
		FOREX("MRC"), //
		INDEX("MRI"), //
		EQUITY("E"), //
		FUTURE("F"), //
		OPTION("O"), //
		SPREAD("FC"), // Invalid CFI code, won't match things
		;

		private final String cfi;

		private SecurityType(final String cfi) {
			this.cfi = cfi;
		}

		public static SecurityType fromCFI(final String code) {

			if (code.length() != 6) {
				return NULL_TYPE;
			}

			for (final SecurityType t : values()) {
				if (code.startsWith(t.cfi)) {
					return t;
				}
			}

			return NULL_TYPE;
		}

	}

	enum State {

		ACTIVE, PASSIVE

	};

	enum OptionType implements Existential {

		NULL, PUT, CALL;

		@Override
		public boolean isNull() {
			return this == NULL;
		}

	}

	enum OptionStyle implements Existential {

		NULL, DEFAULT, AMERICAN, EUROPEAN;

		@Override
		public boolean isNull() {
			return this == NULL;
		}

	}

	/**
	 * An immutable ID for this instrument that is globally unique across all
	 * vendors and exchanges.
	 */
	InstrumentID id();

	/**
	 * The instrument security type (stock, future, etc)
	 */
	SecurityType securityType();

	/**
	 * Order book liquidity type.
	 */
	BookLiquidityType liquidityType();

	/**
	 * Structure of order book (price level, order ID, etc).
	 */
	BookStructureType bookStructure();

	/**
	 * Max book depth for price level structure.
	 */
	Size maxBookDepth();

	/**
	 * The vendor that provides the underlying data for this instrument.
	 */
	VendorID vendor();

	/**
	 * The normalized symbol representing this instrument in the primary
	 * vendor's symbology.
	 */
	String symbol();

	/**
	 * A list of alternate symbology from other vendors that represent the same
	 * security as this instrument.
	 */
	Map<VendorID, String> vendorSymbols();

	/**
	 * The instrument name/description.
	 */
	@Override
	String description();

	/**
	 * CFI classification for this instrument.
	 * http://en.wikipedia.org/wiki/ISO_10962
	 */
	String CFICode();

	/**
	 * The primary currency this instrument trades and quotes in.
	 * http://en.wikipedia.org/wiki/ISO_4217
	 */
	String currencyCode();

	/**
	 * The MIC code for the exchange this instrument trades on.
	 * http://en.wikipedia.org/wiki/ISO_10383
	 */
	String exchangeCode();

	/**
	 * The logical group an instrument belongs to (i.e. a futures root code)
	 */
	String instrumentGroup();

	/**
	 * The current trading state of this instrument. ACTIVE means the symbol is
	 * currently trading and has live price data, PASSIVE means the symbol is
	 * expired or delisted and only historical data is available.
	 */
	State state();

	/**
	 * The channel that data for this instrument is broadcast over. This is
	 * vendor-specific.
	 */
	ChannelID channel();

	/**
	 * The minimum price movement for this instrument.
	 */
	Price tickSize();

	/**
	 * The real value of a price change of one, in the traded currency.
	 */
	Price pointValue();

	/**
	 * For some contracts (e.g. SI, HG), the exchange will report transaction
	 * prices to counterparties in different units than those reported in
	 * general market data. For example, prices for silver futures are reported
	 * in dollars, but the fill prices in FIX messages are in cents. For most
	 * contracts this value will be 1.
	 */
	Price transactionPriceConversionFactor();

	/**
	 * The date this instrument record was created.
	 */
	DateTime created();

	/**
	 * The date this instrument record was updated.
	 */
	DateTime updated();

	/**
	 * A calendar of trading events, containing notable events in an
	 * instrument's lifecycle (first trade date, last trade date / expiration,
	 * etc.)
	 */
	Calendar calendar();

	/**
	 * A typical schedule of weekly trading sessions for this instrument.
	 */
	Schedule schedule();

	/**
	 * The time zone that this instrument trades in.
	 */
	DateTimeZone timeZone();

	/**
	 * The standard display format specification for quoted prices.
	 */
	PriceFormat priceFormat();

	/**
	 * The standard display format specification for options strike prices on
	 * this instrument.
	 */
	PriceFormat optionStrikePriceFormat();

	/*
	 * Synthetic instrument fields
	 */

	/**
	 * If this is a synthetic instrument, returns a list of component
	 * instruments that contribute to this market. This will be an empty list
	 * for most instruments.
	 */
	List<InstrumentID> components();

	/*
	 * Futures-specific fields
	 */

	/**
	 * A delivery date for an instrument that is linked to a physical commodity.
	 * This is primarily for month display purposes for futures, and may be
	 * null.
	 *
	 * This is an alias for the LAST_DELIVERY_DATE event in {@link #calendar()}.
	 * To get the full delivery date range, see {@link #calendar()}.
	 */
	DateTime delivery();

	/**
	 * The instrument expiration date. May be null.
	 *
	 * This is an alias for the LAST_TRADE_DATE event in {@link #calendar()}.
	 */
	DateTime expiration();

	/*
	 * Options-specific fields
	 */

	/**
	 * Option underlier instrument.
	 */
	InstrumentID underlier();

	/**
	 * Option strike price in market currency.
	 */
	Price strikePrice();

	/**
	 * Option type: call vs put.
	 */
	OptionType optionType();

	/**
	 * Option style : American vs European.
	 */
	OptionStyle optionStyle();

	/*
	 * Spread-specific fields
	 */

	/**
	 * Spread type.
	 */
	SpreadType spreadType();

	/**
	 * Spread legs. Only applies to spread instruments, not to be confused with
	 * synthetic instrument {@link #components()}.
	 */
	List<SpreadLeg> spreadLegs();

	/*
	 * Deprecated stuff that may be supported for a time but is already removed
	 * from InstrumentDefinition.
	 */

	@Deprecated
	enum Month {
		NULL_MONTH, JANUARY, FEBRUARY, MARCH, APRIL, MAY,
		JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMEBR, DECEMBER
	}

	@Deprecated
	String marketGUID();

	/**
	 * @see #priceFormat()
	 */
	@Deprecated
	Fraction displayFraction();

	/**
	 * @see #schedule()
	 */
	@Deprecated
	Schedule marketHours();

	/**
	 * @see #calendar()
	 */
	@Deprecated
	TimeInterval lifetime();

	/**
	 * @see #calendar()
	 */
	@Deprecated
	Time contractExpire();

	/**
	 * @see #symbolExpiration()
	 */
	@Deprecated
	Month contractDeliveryMonth();

	/**
	 * Exchanges now need to be looked up separately based on the MIC code
	 */
	@Deprecated
	Exchange exchange();

	/**
	 * @see #timeZone()
	 */
	@Deprecated
	long timeZoneOffset();

	/**
	 * @see #timeZone()
	 */
	@Deprecated
	String timeZoneName();

	/**
	 * Depreacted to avoid confusion with synthetic instrument
	 * {@link #components()}.
	 *
	 * @see #spreadLegs()
	 */
	List<InstrumentID> componentLegs();

	/**
	 * Canonical null instance.
	 */
	Instrument NULL = new Instrument() {

		@Override
		public MetaType type() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public int compareTo(final Instrument o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String marketGUID() {
			throw new UnsupportedOperationException();
		}

		@Override
		public SecurityType securityType() {
			throw new UnsupportedOperationException();
		}

		@Override
		public BookLiquidityType liquidityType() {
			throw new UnsupportedOperationException();
		}

		@Override
		public BookStructureType bookStructure() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Size maxBookDepth() {
			throw new UnsupportedOperationException();
		}

		@Override
		public VendorID vendor() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Map<VendorID, String> vendorSymbols() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String symbol() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String description() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String CFICode() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Exchange exchange() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String exchangeCode() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price tickSize() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price pointValue() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price transactionPriceConversionFactor() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Fraction displayFraction() {
			throw new UnsupportedOperationException();
		}

		@Override
		public TimeInterval lifetime() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Schedule marketHours() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Month contractDeliveryMonth() {
			throw new UnsupportedOperationException();
		}

		@Override
		public long timeZoneOffset() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String timeZoneName() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<InstrumentID> componentLegs() {
			throw new UnsupportedOperationException();
		}

		@Override
		public InstrumentID id() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Time contractExpire() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String currencyCode() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String instrumentGroup() {
			throw new UnsupportedOperationException();
		}

		@Override
		public State state() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ChannelID channel() {
			throw new UnsupportedOperationException();
		}

		@Override
		public DateTime created() {
			throw new UnsupportedOperationException();
		}

		@Override
		public DateTime updated() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Calendar calendar() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Schedule schedule() {
			throw new UnsupportedOperationException();
		}

		@Override
		public DateTimeZone timeZone() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PriceFormat priceFormat() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PriceFormat optionStrikePriceFormat() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<InstrumentID> components() {
			throw new UnsupportedOperationException();
		}

		@Override
		public DateTime delivery() {
			throw new UnsupportedOperationException();
		}

		@Override
		public DateTime expiration() {
			throw new UnsupportedOperationException();
		}

		@Override
		public InstrumentID underlier() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price strikePrice() {
			throw new UnsupportedOperationException();
		}

		@Override
		public OptionType optionType() {
			throw new UnsupportedOperationException();
		}

		@Override
		public OptionStyle optionStyle() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<SpreadLeg> spreadLegs() {
			throw new UnsupportedOperationException();
		}

		@Override
		public SpreadType spreadType() {
			throw new UnsupportedOperationException();
		}

	};

}
