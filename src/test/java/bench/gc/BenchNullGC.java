package bench.gc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ddfplus.feed.common.util.time.StopWatch;

public class BenchNullGC {

	private static final Logger log = LoggerFactory
			.getLogger(BenchNullGC.class);

	static final int COUNT = 1000 * 1000;

	static final Object[] objectArray = new Object[COUNT];

	static final Object NULL = new Object();

	static final StopWatch timer = new StopWatch();

	public static void main(String[] args) throws Exception {

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

		// Thread.sleep(1000 * 1000);

	}

	static void test() {

		System.gc();

		for (int k = 0; k < COUNT; k++) {
			objectArray[k] = NULL;
			// objectArray[k] = null;
		}

		timer.startNow();
		System.gc();
		timer.stopNow();

		log.info("{}", timer.nanoTime() / COUNT);

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

	// result
	// null : 12 ns
	// NULL : 14 ns

}
