/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book.api;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.base.book.enums.MarketBookAction;
import com.barchart.util.common.anno.NotMutable;

@NotMutable
public interface MarketDoBookEntry extends MarketBookEntry {

	MarketBookAction act();

	Book.Type type();

}
