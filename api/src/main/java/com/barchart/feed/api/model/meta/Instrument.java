package com.barchart.feed.api.model.meta;

import java.util.Collections;
import java.util.List;

import com.barchart.feed.api.util.Identifier;
import com.barchart.feed.api.util.InstrumentGUID;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
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

		NULL_TYPE, //
		FOREX, //
		INDEX, //
		EQUITY, //
		FUTURE, //
		OPTION, //
		SPREAD, //
		;

	}
	
	InstrumentGUID GUID();

	String marketGUID();

	SecurityType securityType();

	BookLiquidityType liquidityType();

	BookStructureType bookStructure();

	Size maxBookDepth();

	String instrumentDataVendor();

	String symbol();

	String description();

	String CFICode();

	Exchange exchange();

	String exchangeCode();

	Price tickSize();

	Price pointValue();

	Fraction displayFraction();

	TimeInterval lifetime();

	Schedule marketHours();

	long timeZoneOffset();

	String timeZoneName();

	List<InstrumentGUID> componentLegs();

	/**
	 * Canonical null instance.
	 */
	Instrument NULL = new Instrument() {
		
		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public int compareTo(final Instrument o) {
			return 0;
		}

		@Override
		public InstrumentGUID GUID() {
			return InstrumentGUID.NULL;
		}

		@Override
		public String marketGUID() {
			return "NULL GUID";
		}

		@Override
		public SecurityType securityType() {
			return null;
		}

		@Override
		public BookLiquidityType liquidityType() {
			return null;
		}

		@Override
		public BookStructureType bookStructure() {
			return null;
		}

		@Override
		public Size maxBookDepth() {
			return Size.NULL;
		}

		@Override
		public String instrumentDataVendor() {
			return "NULL VENDOR";
		}

		@Override
		public String symbol() {
			return "NULL SYMBOL";
		}

		@Override
		public String description() {
			return "NULL INSTRUMENT";
		}

		@Override
		public String CFICode() {
			return "NULL CFICode";
		}

		@Override
		public Exchange exchange() {
			return null;
		}

		@Override
		public String exchangeCode() {
			return "NULL EXCHANGE";
		}

		@Override
		public Price tickSize() {
			return Price.NULL;
		}

		@Override
		public Price pointValue() {
			return Price.NULL;
		}

		@Override
		public Fraction displayFraction() {
			return Fraction.NULL;
		}

		@Override
		public TimeInterval lifetime() {
			return TimeInterval.NULL;
		}

		@Override
		public Schedule marketHours() {
			return Schedule.NULL;
		}

		@Override
		public long timeZoneOffset() {
			return 0;
		}

		@Override
		public String timeZoneName() {
			return "NULL TIMEZONE";
		}

		@Override
		public List<InstrumentGUID> componentLegs() {
			return Collections.emptyList();
		}

		@Override
		public Identifier id() {
			return Identifier.NULL;
		}
		
		@Override
		public String toString() {
			return "NULL INSTRUMENT";
		}
		
	};

}
