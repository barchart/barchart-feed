package com.barchart.feed.inst.participant;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openfeed.InstrumentDefinition;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;

public interface InstrumentState extends Instrument, Resettable, Instrumentable {
	
	enum State {
		/** An invalid instrument definition */
		NULL,
		/** An empty instrument definition with an ID */
		EMPTY,
		/** A partially complete instrument definition */
		PARTIAL,
		/** A complete instrument definition */
		FULL
	}
	
	State state();
	
	@Override
	void process(InstrumentDefinition value);
	
	@Override
	InstrumentDefinition definition();
	
	@Override
	void reset();
	
	InstrumentState NULL = new InstrumentState() {

		@Override
		public String marketGUID() {
			return "NULL_INSTRUMENT_STATE";
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
			return "NULL_VENDOR";
		}

		@Override
		public String symbol() {
			return "NULL_SYMBOL";
		}
		
		@Override
		public Map<VendorID, String> vendorSymbols() {
			return new HashMap<VendorID, String>(0);
		}

		@Override
		public String description() {
			return "NULL_DESCRIPTION";
		}

		@Override
		public String CFICode() {
			return "NULL_CFI";
		}

		@Override
		public Exchange exchange() {
			return Exchange.NULL;
		}

		@Override
		public String exchangeCode() {
			return "NULL_EXCHANGE_CODE";
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
			return "NULL_TIMEZONE";
		}

		@Override
		public List<InstrumentID> componentLegs() {
			return Collections.emptyList();
		}

		@Override
		public int compareTo(Instrument o) {
			return o.compareTo(Instrument.NULL);
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public InstrumentID id() {
			return InstrumentID.NULL;
		}

		@Override
		public MetaType type() {
			return MetaType.INSTRUMENT;
		}

		@Override
		public State state() {
			return State.NULL;
		}

		@Override
		public void process(InstrumentDefinition value) {
			
		}

		@Override
		public InstrumentDefinition definition() {
			return InstrumentDefinition.getDefaultInstance();
		}

		@Override
		public void reset() {
			
		}

		@Override
		public Time contractExpire() {
			return Time.NULL;
		}

	};

}
