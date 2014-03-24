package com.barchart.feed.series.network;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.BookSet;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.Session;
import com.barchart.feed.api.model.data.SessionSet;
import com.barchart.feed.api.model.data.Trade;
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
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;

public class TestHarness {
    private static HashSet<Market.Component> staticSet = new HashSet<Market.Component>();
    private static ValueFactory factory = new ValueFactoryImpl();
    
    public static Size makeSize(final String sz) {
        return factory.newSize(Integer.parseInt(sz));
    }
    
    public static Price makePrice(String dblStr) {
        final double db = new BigDecimal(dblStr).setScale(2, RoundingMode.HALF_UP).doubleValue();
        Price p = factory.newPrice(db);
        
        return p;
    }
    
    public static Market makeMarket(final DateTime t, final Instrument i, final String price, final String sz) {
        return new Market() {
            private Instrument inst = i;
            private DateTime time = t;
            private Price p = makePrice(price);
            private Set<Market.Component> thisSet = staticSet;
            private Size size = makeSize(sz);
           
            
            @Override
            public Instrument instrument() {
                return inst;
            }
            
            private Time getTime() {
                return factory.newTime(time.getMillis());
            }

            @Override
            public Time updated() {
                return getTime();
            }

            @Override
            public boolean isNull() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Set<Component> change() {
                return thisSet;
            }

            @Override
            public Trade trade() {
                return new Trade() {

                    @Override
                    public Instrument instrument() {
                        return inst;
                    }

                    @Override
                    public Time updated() {
                        return getTime();
                    }

                    @Override
                    public boolean isNull() {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public Session session() {
                       return null;
                    }

                    @Override
                    public Set<TradeType> types() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public Price price() {
                        return p;
                    }

                    @Override
                    public Size size() {
                        return size;
                    }

                    @Override
                    public Time time() {
                        return getTime();
                    }
                    
                };
            }

            @Override
            public Book book() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public BookSet bookSet() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Cuvol cuvol() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Session session() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public SessionSet sessionSet() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public LastPrice lastPrice() {
                // TODO Auto-generated method stub
                return null;
            }
            
        };
    }
    
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
    
    public static BarchartSeriesProvider getTestSeriesProviderWithNoDistributor(SeriesSubscription ss) {
        BarchartSeriesProvider bsp = new BarchartSeriesProvider(getUnitTestFeedService());
        return bsp;
    }
    
    public static BarchartSeriesProvider getTestSeriesProvider(SeriesSubscription ss) {
        BarchartSeriesProvider bsp = new BarchartSeriesProvider(getUnitTestFeedService());
        bsp.getAssemblerMapForTesting().put(ss, new ArrayList<Distributor>());
        bsp.getAssemblerMapForTesting().get(ss).add(new Distributor(ss));
        return bsp;
    }
    
    public static SeriesFeedService getUnitTestFeedService() {
        return new SeriesFeedService() {
            Instrument in = null;
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
                return in = in == null ? makeInstrument(symbol) : in;
            }
            
        };
    }
}
