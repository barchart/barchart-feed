/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.api.instrument.enums;

public class BenchStringFormat {

	public static void main(String[] args) {

		int i = 0;

		long prev_time = System.currentTimeMillis();

		long time;

		for (i = 0; i < 100000; i++) {
			String s = "Blah" + i + "Blah";
		}

		time = System.currentTimeMillis() - prev_time;

		System.out.println("Time after for loop " + time);

		prev_time = System.currentTimeMillis();

		for (i = 0; i < 100000; i++) {
			String s = String.format("Blah %d Blah", i);
		}

		time = System.currentTimeMillis() - prev_time;

		System.out.println("Time after for loop " + time);

	}

}
