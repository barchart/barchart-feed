/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Identifier;
import com.barchart.feed.inst.InstrumentField;
import com.barchart.missive.core.ObjectMapSafe;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.FactoryLoader;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.values.api.PriceValue;

public abstract class InstrumentBase extends ObjectMapSafe implements Instrument {
	
	private static final Factory factory = FactoryLoader.load();
	
	public final List<Identifier> componentLegs = 
			new ArrayList<Identifier>();
	
	private volatile Identifier id = Identifier.NULL;
	
	public static class InstIdentifier implements Identifier {
		
		private final String id;
		
		public InstIdentifier(final CharSequence id) {
			this.id = id.toString().intern();
		}
		
		@Override
		public String toString() {
			return id;
		}

		@Override
		public int compareTo(Identifier o) {
			return id.compareTo(o.toString());
		}
		
		@Override
		public boolean equals(final Object o) {
			
			if(!(o instanceof Identifier)) {
				return false;
			}
			
			return id.equals(((Identifier)o).toString());
			
		}
		
		@Override
		public int hashCode() {
			return id.hashCode();
		}
		
		@Override
		public boolean isNull() {
			return this == Identifier.NULL;
		}
		
	}
	
	@Override
	public MetaType type() {
		return MetaType.INSTRUMENT;
	}
	
	@Override 
	public Identifier id() {
		if(id == Identifier.NULL && get(InstrumentField.GUID) != null && 
				!get(InstrumentField.GUID).isNull()) {
			id = new InstIdentifier(get(InstrumentField.GUID).toString());
		}
		return id;
	}
	
	@Override
	public int compareTo(final Instrument o) {
		return id().compareTo(o.id());
	}
	
	@Override
	public int hashCode() {
		return id().hashCode();
	}
	
	@Override
	public boolean equals(final Object o) {
		if(o instanceof Instrument) {
			return compareTo((Instrument)o) == 0;
		} else {
			return false;
		}
	}
	
	@Override
	public final boolean isNull() {
		return this == Instrument.NULL;
	}
	
	@Override
	public String marketGUID() {
		return get(InstrumentField.GUID).toString();
	}

	@Override
	public SecurityType securityType() {
		return get(InstrumentField.SECURITY_TYPE);
	}

	@Override
	public BookLiquidityType liquidityType() {
		return get(InstrumentField.BOOK_LIQUIDITY);
	}

	@Override
	public BookStructureType bookStructure() {
		return get(InstrumentField.BOOK_STRUCTURE);
	}

	@Override
	public Size maxBookDepth() {
		return factory.newSize(get(InstrumentField.BOOK_DEPTH).asLong(), 1);
	}
	
	@Override
	public String instrumentDataVendor() {
		return get(InstrumentField.VENDOR).toString();
	}

	@Override
	public String symbol() {
		return get(InstrumentField.SYMBOL).toString();
	}

	@Override
	public String description() {
		return get(InstrumentField.DESCRIPTION).toString();
	}

	@Override
	public String CFICode() {
		return get(InstrumentField.CFI_CODE).toString();
	}
	
	@Override
	public Exchange exchange() {
		return Exchanges.fromName(exchangeCode());
	}

	@Override
	public String exchangeCode() {
		return get(InstrumentField.EXCHANGE_CODE).toString();
	}

	@Override
	public Price tickSize() {
		final PriceValue temp = get(InstrumentField.TICK_SIZE);
		return factory.newPrice(temp.mantissa(), temp.exponent());
	}

	@Override
	public Price pointValue() {
		final PriceValue temp = get(InstrumentField.POINT_VALUE);
		return factory.newPrice(temp.mantissa(), temp.exponent());
	}

	@Override
	public Fraction displayFraction() {
		return get(InstrumentField.DISPLAY_FRACTION);
	}

	@Override
	public TimeInterval lifetime() {
		return get(InstrumentField.LIFETIME);
	}

	@Override
	public Schedule marketHours() {
		return get(InstrumentField.MARKET_HOURS);
	}
	
	@Override
	public long timeZoneOffset() {
		return get(InstrumentField.TIME_ZONE_OFFSET).asLong();
	}

	@Override
	public String timeZoneName() {
		return get(InstrumentField.TIME_ZONE_NAME).toString();
	}

	@Override
	public List<Identifier> componentLegs() {
		return Collections.unmodifiableList(componentLegs);
	}

}
