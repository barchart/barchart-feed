/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package bench.colls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import javolution.util.FastMap;

public class MapPerformance {

	static List<PerfBase<Map<Integer, Integer>>> tests = new ArrayList<PerfBase<Map<Integer, Integer>>>();

	static {
		tests.add(new PerfBase<Map<Integer, Integer>>("put") {
			int test(Map<Integer, Integer> map, PerfParam tp) {
				int loops = tp.loops;
				int size = tp.size;
				for (int i = 0; i < loops; i++) {
					map.clear();
					for (int j = 0; j < size; j++)
						map.put(j, j);
				}
				return loops * size;
			}
		});
		tests.add(new PerfBase<Map<Integer, Integer>>("get") {
			int test(Map<Integer, Integer> map, PerfParam tp) {
				int loops = tp.loops;
				int span = tp.size * 2;
				for (int i = 0; i < loops; i++)
					for (int j = 0; j < span; j++)
						map.get(j);
				return loops * span;
			}
		});
		tests.add(new PerfBase<Map<Integer, Integer>>("iterate") {
			int test(Map<Integer, Integer> map, PerfParam tp) {
				int loops = tp.loops * 10;
				for (int i = 0; i < loops; i++) {
					Iterator it = map.entrySet().iterator();
					while (it.hasNext())
						it.next();
				}
				return loops * map.size();
			}
		});
	}

	static void testRun() {

		PerfRunner.run(new TreeMap<Integer, Integer>(), tests);
		PerfRunner.run(new LinkedHashMap<Integer, Integer>(), tests);
		PerfRunner.run(new IdentityHashMap<Integer, Integer>(), tests);
		PerfRunner.run(new WeakHashMap<Integer, Integer>(), tests);
		PerfRunner.run(new Hashtable<Integer, Integer>(), tests);
		PerfRunner.run(new HashMap<Integer, Integer>(), tests);
		PerfRunner.run(new ConcurrentHashMap<Integer, Integer>(), tests);
		PerfRunner.run(new FastMap<Integer, Integer>(), tests);
		// Tester.run(new Int2IntArrayMap(), tests);

	}

	public static void main(String[] args) {

		if (args.length > 0)
			PerfRunner.defaultParams = PerfParam.array(args);

		testRun();
		testRun();
		testRun();

	}

}