package com.barchart.feed.api.series.temporal;

import static org.junit.Assert.*;

import org.junit.Test;

public class PeriodTest {

    @Test
    public void testIsCloserTo() {
        Period target = Period.DAY;
        Period p1 = Period.ONE_MINUTE;
        Period p2 = new Period(PeriodType.MINUTE, 1);
        assertFalse(p1.isCloserTo(target, p2));
        
        p2 = new Period(PeriodType.SECOND, 1);
        assertFalse(p2.isCloserTo(target, p1));
        assertTrue(p1.isCloserTo(target, p2));
        
        p2 = new Period(PeriodType.MINUTE, 10);
        assertFalse(p2.isCloserTo(target, p1));
        assertTrue(p1.isCloserTo(target, p2));
    }

}
