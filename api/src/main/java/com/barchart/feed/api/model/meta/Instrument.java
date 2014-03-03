package com.barchart.feed.api.model.meta;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
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
		;

		private final String cfi;
		
		private SecurityType(final String cfi) {
			this.cfi = cfi;
		}
		
		public static SecurityType fromCFI(final String code) {
			
			if(code.length() != 6) {
				return NULL_TYPE;
			}
			
			for(final SecurityType t : values()) {
				if(code.startsWith(t.cfi)) {
					return t;
				}
			}
			
			return NULL_TYPE;
		}
		
	}
	
	enum Month {
		NULL_MONTH, JANUARY, FEBRUARY, MARCH, APRIL, MAY, 
		JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMEBR, DECEMBER
	}
	
	InstrumentID id();

	String marketGUID();

	SecurityType securityType();

	BookLiquidityType liquidityType();

	BookStructureType bookStructure();

	Size maxBookDepth();

	String instrumentDataVendor();

	Map<VendorID, String> vendorSymbols();
	
	String symbol();

	@Override
	String description();

	String CFICode();

	Exchange exchange();

	String exchangeCode();

	Price tickSize();

	Price pointValue();

	/**
	 * For some contracts (e.g. SI, HG), the exchange will report transaction
	 * prices to counterparties in different units than those reported in
	 * general market data. For example, prices for silver futures are reported
	 * in dollars, but the fill prices in FIX messages are in cents. For most
	 * contracts this value will be 1.
	 * 
	 * @return
	 */
	Price transactionPriceConversionFactor();

	Fraction displayFraction();
 
	@Deprecated
	TimeInterval lifetime();
	
	Schedule marketHours();

	Time contractExpire();
	
	Month contractDeliveryMonth();

	long timeZoneOffset();

	String timeZoneName();

	List<InstrumentID> componentLegs();

	/**
	 * Canonical null instance.
	 */
	Instrument NULL = new Instrument() {
		
		@Override
		public MetaType type() {
			return MetaType.INSTRUMENT;
		}
		
		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public int compareTo(final Instrument o) {
			return 0;
		}

		@Override
		public String marketGUID() {
			return "NULL_GUID";
		}

		@Override
		public SecurityType securityType() {
			return SecurityType.NULL_TYPE;
		}

		@Override
		public BookLiquidityType liquidityType() {
			return BookLiquidityType.NONE;
		}

		@Override
		public BookStructureType bookStructure() {
			return BookStructureType.NONE;
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
		public Map<VendorID, String> vendorSymbols() {
			return Collections.<VendorID, String> emptyMap();
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
		public Price transactionPriceConversionFactor() {
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
		public Month contractDeliveryMonth() {
			return Month.NULL_MONTH;
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
		public List<InstrumentID> componentLegs() {
			return Collections.<InstrumentID> emptyList();
		}

		@Override
		public InstrumentID id() {
			return InstrumentID.NULL;
		}
		
		@Override
		public String toString() {
			return "NULL_INSTRUMENT";
		}

		@Override
		public Time contractExpire() {
			return Time.NULL;
		}

	};

}
