package com.barchart.feed.series.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import rx.Observable;

import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.connection.Connection.Monitor;
import com.barchart.feed.api.connection.TimestampListener;
import com.barchart.feed.api.consumer.ConsumerAgent;
import com.barchart.feed.api.consumer.MarketService;
import com.barchart.feed.api.filter.Filter;
import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.BookSet;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.model.data.Session;
import com.barchart.feed.api.model.data.SessionSet;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.feed.api.model.meta.id.ExchangeID;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.series.services.HistoricalObserver;
import com.barchart.feed.api.series.services.HistoricalResult;
import com.barchart.feed.api.series.services.Query;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Scaled;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;

public class FauxMarketService implements MarketService {
    private MarketObserver<Market> callback;
    
    private HashSet<Market.Component> staticSet = new HashSet<Market.Component>();
    
    private FauxHistoricalService histService;
    
    private Map<Instrument, SymbolTick> lastTicks = new HashMap<Instrument, SymbolTick>();
    
    private Query lastQuery;
    
    private boolean isRunning;
    
    private DateTimeFormatter tickFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private DateTimeFormatter minuteFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
    
    private Thread serverThread;
    
    public FauxMarketService(String uname, String pword) {
        this.histService = new FauxHistoricalService(null);
        staticSet.add(Market.Component.TRADE);
    }
    
    private class HistoricalSubject implements HistoricalObserver<HistoricalResult> {
        @Override public void onCompleted() {}
        @Override public void onError(Throwable e) {}
        @Override
        public void onNext(HistoricalResult historicalResult) {
            String csv = historicalResult.getResult().get(historicalResult.getResult().size() - 1);
            System.out.println("last csv = " + csv);
            String[] csvArray = csv.split("\\,");
            
            SymbolTick config = new SymbolTick();
            SeriesSubscription d = (SeriesSubscription)historicalResult.getSubscription();
            config.desc = d;
            
            Price p = makePrice(csvArray[csvArray.length - 2]);
            config.lastPrice = p;
            
            DateTime tradeTime;
            if(d.getTimeFrames()[0].getPeriod().getPeriodType().isHigherThan(PeriodType.TICK)) {
                tradeTime = minuteFormat.parseDateTime(csvArray[0]);
            }else{
                tradeTime = tickFormat.parseDateTime(csvArray[0]);
            }
            config.lastTime = tradeTime.plusMinutes(1);
            
            config.lastSize = makeSize(csvArray[csvArray.length - 1]);
            
            lastTicks.put(d.getInstrument(), config);
            
            isRunning = true;
            
            if(serverThread == null || !serverThread.isAlive()) {
                System.out.println("Starting Market Thread");
                serverThread = getServerThread();
                serverThread.start();
            }
        }
    }
    
    private class SymbolTick {
        private Price lastPrice;
        private DateTime lastTime;
        private Size lastSize;
        private SeriesSubscription desc;
    }
    
    private Thread getServerThread() {
        return new Thread() {
            TradingWeek week = TradingWeek.DEFAULT;
            Random random = new Random();
            boolean isNeg;
            
            public void run() {
                try {
                    while(isRunning) {
                        for(Instrument i : lastTicks.keySet()) {
                            double added = isNeg ? random.nextDouble() * -1 : random.nextDouble();
                            isNeg = !isNeg;
                            
                            SymbolTick lastTick = lastTicks.get(i);
                            double newPrice = lastTick.lastPrice.asDouble() + added;
//                            System.out.println("newPrice for Instrument: " + i.symbol() + " = " + newPrice);
                            lastTick.lastPrice = makePrice("" + newPrice);
                            lastTick.lastTime = new DateTime(lastTick.lastTime.plusMillis((int)(500 * random.nextDouble())));//week.getNextSessionDate(lastTick.lastTime, lastTick.desc.getTimeFrames()[0].getPeriod());
                            lastTick.lastSize = makeSize("" + random.nextInt(10));
                            
                            Market m = makeMarket(lastTick.lastTime, lastTick.desc.getInstrument(), 
                                "" + lastTick.lastPrice.asDouble(), "" + lastTick.lastSize.mantissa());
                            
                            callback.onNext(m);
                            
                            Thread.sleep(2000);
                        }
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
    
    public void preSubscribe(Query query) {
        this.lastQuery = query;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public <V extends MarketData<V>> ConsumerAgent register(MarketObserver<V> callback, Class<V> clazz) {
        this.callback = (MarketObserver<Market>) callback;
        
        return new ConsumerAgent() {

            @Override
            public Observable<Result<Instrument>> include(String... symbols) {
                //Use the historical service to get the last price for accurate simulation.
                if(lastQuery.hasCustomQuery()) {
                    histService.subscribe(new HistoricalSubject(), lastQuery.toSubscription(makeInstrument(symbols[0])), lastQuery);
                }else{
                    histService.subscribe(new HistoricalSubject(), lastQuery.toSubscription(makeInstrument(symbols[0])));
                }
                return instrument(symbols);
            }

            @Override
            public Observable<Result<Instrument>> exclude(
                    String... symbols) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public State state() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isActive() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void activate() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void deactivate() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void terminate() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public boolean hasMatch(Instrument instrument) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public String expression() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Filter filter() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void filter(Filter filter) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void include(Metadata... meta) {
                if(meta[0] instanceof Instrument) {
                	this.include(lastQuery.getSymbol());
                }
            }

            @Override
            public void exclude(Metadata... meta) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void clear() {
                // TODO Auto-generated method stub
                
            }
            
        };
    }

    @Override
    public Observable<Market> snapshot(InstrumentID instrument) {
        // TODO Auto-generated method stub
        return null;
    }

    private boolean isEnabled;
    private Monitor listener;
    private Object enableLock = new Object();
    @Override
    public void startup() {
    	(new Thread(){
            public void run() {
            	if(!isEnabled) {
            		isEnabled = true;
            		try { Thread.sleep(2000); }catch(Exception e) { e.printStackTrace(); }
 	                listener.handle(com.barchart.feed.api.connection.Connection.State.CONNECTING, null);
 	                try { Thread.sleep(2000); }catch(Exception e) { e.printStackTrace(); }
 	                listener.handle(com.barchart.feed.api.connection.Connection.State.CONNECTED, null);
            	}
            	
            	if(isEnabled) {
	               synchronized(enableLock) {
	            	   try {
	            		   enableLock.wait();
	            		   System.out.println("enable lock released");
	            	   }catch(Exception e) {
	            		   e.printStackTrace();
	            	   }
	               }
            	}
            }
        }).start();
    }

    @Override
    public void shutdown() {
        isEnabled = false;
        synchronized(enableLock) {
        	try {
        		enableLock.notify();
        	}catch(Exception e) {
        		e.printStackTrace();
        	}
        }
    }

    @Override
    public void bindConnectionStateListener(final Monitor listener) {
        this.listener = listener;
    }

    @Override
    public void bindTimestampListener(TimestampListener listener) {
        // TODO Auto-generated method stub
        
    }
    
    private Market makeMarket(final DateTime t, final Instrument i, final String price, final String sz) {
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
                return new Time() {

                    @Override
                    public String zone() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public long millisecond() {
                        return time.getMillis();
                    }

                    @Override
                    public int compareTo(Time thatTime) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public boolean isNull() {
                        // TODO Auto-generated method stub
                        return false;
                    }
                };
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
                        // TODO Auto-generated method stub
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
            
        };
    }
    
    private Size makeSize(final String sz) {
        return new Size() {
            int size = Integer.parseInt(sz);
            
            @Override
            public boolean equalsScale(Size that) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public double asDouble() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long mantissa() {
                return size;
            }

            @Override
            public int exponent() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public boolean isZero() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Size scale(int exponent) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size norm() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size neg() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size add(Size that) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size add(long increment) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size sub(Size that) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size sub(long decrement) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size mult(Scaled<?> factor) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size mult(long factor) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size div(Scaled<?> factor) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size div(long factor) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public long count(Size that) throws ArithmeticException {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public boolean greaterThan(Size that) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean lessThan(Size that) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public int compareTo(Size that) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public boolean isNull() {
                // TODO Auto-generated method stub
                return false;
            }
            
        };
    }
    
    private Price makePrice(String dblStr) {
        final double db = new BigDecimal(dblStr).setScale(2, RoundingMode.HALF_UP).doubleValue();
        
        final int exponent = (int)DoubleUtil.fracPart(db);
        final long mantissa = DoubleUtil.intPart(db);
        Price p = new Price() {
            private int expon = exponent;
            private long mant = mantissa;
            private double doub = db;
            
            @Override
            public boolean equalsScale(Price that) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public double asDouble() {
                return doub;
            }

            @Override
            public long mantissa() {
                return mant;
            }

            @Override
            public int exponent() {
                // TODO Auto-generated method stub
                return expon;
            }

            @Override
            public boolean isZero() {
                return false;
            }

            @Override
            public Price scale(int exponent) throws ArithmeticException {
                return null;
            }

            @Override
            public Price norm() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price neg() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price add(Price that) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price add(long increment) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price sub(Price that) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price sub(long decrement) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price mult(Scaled<?> factor) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price mult(long factor) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price div(Scaled<?> factor) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price div(long factor) throws ArithmeticException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public long count(Price that) throws ArithmeticException {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public boolean greaterThan(Price that) {
                return doub > that.asDouble();
            }

            @Override
            public boolean lessThan(Price that) {
                return doub < that.asDouble();
            }

            @Override
            public int compareTo(Price that) {
                 return Double.compare(doub, that.asDouble());
            }

            @Override
            public boolean isNull() {
                return false;
            }
            
        };
        
        return p;
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

    @Override
    public Observable<Result<Instrument>> instrument(final String... symbols) {
        Result<Instrument> r = new Result<Instrument>() {
            @Override public SearchContext context() { return null; }
            @Override public boolean isNull() { return false; }
            @Override 
            public Map<String, List<Instrument>> results() {
                Map<String, List<Instrument>> map = new HashMap<String, List<Instrument>>();
                List<Instrument> l = new ArrayList<Instrument>();
                Instrument i = makeInstrument(symbols[0]);
                l.add(i);
                map.put(symbols[0], l);
                return map;
            }
        };
        
        return Observable.from(r);
    }
    
    @Override
    public Observable<Result<Instrument>> instrument(SearchContext ctx,
            String... symbols) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<InstrumentID, com.barchart.feed.api.connection.Subscription<Instrument>> instruments() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<ExchangeID, com.barchart.feed.api.connection.Subscription<Exchange>> exchanges() {
        // TODO Auto-generated method stub
        return null;
    }
}
