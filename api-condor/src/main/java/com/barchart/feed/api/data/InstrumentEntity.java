/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.data;

import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.BookStructureType;
import com.barchart.feed.api.enums.MarketCurrency;
import com.barchart.feed.api.enums.SecurityType;
import com.barchart.feed.api.framework.FrameworkEntity;
import com.barchart.feed.api.framework.MarketTag;
import com.barchart.feed.api.inst.GuidList;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.feed.api.message.Message;
import com.barchart.feed.api.util.Schedule;
import com.barchart.missive.api.Tag;
import com.barchart.missive.core.MissiveException;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.TimeInterval;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueConst;

public interface InstrumentEntity extends Value<InstrumentEntity>,
		Instrument,	FrameworkEntity<InstrumentEntity>,
		Comparable<InstrumentEntity> {

	InstrumentGUID getGUID();

	@Override
	boolean equals(Object that);

	@Override
	int hashCode();

	InstrumentEntity NULL_INSTRUMENT = new InstrumentEntity() {

		@Override
		public <V> V get(final Tag<V> tag) throws MissiveException {
			return null;
		}

		@Override
		public boolean contains(final Tag<?> tag) {
			return false;
		}

		@Override
		public Tag<?>[] tagsList() {
			return new Tag<?>[0];
		}

		@Override
		public int mapSize() {
			return 0;
		}

		@Override
		public InstrumentEntity freeze() {
			return this;
		}

		@Override
		public boolean isFrozen() {
			return true;
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public InstrumentGUID getGUID() {
			return InstrumentGUID.NULL_INSTRUMENT_GUID;
		}

		@Override
		public String marketGUID() {
			return InstrumentGUID.NULL_INSTRUMENT_GUID.toString();
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
		public String exchangeCode() {
			return "NULL";
		}

		@Override
		public MarketCurrency currency() {
			return MarketCurrency.NULL_CURRENCY;
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
		public long timeZoneOffset() {
			return 0;
		}

		@Override
		public String timeZoneName() {
			return "NULL";
		}

		@Override
		public <V> void set(final Tag<V> tag, final V value)
				throws MissiveException {

		}

		@Override
		public Time lastTime() {
			return null;
		}

		@Override
		public Time lastUpdateTime() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public InstrumentEntity copy() {
			return this;
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
		public InstrumentGUID GUID() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Schedule marketHours() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GuidList componentLegs() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Message lastUpdate() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Message lastSnapshot() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MarketTag<InstrumentEntity> tag() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void update(final Message update) {
			// TODO Auto-generated method stub

		}

		@Override
		public int compareTo(InstrumentEntity o) {
			// TODO Auto-generated method stub
			return 0;
		}

	};

}
