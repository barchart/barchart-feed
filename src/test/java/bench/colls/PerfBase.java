/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package bench.colls;

public abstract class PerfBase<C> {
	
	String name;

	public PerfBase(String name) {
		this.name = name;
	}

	// Override this Template Method for different tests.
	// Returns actual number of repetitions of test.
	abstract int test(C container, PerfParam tp);

}