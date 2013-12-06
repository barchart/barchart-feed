/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestDictKey {

	class Value {

		<V> V get(Dict<V> key) {
			return null;
		}

	}

	@Test
	public void testName() {

		Value value = new Value();

		String result1 = value.get(DictOne.TEST);

		Integer result2 = value.get(DictTwo.ONE);
		Integer result3 = value.get(DictTwo.TWO);

		assertEquals(DictOne.ONE.name(), "ONE");
		assertEquals(DictOne.TEST.name(), "TEST");

		System.out.println(DictOne.ONE.name() + " " + DictOne.ONE.ordinal());
		System.out.println(DictTwo.TWO.name() + " " + DictTwo.TWO.ordinal());

		System.out.println("guid : " + DictOne.ONE.guid());
		System.out.println("guid : " + DictTwo.TWO.guid());

		System.out.println("DictOne.TEST.guid() : " + DictOne.TEST.guid());
		System.out.println("DictTwo.TEST.guid() : " + DictTwo.TEST.guid());

		System.out.println("DictBase.X1.guid() : " + DictBase.X1.guid());

		for (Dict<?> key : DictBase.values()) {

			System.out.println("# 0 # key : " + key.guid() + " / "
					+ key.ordinal());

		}

		for (Dict<?> key : DictOne.values()) {

			System.out.println("# 1 # key : " + key.guid() + " / "
					+ key.ordinal());

		}

		for (Dict<?> key : DictTwo.values()) {

			System.out.println("# 2 # key : " + key.guid() + " / "
					+ key.ordinal());

		}

	}

}
