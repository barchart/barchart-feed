package com.barchart.feed.series.services;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.series.services.NodeDescriptor;
import com.barchart.feed.api.series.services.Processor;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.TimeInterval;

public class BarBuilderNodeDescriptorTest {
    
    @Test
    public void testGetLowerBaseType() {
        PeriodType type = BarBuilderNodeDescriptor.getLowerBaseType(PeriodType.MONTH);
        assertEquals(PeriodType.DAY, type);
        
        type = BarBuilderNodeDescriptor.getLowerBaseType(PeriodType.DAY);
        assertEquals(PeriodType.MINUTE, type);
        
        type = BarBuilderNodeDescriptor.getLowerBaseType(PeriodType.MINUTE);
        assertEquals(PeriodType.SECOND, type);
    }

    @Test
    public void testGetProcessorChain() {
        String symbol = "ESZ13";
        Instrument instr = makeInstrument(symbol);
        BarBuilderNodeDescriptor nDesc = new BarBuilderNodeDescriptor();
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf1 = new TimeFrame(new Period(PeriodType.TICK, 1), dt1, null);
        
        SeriesSubscription sub1 = new SeriesSubscription("ESZ13", instr, nDesc, new TimeFrame[] { tf1 }, TradingWeek.DEFAULT);
        
        symbol = "ESZ13";
        instr = makeInstrument(symbol);
        nDesc = new BarBuilderNodeDescriptor();
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.MONTH, 7), dt2, null);
        
        SeriesSubscription sub2 = new SeriesSubscription("ESZ13", instr, nDesc, new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
        
        assertTrue(sub2.isDerivableFrom(sub1));
        
        List<Processor> pList = nDesc.getProcessorChain(sub1, sub2);
        int size = pList.size();
        assertEquals(5, size);
        assertEquals(new Period(PeriodType.MONTH, 7), 
            ((BarBuilder)pList.get(size - 1)).getOutputSubscription(NodeDescriptor.TYPE_IO).getTimeFrames()[0].getPeriod());
        assertEquals(new Period(PeriodType.MONTH, 1), 
            ((BarBuilder)pList.get(size - 2)).getOutputSubscription(NodeDescriptor.TYPE_IO).getTimeFrames()[0].getPeriod());
        assertEquals(new Period(PeriodType.DAY, 1), 
            ((BarBuilder)pList.get(size - 3)).getOutputSubscription(NodeDescriptor.TYPE_IO).getTimeFrames()[0].getPeriod());
        assertEquals(new Period(PeriodType.MINUTE, 1), 
            ((BarBuilder)pList.get(size - 4)).getOutputSubscription(NodeDescriptor.TYPE_IO).getTimeFrames()[0].getPeriod());
        assertEquals(new Period(PeriodType.SECOND, 1), 
            ((BarBuilder)pList.get(size - 5)).getOutputSubscription(NodeDescriptor.TYPE_IO).getTimeFrames()[0].getPeriod());
        
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
