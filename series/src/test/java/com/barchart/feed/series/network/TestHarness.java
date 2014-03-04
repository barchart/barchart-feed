package com.barchart.feed.series.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.network.Analytic;
import com.barchart.feed.api.series.network.Assembler;
import com.barchart.feed.api.series.service.SeriesFeedService;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;

public class TestHarness {
    public static Instrument makeInstrument(final String symbol) {
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

			@Override
			public Price transactionPriceConversionFactor() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Time contractExpire() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Month contractDeliveryMonth() {
				// TODO Auto-generated method stub
				return null;
			}
        };
    }
    
    public static Analytic makeEmptyAnalytic() {
        return new Analytic() {
        	@Override
            public Span process(Span span) {
                // TODO Auto-generated method stub
                return span;
            }

			@Override
            public <E extends DataPoint> void addInputTimeSeries(String key,
                    DataSeries<E> timeSeries) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public <E extends DataPoint> DataSeries<E> getInputTimeSeries(
                    String key) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <E extends DataPoint> void addOutputTimeSeries(String key,
                    DataSeries<E> timeSeries) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public <E extends DataPoint> DataSeries<E> getOutputTimeSeries(
                    String key) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void valueUpdated() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public <E extends DataPoint> void setValue(DateTime time, String key, E e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void setCalculation(DateTime time, String key, double value) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void setRange(DateTime time, String key, double high,
                    double low) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void setZone(DateTime time, DateTime nextTime, String key,
                    double high, double low, double nextHigh, double nextLow) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void setName(String name) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public String getName() {
                // TODO Auto-generated method stub
                return null;
            }
		};
    }
    
    public static BarchartSeriesProvider getTestSeriesProvider(SeriesSubscription ss) {
        BarchartSeriesProvider bsp = new BarchartSeriesProvider(getUnitTestFeedService());
        bsp.getAssemblerMapForTesting().put(ss, new ArrayList<Distributor>());
        bsp.getAssemblerMapForTesting().get(ss).add(new Distributor(ss));
        return bsp;
    }
    
    public static SeriesFeedService getUnitTestFeedService() {
        return new SeriesFeedService() {

            @Override
            public void registerAssembler(Assembler assembler) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void unregisterAssembler(Assembler assembler) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public Instrument lookupInstrument(String symbol) {
                // TODO Auto-generated method stub
                return null;
            }
            
        };
    }
}
