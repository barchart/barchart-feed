/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.bench;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Overflow {

	static final Logger log = LoggerFactory.getLogger(Overflow.class);

	public static void main(String[] args) {

		long valuePlus = 1L;
		long valueMinus = -1L;

		for (int k = 0; k < 22; k++) {

			log.info("k={} v={}", k, valuePlus);
			log.info("k={} v={}", k, valueMinus);

			valuePlus *= 10;
			valueMinus *= 10;

		}

	}

}
