/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums;

public interface ParamEnum<V, T extends ParamEnum<V, T>> extends EnumJDK {

	int sequence();

	V value();

	//

	boolean is(ParamEnum<?, ?> that);

	boolean isIn(ParamEnum<?, ?>... thatArray);

}
