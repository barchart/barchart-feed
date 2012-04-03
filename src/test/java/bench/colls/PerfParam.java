/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package bench.colls;

public class PerfParam {

	public final int size;
	public final int loops;

	public PerfParam(int size, int loops) {
		this.size = size;
		this.loops = loops;
	}

	// Create an array of TestParam from a varargs sequence:
	public static PerfParam[] array(int... values) {
		int size = values.length / 2;
		PerfParam[] result = new PerfParam[size];
		int n = 0;
		for (int i = 0; i < size; i++)
			result[i] = new PerfParam(values[n++], values[n++]);
		return result;
	}

	// Convert a String array to a TestParam array:
	public static PerfParam[] array(String[] values) {
		int[] vals = new int[values.length];
		for (int i = 0; i < vals.length; i++)
			vals[i] = Integer.decode(values[i]);
		return array(vals);
	}

}