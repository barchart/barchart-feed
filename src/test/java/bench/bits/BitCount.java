package bench.bits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ddfplus.feed.common.util.time.StopWatch;

public class BitCount {

	static final Logger log = LoggerFactory.getLogger(BitCount.class);

	static final int COUNT = 1000 * 1000 * 100;

	static final StopWatch timer = new StopWatch();

	static final int BITS = 13542523;

	static final int bitCount(int bits) {
		for (int k = 0; k < 32; k++) {
			bits++;
		}
		return bits;
	}

	static void test1() {

		timer.clear();

		timer.start();

		int a = 0;

		for (int k = 0; k < COUNT; k++) {
			for (int m = 0; m < COUNT; m++) {
				a = Integer.bitCount(m);
			}
		}

		timer.stop();

		log.info("bitCount :: {}", a);
		log.info("bitCount :: {}", timer.nanoString());
		log.info("bitCount :: {}", timer.nanoTime() / COUNT);

	}

	static void test2() {

		timer.clear();

		timer.start();

		int a = 0;

		for (int k = 0; k < COUNT; k++) {
			for (int m = 0; m < COUNT; m++) {
				a = bitCount(m);
			}
		}

		timer.stop();

		log.info("bitCount :: {}", a);
		log.info("bitCount :: {}", timer.nanoString());
		log.info("bitCount :: {}", timer.nanoTime() / COUNT);

	}

	public static void main(String[] args) {

		log.info("###");
		test1();
		test2();
		log.info("###");
		test1();
		test2();
		log.info("###");
		test1();
		test2();
		log.info("###");
		test1();
		test2();
		log.info("###");
		test1();
		test2();
		log.info("###");
		test1();
		test2();
		log.info("###");
		test1();
		test2();
		log.info("###");
		test1();
		test2();
		log.info("###");
		test1();
		test2();
		log.info("###");
		test1();
		test2();
		log.info("###");

	}

}
