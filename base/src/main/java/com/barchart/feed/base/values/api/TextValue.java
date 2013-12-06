/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.api;

import com.barchart.util.common.anno.NotMutable;

/**
 * note: CharSequence is implemented by: TextValue, String, StringBuilder,
 * CharBuffer
 */
@NotMutable
public interface TextValue extends Value<TextValue>, Comparable<CharSequence>,
		CharSequence {

	/** thatText is any CharSequence */
	TextValue concat(CharSequence thatText);

	TextValue toUpperCase();

	TextValue toLowerCase();

	/** thatText is any CharSequence */
	@Override
	int compareTo(CharSequence thatText);

	/** thatText is any CharSequence */
	@Override
	boolean equals(Object thatText);

	/** identical to String.hashCode() */
	@Override
	int hashCode();

	/** one to one converter */
	@Override
	String toString();

}
