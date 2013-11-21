package com.barchart.feed.api.data;

import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.BookStructureType;
import com.barchart.feed.api.enums.MarketCurrency;
import com.barchart.feed.api.enums.SecurityType;
import com.barchart.feed.api.inst.GuidList;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.missive.api.Tag;
import com.barchart.missive.api.TagMap;
import com.barchart.missive.core.MissiveException;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.Value;

public interface Instrument extends MarketData<Instrument>, Comparable<Instrument>, Value<Instrument>, TagMap {
	
	InstrumentGUID GUID();
	
	String marketGUID();
	
	SecurityType securityType();
	
	BookLiquidityType liquidityType();
	
	BookStructureType bookStructure();
	
	long maxBookDepthLong();
	Size maxBookDepth();
	
	String instrumentDataVendor();
	
	String symbol();
	
	String description();
	
	String CFICode();
	
	Exchange exchange();
	String exchangeCode();
	
	MarketCurrency currency();
	
	double tickSizeDouble();
	Price tickSize();
	
	double pointValueDouble();
	Price pointValue();
	
	Fraction displayFraction();
	
	TimeInterval lifetime();
	
//	Schedule marketHours();
	
	long timeZoneOffset();
	
	String timeZoneName();
	
	GuidList componentLegs();
	
	// TODO
	public static Instrument NULL_INSTRUMENT = new Instrument() {

		@Override
		public Time lastUpdateTime() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isNull() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int compareTo(Instrument o) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public InstrumentGUID GUID() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String marketGUID() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SecurityType securityType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BookLiquidityType liquidityType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BookStructureType bookStructure() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long maxBookDepthLong() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Size maxBookDepth() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String instrumentDataVendor() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String symbol() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String description() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String CFICode() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Exchange exchange() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String exchangeCode() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MarketCurrency currency() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public double tickSizeDouble() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Price tickSize() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public double pointValueDouble() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Price pointValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Fraction displayFraction() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public TimeInterval lifetime() {
			// TODO Auto-generated method stub
			return null;
		}

//		@Override
//		public Schedule marketHours() {
//			// TODO Auto-generated method stub
//			return null;
//		}

		@Override
		public long timeZoneOffset() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String timeZoneName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GuidList componentLegs() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Instrument freeze() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isFrozen() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public <V> V get(Tag<V> tag) throws MissiveException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean contains(Tag<?> tag) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Tag<?>[] tagsList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int mapSize() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Instrument copy() {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	
}
