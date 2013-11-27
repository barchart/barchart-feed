/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.random;

import java.security.SecureRandom;
import java.math.BigInteger;

/**
 * No frills id generator using SecureRandom
 * 
 * @author maurycy
 */
public final class SecureGenerator {

	static public final int length = 32;

	// MJS: This is expensive to initialize so we keep one around for a while
	static private SecureRandom random = new SecureRandom();

	// MJS: We make this long enough to make almost impossible a collision in the history of the universe
	static public String nextSessionId() {
		return new BigInteger( 256, random ).toString( 32 );
	}

}