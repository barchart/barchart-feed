/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.message;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.book.enums.MarketBookAction;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;

public class MockMsgBook extends MockMessage {

	public MockMsgBook(Instrument inst) {
		super(inst);
	}

	@Override
	public <Result, Param> Result accept(
			MockMessageVisitor<Result, Param> visitor, Param param) {
		return visitor.visit(this, param);
	}

	public MarketBookAction act;

	public Book.Side side;

	public Book.Type type;

	public int place;

	public PriceValue price;

	public SizeValue size;

	public TimeValue time;

}
