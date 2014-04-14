package com.barchart.feed.api.series;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

public class ExtendedChronologyTest {

	@Test
	public void testWithPeriodStart() {
		DateTime testDate = new DateTime(2009, 5, 1, 0, 0, 0, 0);//Friday, May 1st
		DateTime adjustedDate = ExtendedChronology.withPeriodStart(testDate);
		assertEquals("2009-04-01T00:00:00.000", noZone(adjustedDate.toString()));
		
		testDate = new DateTime(2009, 3, 27, 0, 0, 0, 0);//Friday, May 1st
		adjustedDate = ExtendedChronology.withPeriodStart(testDate);
		assertEquals("2009-01-01T00:00:00.000", noZone(adjustedDate.toString()));
	}
	
	@Test
	public void testWithPeriodEnd() {
		DateTime testDate = new DateTime(2009, 5, 1, 0, 0, 0, 0);//Friday, May 1st
		DateTime adjustedDate = ExtendedChronology.withPeriodEnd(testDate);
		assertEquals("2009-06-30T00:00:00.000", noZone(adjustedDate.toString()));
		
		testDate = new DateTime(2009, 3, 27, 0, 0, 0, 0);//Friday, May 1st
		adjustedDate = ExtendedChronology.withPeriodEnd(testDate);
		assertEquals("2009-03-31T00:00:00.000", noZone(adjustedDate.toString()));
	}
	
	/**
	 * Rudimentarily removes the zone information appended to the string
	 * @param dateStr
	 * @return
	 */
	private String noZone(String dateStr) {
		return dateStr.substring(0, dateStr.length() - 6);
	}

}
