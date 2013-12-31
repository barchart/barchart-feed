package com.barchart.feed.series.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.TimeInterval;

public class SubscriptionTest {

    /**
     * We need to test:
     * 1. Symbols are same
     * 2. TimeFrames: (assuming sub1 is the "source" and sub2 is the candidate "sink")
     *      a. StartDates for sub2 are equal or sub2 is after sub1
     *      b. For all TimeFrames:
     *          I. sub1.PeriodType is lower than sub2.PeriodType
     *          II. sub1.PeriodType is lower than PeriodType.WEEK (one of [TICK, MINUTE, HOUR, DAY] )
     *          III. sub1.Period.Interval == 1
     *          --OR--
     *          I. sub1.PeriodType is equal than sub2.PeriodType
     *          II. sub1.Period.Interval == 1
     *      c. Sessions (TradingWeek(s)) are the same.
     */ 
    @Test
    public void testIsDerivableFrom() {
        String symbol = "ESZ13";
        Instrument instr = makeInstrument(symbol);
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf1 = new TimeFrame(new Period(PeriodType.TICK, 1), dt1, null);
        
        SeriesSubscription sub1 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf1 }, TradingWeek.DEFAULT);
        
        symbol = "ESZ13";
        instr = makeInstrument(symbol);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.SECOND, 1), dt2, null);
        
        SeriesSubscription sub2 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
        
        assertTrue(sub2.isDerivableFrom(sub1));
        
        //-----
        
        symbol = "ESZ13";
        instr = makeInstrument(symbol);
        DateTime dt3 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf3 = new TimeFrame(new Period(PeriodType.SECOND, 1), dt3, null);
        
        SeriesSubscription sub3 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf3 }, TradingWeek.DEFAULT);
        
        List<SeriesSubscription> subList = new ArrayList<SeriesSubscription>();
        subList.add(sub2);
        subList.add(sub1);
        
        assertTrue(subList.contains(sub3));
        
        
    }
    
    public Instrument makeInstrument(final String symbol) {
        return new Instrument() {

            @Override
            public int compareTo(Instrument arg0) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public boolean isNull() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public MetaType type() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public InstrumentID id() {
                return new InstrumentID(symbol);
            }

            @Override
            public String marketGUID() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public SecurityType securityType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public BookLiquidityType liquidityType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public BookStructureType bookStructure() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size maxBookDepth() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String instrumentDataVendor() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String symbol() {
               return symbol;
            }

            @Override
            public String description() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String CFICode() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Exchange exchange() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String exchangeCode() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price tickSize() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price pointValue() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Fraction displayFraction() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public TimeInterval lifetime() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Schedule marketHours() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public long timeZoneOffset() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public String timeZoneName() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<InstrumentID> componentLegs() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Map<VendorID, String> vendorSymbols() {
                // TODO Auto-generated method stub
                return null;
            }
            
        };
    }

}
