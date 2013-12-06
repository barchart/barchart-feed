/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums;

public abstract class ParamEnumBase2D<V, T extends ParamEnum2D<V, T>> extends
		ParamEnumBase<V, T> implements ParamEnum2D<V, T> {

	//

	protected final int row;

	@Override
	public final int row() {
		return row;
	}

	protected final int col;

	@Override
	public final int col() {
		return col;
	}

	protected ParamEnumBase2D(final ParamEnumBase<?, ?>[] values,
			final int ordinal, final V defVal, final int row, final int col) {
		super(values, ordinal, defVal);
		this.row = row;
		this.col = col;
	}

}
