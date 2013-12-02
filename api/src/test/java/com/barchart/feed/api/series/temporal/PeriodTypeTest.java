package com.barchart.feed.api.series.temporal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

/**
 * Tests common functionality of the {@link PeriodType} enum.
 * 
 * @author David Ray
 */
public class PeriodTypeTest {
	
	@Test
	public void testConstruction() {
		assertEquals(9, PeriodType.values().length);
	}

	@Test
	public void testInc() {
		PeriodType testType = PeriodType.TICK;
		assertEquals(PeriodType.YEAR, PeriodType.inc(testType, 100));
		assertEquals(PeriodType.TICK, PeriodType.inc(testType, -100));
		assertEquals(PeriodType.TICK, PeriodType.inc(testType, 0));
		assertEquals(PeriodType.SECOND, PeriodType.inc(testType, 1));
		assertEquals(PeriodType.MINUTE, PeriodType.inc(testType, 2));
		assertEquals(PeriodType.HOUR, PeriodType.inc(testType, 3));
		assertEquals(PeriodType.DAY, PeriodType.inc(testType, 4));
		assertEquals(PeriodType.WEEK, PeriodType.inc(testType, 5));
		assertEquals(PeriodType.MONTH, PeriodType.inc(testType, 6));
		assertEquals(PeriodType.QUARTER, PeriodType.inc(testType, 7));
		assertEquals(PeriodType.YEAR, PeriodType.inc(testType, 8));
	}
	
	@Test
	public void testDec() {
		PeriodType testType = PeriodType.YEAR;
		assertEquals(PeriodType.TICK, PeriodType.dec(testType, 100));
		assertEquals(PeriodType.YEAR, PeriodType.dec(testType, -100));
		assertEquals(PeriodType.YEAR, PeriodType.dec(testType, 0));
		assertEquals(PeriodType.QUARTER, PeriodType.dec(testType, 1));
		assertEquals(PeriodType.MONTH, PeriodType.dec(testType, 2));
		assertEquals(PeriodType.WEEK, PeriodType.dec(testType, 3));
		assertEquals(PeriodType.DAY, PeriodType.dec(testType, 4));
		assertEquals(PeriodType.HOUR, PeriodType.dec(testType, 5));
		assertEquals(PeriodType.MINUTE, PeriodType.dec(testType, 6));
		assertEquals(PeriodType.SECOND, PeriodType.dec(testType, 7));
		assertEquals(PeriodType.TICK, PeriodType.dec(testType, 8));
	}
	
	@Test
	public void testLower() {
		PeriodType testType = PeriodType.YEAR;
		assertEquals(PeriodType.QUARTER, PeriodType.lower(testType));
		assertEquals(PeriodType.WEEK, PeriodType.lower(PeriodType.MONTH));
		assertEquals(PeriodType.TICK, PeriodType.lower(PeriodType.TICK));
	}
	
	@Test
	public void testHigher() {
		PeriodType testType = PeriodType.TICK;
		assertEquals(PeriodType.SECOND, PeriodType.higher(testType));
		assertEquals(PeriodType.WEEK, PeriodType.higher(PeriodType.DAY));
		assertEquals(PeriodType.YEAR, PeriodType.higher(PeriodType.YEAR));
	}
	
	@Test
	public void testTypeFor() {
		assertEquals(PeriodType.MINUTE, PeriodType.forString("MiNuTE"));
	}
	
	@Test
	public void testIsLowerThan() {
		assertTrue(PeriodType.MONTH.isLowerThan(PeriodType.QUARTER));
	}
	
	@Test
	public void testEqualsAtResolution() {
		DateTime dt1 = new DateTime(2013, 11, 5, 14, 30, 30);
		DateTime dt2 = new DateTime(2013, 12, 6, 15, 31, 31);
		
		//First assert the two dates are not equal in a "regular" comparison.
		assertFalse(dt1.equals(dt2));
		
		assertTrue(PeriodType.YEAR.equalsAtResolution(dt1, dt2));
		
		dt1 = new DateTime(2013, 11, 5, 14, 30, 30);
		dt2 = new DateTime(2014, 11, 5, 14, 30, 30);
		assertFalse(PeriodType.YEAR.equalsAtResolution(dt1, dt2));
		
		dt1 = new DateTime(2013, 11, 5, 14, 30, 30);
		dt2 = new DateTime(2013, 12, 6, 15, 31, 31);
		assertFalse(PeriodType.MONTH.equalsAtResolution(dt1, dt2));
		assertTrue(PeriodType.QUARTER.equalsAtResolution(dt1, dt2));
		
		assertFalse(PeriodType.MONTH.equalsAtResolution(dt1, dt2));
		assertTrue(PeriodType.QUARTER.equalsAtResolution(dt1, dt2));
		
		dt1 = new DateTime(2013, 11, 20, 14, 30, 30, 0); //vary the day
		dt2 = new DateTime(2013, 11, 21, 14, 30, 30, 0);
		assertFalse(PeriodType.DAY.equalsAtResolution(dt1, dt2));
		assertTrue(PeriodType.WEEK.equalsAtResolution(dt1, dt2));
		assertTrue(PeriodType.HOUR.equalsAtResolution(dt1, dt2));
	}
	
	@Test
	public void testCompareAtResolution() {
		DateTime dt1 = new DateTime(2013, 11, 5, 14, 30, 30);
		DateTime dt2 = new DateTime(2014, 12, 7, 13, 30, 45); //hour is only field less than other
		
		assertTrue(dt1.isBefore(dt2));
		assertEquals(-1, PeriodType.DAY.compareAtResolution(dt1, dt2));
		
		int result = PeriodType.HOUR.compareAtResolution(dt1, dt2);
		assertEquals(1, result);
		
		result = PeriodType.MINUTE.compareAtResolution(dt1, dt2);
		assertEquals(0, result);
	}
	
	@Test
	public void testResolutionInstant() {
		DateTime dt1 = new DateTime(2013, 11, 5, 14, 30, 30, 2);
		
		List<Integer> fl = (List<Integer>)Arrays.asList(2013, 4, 11, 45, 5, 14, 30, 30, 2);
		Collections.reverse(fl);
		Integer[] fields = (Integer[])fl.toArray();
		
		DateTime resolved = PeriodType.MONTH.resolutionInstant(dt1);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("SSS,ss,mm,HH,dd,w,M,:,yyyy");
		
		PeriodType[] types = PeriodType.values();
		for(int i = 0;i < types.length;i++) {
			resolved = types[i].resolutionInstant(dt1);
			
			String[] strFields = fmt.print(resolved).split("\\,");
			strFields[PeriodType.QUARTER.ordinal()] = "" + ExtendedChronology.getQuarter(resolved);
			
			if(i == PeriodType.QUARTER.ordinal()) {
				assertEquals(4, ExtendedChronology.getQuarter(resolved));
			}else if(i == PeriodType.WEEK.ordinal()) {
				assertEquals(fields[i], new Integer(strFields[i]));
			}
			
			System.out.println(types[i] + ": " + fields[i] + ",  " + 
				(i == PeriodType.QUARTER.ordinal() ? ExtendedChronology.getQuarter(resolved) : strFields[i]));
			
			System.out.print("\t");
			for(int j = 0;j < strFields.length;j++) {
				System.out.print(strFields[j] + ",");
			}
			System.out.println("");
		}
	}
	
}
