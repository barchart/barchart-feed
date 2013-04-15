/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.bar.api;

import org.joda.time.DateTime;

import com.barchart.feed.base.bar.enums.MarketBarField;
import com.barchart.util.values.api.Value;

/**
 * represents a market bar, such as O-H-L-C;
 * 
 * bar can be for current or previous day;
 */
public interface MarketBar extends Value<MarketBar> {

	<V extends Value<V>> V get(MarketBarField<V> field);
	
	public double getOpen();
	public double getHigh();
	public double getLow();
	public double getClose();
	public double getSettle();
	
	public long getVolume();
	public long getOpenInterest();
	
	public DateTime getLastUpdate();
	public DateTime getSessionClose();

}
