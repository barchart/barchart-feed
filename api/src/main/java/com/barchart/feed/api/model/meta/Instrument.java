package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.BookStructureType;
import com.barchart.feed.api.enums.MarketCurrency;
import com.barchart.feed.api.enums.SecurityType;
import com.barchart.feed.api.inst.GuidList;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.feed.api.model.Metadata;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.impl.ValueConst;
import com.barchart.util.value.api.Fraction;

public interface Instrument extends Comparable<Instrument>, Metadata {
	
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
	
	MarketCurrency currency();
	
	Price tickSize();
	
	Price pointValue();
	
	Fraction displayFraction();
	
	TimeInterval lifetime();
	
	Schedule marketHours();
	
	long timeZoneOffset();
	
	String timeZoneName();
	
	GuidList componentLegs();
	
	public static Instrument NULL_INSTRUMENT = new Instrument() {

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public int compareTo(Instrument o) {
			return 0;
		}

		@Override
		public InstrumentGUID GUID() {
			return InstrumentGUID.NULL_INSTRUMENT_GUID;
		}

		@Override
		public String marketGUID() {
			return "NULL";
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
			return ValueConst.NULL_SIZE;
		}

		@Override
		public String instrumentDataVendor() {
			return "NULL";
		}

		@Override
		public String symbol() {
			return "NULL";
		}

		@Override
		public String description() {
			return "NULL";
		}

		@Override
		public String CFICode() {
			return "NULL";
		}

		@Override
		public Exchange exchange() {
			return null;
		}

		@Override
		public String exchangeCode() {
			return "NULL";
		}

		@Override
		public MarketCurrency currency() {
			return null;
		}

		@Override
		public Price tickSize() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Price pointValue() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Fraction displayFraction() {
			return ValueConst.NULL_FRACTION;
		}

		@Override
		public TimeInterval lifetime() {
			return ValueConst.NULL_TIME_INTERVAL;
		}

		@Override
		public Schedule marketHours() {
			return ValueConst.NULL_SCHEDULE;
		}

		@Override
		public long timeZoneOffset() {
			return 0;
		}

		@Override
		public String timeZoneName() {
			return "NULL";
		}

		@Override
		public GuidList componentLegs() {
			return new GuidList();
		}

	};
	
}
