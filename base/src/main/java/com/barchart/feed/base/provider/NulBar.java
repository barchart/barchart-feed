/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import org.joda.time.DateTime;

import com.barchart.feed.api.data.SessionObject;
import com.barchart.feed.api.market.Snapshot;
import com.barchart.feed.api.market.Update;
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.enums.MarketBarField;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;

@NotMutable
public class NulBar extends ValueFreezer<MarketBar> implements MarketBar {

	@Override
	public <V extends Value<V>> V get(final MarketBarField<V> field) {

		assert field != null;

		return field.value();

	}

	@Override
	public final boolean isNull() {
		return this == MarketConst.NULL_BAR;
	}

	@Override
	public final String toString() {

		final StringBuilder text = new StringBuilder(384);

		text.append("Bar > ");

		for (final MarketBarField<?> field : MarketBarField.values()) {

			text.append(" ");
			text.append(field.name());
			text.append(" > ");
			text.append(get(field));
			text.append(" ");

		}

		// System.err.println(text.length());

		return text.toString();

	}

	@Override
	public double getOpen() {
		return Double.NaN;
	}

	@Override
	public double getHigh() {
		return Double.NaN;
	}

	@Override
	public double getLow() {
		return Double.NaN;
	}

	@Override
	public double getClose() {
		return Double.NaN;
	}

	@Override
	public double getSettle() {
		return Double.NaN;
	}

	@Override
	public long getVolume() {
		return 0;
	}

	@Override
	public long getOpenInterest() {
		return 0;
	}

	@Override
	public DateTime getLastUpdate() {
		return MarketConst.NULL_DATETIME;
	}

	@Override
	public DateTime getSessionClose() {
		return MarketConst.NULL_DATETIME;
	}

	@Override
	public Update<SessionObject> lastUpdate() {
		return null;
	}

	@Override
	public Snapshot<SessionObject> lastSnapshot() {
		return null;
	}

}
