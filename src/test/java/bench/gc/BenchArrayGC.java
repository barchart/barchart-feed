/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package bench.gc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.bench.time.StopWatch;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.provider.ValueBuilder;

public class BenchArrayGC {

	private static final Logger log = LoggerFactory
			.getLogger(BenchArrayGC.class);

	static final int COUNT = 1000 * 1000;

	static final int SIZE = 50;

	static final PriceValue[][] priceArray = new PriceValue[COUNT][SIZE];

	static final StopWatch timer = new StopWatch();

	public static void main(final String[] args) throws Exception {

		test();
		test();
		test();
		test();
		test();
		test();
		test();
		test();
		test();
		test();

		log.info("READY");

		Thread.sleep(1000 * 1000);

	}

	static void test() {

		System.gc();

		for (int i = 0; i < COUNT; i++) {
			for (int k = 0; k < SIZE; k++) {
				priceArray[i][k] = ValueBuilder.newPrice(i, k); // ?? ns
			}
		}

		timer.startNow();
		System.gc();
		timer.stopNow();

		log.info("{}", timer.getDiff() / COUNT / SIZE);

	}

	/*
	 * -Djava.net.preferIPv4Stack=true
	 * 
	 * -enableassertions
	 * 
	 * -server
	 * 
	 * -XX:ThreadPriorityPolicy=0123456789
	 * 
	 * -verbose:gc -Xloggc:./logs/gc.log -XX:+PrintGCDetails
	 * -XX:+PrintGCTimeStamps
	 * 
	 * -Xms1600m -Xmx1600m
	 * 
	 * -XX:PermSize=50m -XX:MaxPermSize=50m
	 * 
	 * -XX:NewSize=300m -XX:MaxNewSize=300m -XX:SurvivorRatio=2
	 * -XX:TargetSurvivorRatio=90
	 * 
	 * -XX:+UseParNewGC -XX:ParallelGCThreads=4
	 * 
	 * -XX:+UseConcMarkSweepGC -XX:ParallelCMSThreads=1
	 * -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70
	 */

	// result:

	// count=100k size=10 total=1m
	// gc : 95 ns

	// count=50k size=20 total=1m
	// gc : 95 ns

	// count=100k size=100 total=10m
	// gc : 85 ns

	// count=1m size=10 total=10m
	// gc : 85 ns

	// count=1m size=50 total=50m
	// gc : 75 ns
	// need to change gc settings : more heap

}
