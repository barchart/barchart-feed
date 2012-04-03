package bench;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMarketField2D {

	static final Logger log = LoggerFactory.getLogger(TestMarketField2D.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() {

		for (MarketField2D field : MarketField2D.valuesUnsafe()) {
			log.info("field {}", field);
			log.info("row {} col {}", field.row(), field.col());
		}

		assertTrue(true);

	}

}
