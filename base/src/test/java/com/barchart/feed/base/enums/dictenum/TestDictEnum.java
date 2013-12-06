/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums.dictenum;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.barchart.feed.base.enums.Dict;
import com.barchart.feed.base.enums.DictEnum;

public class TestDictEnum {

	@Test
	public void test0() {

		for (Dict<?> key : DictEnum.valuesFor(E1.class)) {
			System.out.println("# key : " + key);
		}

		System.out.println("########");

		for (Dict<?> key : DictEnum.valuesFor(E122.class)) {
			System.out.println("# key : " + key);
		}

		System.out.println("########");

		for (Dict<?> key : DictEnum.valuesFor(E11.class)) {
			System.out.println("# key : " + key);
		}

		System.out.println("########");

		for (Dict<?> key : DictEnum.valuesFor(E12.class)) {
			System.out.println("# key : " + key);
		}

		System.out.println("########");

		for (Dict<?> key : DictEnum.valuesFor(E111.class)) {
			System.out.println("# key : " + key);
		}

		System.out.println("########");

		for (Dict<?> key : DictEnum.valuesFor(E121.class)) {
			System.out.println("# key : " + key);
		}

		assertTrue(true);

	}

}
