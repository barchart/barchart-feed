/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package bench.sizeof;

import java.util.concurrent.locks.ReentrantLock;

import net.sourceforge.sizeof.SizeOf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.values.provider.ValueBuilder;

// use -javaagent:./lib/sourceforge-sizeof-0.2.1.jar

public class BenchSizeOf {

	private static final Logger log = LoggerFactory
			.getLogger(BenchSizeOf.class);

	static int COUNT = 100 * 1000;

	static void test(final Object object) {

		log.info("0: class    {}", object.getClass().getName());
		log.info("1: JavaSize {}",
				com.barchart.util.bench.size.JavaSize.of(object));
		log.info("2: SizeOf   {}", SizeOf.deepSizeOf(object));
		log.info("\n");

	}

	public static void main(final String[] args) throws Exception {

		SizeOf.skipStaticField(true);
		SizeOf.setMinSizeToLog(1);

		test(new Object());
		test(ValueBuilder.newPrice(0, 0));
		test(ValueBuilder.newSize(0));
		test(ValueBuilder.newTime(0));
		test(new String());
		test(new ReentrantLock());

		System.gc();

		log.info("READY");

		Thread.sleep(1000 * 1000);

	}
}
