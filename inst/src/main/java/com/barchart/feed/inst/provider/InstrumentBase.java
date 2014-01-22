package com.barchart.feed.inst.provider;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;

public abstract class InstrumentBase implements Instrument {

	protected static final ValueFactory vals = new ValueFactoryImpl();

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
		return "Unknown Data Vendor";
	}
	
	@Override
	public Map<VendorID, String> vendorSymbols() {
		return new HashMap<VendorID, String>(0);
	}

	@Override
	public String description() {
		return symbol();
	}
	
	@Override
	public String CFICode() {
		return "XXXXXX";
	}

	@Override
	public Exchange exchange() {
		return Exchange.NULL;
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
		
		// TODO This field needs to be added to the inst def proto, but in the name of time
		// im just hacking it in here.  This logic will be moved to the xml decoder
		if(CFICode().startsWith("F") && (symbol().startsWith("SI") || symbol().startsWith("HG"))) {
			return vals.newPrice(1, -2);
		}
		
		return Price.ONE;
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
	public String timeZoneName() {
		return "Null_Time_Zone";
	}
	
	@Override
	public long timeZoneOffset() {
		return 0;
	}
	
	@Override
	public List<InstrumentID> componentLegs() {
		return Collections.<InstrumentID> emptyList();
	}
	
	@Override
	public InstrumentID id() {
		return new InstrumentID(marketGUID());
	}
	
	@Override
	public int compareTo(final Instrument o) {
		return id().compareTo(o.id());
	}
	
	@Override
	public boolean equals(final Object o) {
		
		if(!(o instanceof Instrument)) {
			return false;
		}
		
		return compareTo((Instrument)o) == 0;
		
	}
	
	@Override
	public int hashCode() {
		return id().hashCode();
	}
	
	@Override
	public boolean isNull() {
		return this == Instrument.NULL;
	}
	
	@Override
	public MetaType type() {
		return MetaType.INSTRUMENT;
	}
	
	@Override
	public String symbol() {
		return "NULL_SYMBOL";
	}

	@Override
	public String toString() {
		return id().toString();
	}
	
}
