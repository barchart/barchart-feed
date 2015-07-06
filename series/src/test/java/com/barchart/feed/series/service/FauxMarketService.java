package com.barchart.feed.series.service;

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
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import rx.Observable;

import com.barchart.feed.api.AgentID;
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
import com.barchart.feed.api.model.meta.id.ChannelID;
import com.barchart.feed.api.model.meta.id.ExchangeID;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.MetadataID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.model.meta.instrument.Calendar;
import com.barchart.feed.api.model.meta.instrument.PriceFormat;
import com.barchart.feed.api.model.meta.instrument.Schedule;
import com.barchart.feed.api.model.meta.instrument.SpreadLeg;
import com.barchart.feed.api.model.meta.instrument.SpreadType;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.network.Query;
import com.barchart.feed.api.series.service.HistoricalObserver;
import com.barchart.feed.api.series.service.HistoricalResult;
import com.barchart.feed.series.network.SeriesSubscription;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;

public class FauxMarketService implements MarketService {
    private MarketObserver<Market> callback;

    private final HashSet<Market.Component> staticSet = new HashSet<Market.Component>();

    private final FauxHistoricalService histService;

    private final Map<Instrument, SymbolTick> lastTicks = new HashMap<Instrument, SymbolTick>();

    private final ValueFactory factory = new ValueFactoryImpl();

    private Query lastQuery;

    private boolean isRunning;

    private final DateTimeFormatter tickFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final DateTimeFormatter minuteFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

    private Thread serverThread;

    public FauxMarketService(final String uname, final String pword) {
        this.histService = new FauxHistoricalService(null);
        staticSet.add(Market.Component.TRADE);
    }

    private class HistoricalSubject implements HistoricalObserver<HistoricalResult> {
        @Override public void onCompleted() {}
        @Override public void onError(final Throwable e) {}
        @Override
        public void onNext(final HistoricalResult historicalResult) {
            final String csv = historicalResult.getResult().get(historicalResult.getResult().size() - 1);
            System.out.println("last csv = " + csv);
            final String[] csvArray = csv.split("\\,");

            final SymbolTick config = new SymbolTick();
            final SeriesSubscription d = (SeriesSubscription)historicalResult.getSubscription();
            config.desc = d;

            final Price p = makePrice(csvArray[csvArray.length - 2]);
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
            Random random = new Random();
            boolean isNeg;

            @Override
			public void run() {
                try {
                    while(isRunning) {
                        for(final Instrument i : lastTicks.keySet()) {
                            final double added = isNeg ? random.nextDouble() * -1 : random.nextDouble();
                            isNeg = !isNeg;

                            final SymbolTick lastTick = lastTicks.get(i);
                            final double newPrice = lastTick.lastPrice.asDouble() + added;
                            lastTick.lastPrice = makePrice("" + newPrice);
                            lastTick.lastTime = new DateTime(lastTick.lastTime.plusMillis((int)(500 * random.nextDouble())));
                            lastTick.lastSize = makeSize("" + random.nextInt(10));

                            final Market m = makeMarket(lastTick.lastTime, lastTick.desc.getInstrument(),
                                "" + lastTick.lastPrice.asDouble(), "" + lastTick.lastSize.mantissa());

                            callback.onNext(m);

                            Thread.sleep(20);
                        }
                    }
                }catch(final Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void preSubscribe(final Query query) {
        this.lastQuery = query;
    }

    @SuppressWarnings("unchecked")
	@Override
    public <V extends MarketData<V>> ConsumerAgent register(final MarketObserver<V> callback, final Class<V> clazz) {
        this.callback = (MarketObserver<Market>) callback;

        return new ConsumerAgent() {

            @Override
            public Observable<Result<Instrument>> include(final String... symbols) {
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
                    final String... symbols) {
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
            public boolean hasMatch(final Instrument instrument) {
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
            public void filter(final Filter filter) {
                // TODO Auto-generated method stub

            }

            @Override
            public void include(final Metadata... meta) {
                if(meta[0] instanceof Instrument) {
                	this.include(lastQuery.getSymbols().get(0));
                }
            }

            @Override
            public void exclude(final Metadata... meta) {
                // TODO Auto-generated method stub

            }

            @Override
            public void clear() {
                // TODO Auto-generated method stub

            }

			@Override
			public void include(final MetadataID<?>... metaID) {
				// TODO Auto-generated method stub

			}

			@Override
			public void exclude(final MetadataID<?>... metaID) {
				// TODO Auto-generated method stub

			}

			@Override
			public AgentID id() {
				// TODO Auto-generated method stub
				return null;
			}

        };
    }

    @Override
    public Observable<Market> snapshot(final InstrumentID instrument) {
        // TODO Auto-generated method stub
        return null;
    }

    private boolean isEnabled;
    private Monitor listener;
    private final Object enableLock = new Object();
    @Override
    public void startup() {
    	(new Thread(){
            @Override
			public void run() {
            	if(!isEnabled) {
            		isEnabled = true;
            		try { Thread.sleep(2000); }catch(final Exception e) { e.printStackTrace(); }
 	                listener.handle(com.barchart.feed.api.connection.Connection.State.CONNECTING, null);
 	                try { Thread.sleep(2000); }catch(final Exception e) { e.printStackTrace(); }
 	                listener.handle(com.barchart.feed.api.connection.Connection.State.CONNECTED, null);
            	}

            	if(isEnabled) {
	               synchronized(enableLock) {
	            	   try {
	            		   enableLock.wait();
	            		   System.out.println("enable lock released");
	            	   }catch(final Exception e) {
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
        	}catch(final Exception e) {
        		e.printStackTrace();
        	}
        }
    }

    @Override
    public void bindConnectionStateListener(final Monitor listener) {
        this.listener = listener;
    }

    @Override
    public void bindTimestampListener(final TimestampListener listener) {
        // TODO Auto-generated method stub

    }

    private Market makeMarket(final DateTime t, final Instrument i, final String price, final String sz) {
        return new Market() {
            private final Instrument inst = i;
            private final DateTime time = t;
            private final Price p = makePrice(price);
            private final Set<Market.Component> thisSet = staticSet;
            private final Size size = makeSize(sz);


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

					@Override
					public Sequence sequence() {
						// TODO Auto-generated method stub
						return null;
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

    private Size makeSize(final String sz) {
        return factory.newSize(Integer.parseInt(sz));
    }

    private Price makePrice(final String dblStr) {
        final double db = new BigDecimal(dblStr).setScale(2, RoundingMode.HALF_UP).doubleValue();
        final Price p = factory.newPrice(db);

        return p;
    }

    public Instrument makeInstrument(final String symbol) {
        return new Instrument() {

            @Override
            public int compareTo(final Instrument arg0) {
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
			public VendorID vendor() {
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

			@Override
			public String currencyCode() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String instrumentGroup() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public State state() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ChannelID channel() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DateTime created() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DateTime updated() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Calendar calendar() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Schedule schedule() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DateTimeZone timeZone() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PriceFormat priceFormat() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PriceFormat optionStrikePriceFormat() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<InstrumentID> components() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DateTime delivery() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DateTime expiration() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InstrumentID underlier() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Price strikePrice() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public OptionType optionType() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public OptionStyle optionStyle() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public SpreadType spreadType() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<SpreadLeg> spreadLegs() {
				// TODO Auto-generated method stub
				return null;
			}
        };
    }

    @Override
    public Observable<Result<Instrument>> instrument(final String... symbols) {
        final Result<Instrument> r = new Result<Instrument>() {
            @Override public SearchContext context() { return null; }
            @Override public boolean isNull() { return false; }
            @Override
            public Map<String, List<Instrument>> results() {
                final Map<String, List<Instrument>> map = new HashMap<String, List<Instrument>>();
                final List<Instrument> l = new ArrayList<Instrument>();
                final Instrument i = makeInstrument(symbols[0]);
                l.add(i);
                map.put(symbols[0], l);
                return map;
            }
        };

        return Observable.from(r);
    }

    @Override
    public Observable<Result<Instrument>> instrument(final SearchContext ctx,
            final String... symbols) {
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

	@Override
	public Observable<Map<InstrumentID, Instrument>> instrument(final InstrumentID... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numberOfSubscriptions() {
		// TODO Auto-generated method stub
		return 0;
	}
}
