package com.barchart.feed.inst.participant;

import java.util.List;

import org.openfeed.proto.inst.InstrumentDefinition;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Identifier;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
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
		public Price tickSize() {
			// TODO Auto-generated method stub
			return null;
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

		@Override
		public Schedule marketHours() {
			// TODO Auto-generated method stub
			return null;
		}

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
		public List<Identifier> componentLegs() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int compareTo(Instrument o) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean isNull() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Identifier id() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MetaType type() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public State state() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void process(InstrumentDefinition value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public InstrumentDefinition definition() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}
		
	};

}
