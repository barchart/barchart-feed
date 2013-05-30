/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import com.barchart.feed.api.consumer.data.Exchange;
import com.barchart.feed.api.consumer.data.Instrument;
import com.barchart.feed.api.consumer.enums.BookLiquidityType;
import com.barchart.feed.api.consumer.enums.BookStructureType;
import com.barchart.feed.api.consumer.enums.MarketCurrency;
import com.barchart.feed.api.consumer.enums.SecurityType;
import com.barchart.feed.api.consumer.inst.GuidList;
import com.barchart.feed.api.consumer.inst.InstrumentGUID;
import com.barchart.feed.api.util.Schedule;
import com.barchart.feed.inst.InstrumentField;
import com.barchart.missive.core.ObjectMapSafe;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.provider.FactoryProvider;
import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.TimeInterval;

public abstract class InstrumentBase extends ObjectMapSafe implements Instrument {
	
	@Override
	public int compareTo(final Instrument o) {
		return GUID().compareTo(o.GUID());
	}
	
	@Override
	public int hashCode() {
		return GUID().hashCode();
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
	public boolean isFrozen() {
		return true;
	}
	
	@Override
	public Instrument freeze() {
		return this;
	}

	@Override
	public final boolean isNull() {
		return this == Instrument.NULL_INSTRUMENT;
	}
	
	@Override
	public InstrumentGUID GUID() {
		return get(InstrumentField.GUID);
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
		return null;
	}
	
	@Override
	public long maxBookDepthLong() {
		return get(InstrumentField.BOOK_DEPTH).asLong();
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
		return ExchangeFactory.fromCode(exchangeCode());
	}

	@Override
	public String exchangeCode() {
		return get(InstrumentField.EXCHANGE_CODE).toString();
	}

	@Override
	public MarketCurrency currency() {
		return get(InstrumentField.CURRENCY_CODE);
	}
	
	@Override
	public Price tickSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double tickSizeDouble() {
		return get(InstrumentField.TICK_SIZE).asDouble();
	}
	
	@Override
	public Price pointValue() {
		final PriceValue temp = get(InstrumentField.POINT_VALUE);
		return FactoryProvider.instance().newPrice(temp.mantissa(), temp.exponent());
	}

	@Override
	public double pointValueDouble() {
		return get(InstrumentField.POINT_VALUE).asDouble();
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
	public GuidList componentLegs() {
		return get(InstrumentField.COMPONENT_LEGS);
	}

	@Override
	public Time lastUpdateTime() {
		// Ignore
		return null;
	}

}
