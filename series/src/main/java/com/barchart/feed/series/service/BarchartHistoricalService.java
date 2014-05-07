package com.barchart.feed.series.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import rx.Observable;
import rx.Subscriber;

import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.network.Query;
import com.barchart.feed.api.series.network.Subscription;
import com.barchart.feed.api.series.service.HistoricalObserver;
import com.barchart.feed.api.series.service.HistoricalResult;
import com.barchart.feed.api.series.service.HistoricalService;
import com.barchart.feed.series.network.SeriesSubscription;


/**
 * For now this is a rudimentary implementation of a HistoricalService which
 * will be further built out to handle more historical query options and nuances.
 *
 * @author David Ray
 *
 * @param <T>
 */
public class BarchartHistoricalService<T extends HistoricalResult> extends HistoricalService<T> {


	private final String uname;
	private final String pword;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);


	/**
	 *
	 * @param username
	 * @param password
	 */
	public BarchartHistoricalService(final String username, final String password) {

		super(new Observable.OnSubscribe<T>() {
			@Override
			public void call(final Subscriber<? super T> t1) {
				// Unsubscribe hook
				t1.add(new SeriesSubscription());
			}
		});

		this.uname = username;
		this.pword = password;
	}

	/**
	 * Starts a task after a short delay to produce query results which will be
	 * returned to the specified observer's {@link HistoricalObserver#onNext(HistoricalResult)}
	 * method.
	 *
	 * @param observer
	 * @param nodeIO
	 */
	@Override
	public <S extends Subscription> void subscribe(final HistoricalObserver<T> observer, final S nodeIO) {
		scheduler.schedule(new HistoricalRetriever(observer, (SeriesSubscription)nodeIO, null), 2000, TimeUnit.MILLISECONDS);
	}

	/**
     * Starts a task after a short delay to produce query results which will be
     * returned to the specified observer's {@link HistoricalObserver#onNext(HistoricalResult)}
     * method.
     *
     * @param observer
     * @param nodeIO
     * @param customQuery
     */
	@Override
    public <S extends Subscription> void subscribe(final HistoricalObserver<T> observer, final S nodeIO, final Query customQuery) {
        scheduler.schedule(new HistoricalRetriever(observer, (SeriesSubscription)nodeIO, customQuery), 2000, TimeUnit.MILLISECONDS);
    }


    /**
     * Returns a partially completed URL with everything excepting any start date,
     * end date or ordering specification.
     *
     * @param subscription
     * @return
     */
	private String prepareURL(final SeriesSubscription subscription) {
		String baseUrl = subscription.getTimeFrames()[0].getPeriod().getPeriodType() == PeriodType.TICK ?
			TICK_URL_SUFFIX : MINUTE_URL_SUFFIX;

		baseUrl = baseUrl.replaceFirst("\\$\\{UNAME\\}", uname).replaceFirst("\\$\\{PWORD\\}", pword).
			replaceFirst("\\$\\{SYMB\\}", subscription.getSymbol());

		return URL_PREFIX.append(baseUrl).toString();
	}

	/**
	 * Returns the specified date formatted to a ddf plus historical query
	 * string date.
	 *
	 * @param current
	 * @return
	 */
	private String nextDateStr(final DateTime current) {
		final DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
		return format.print(current);
	}

	/**
	 * Runnable which is fired to handle a single query (though TICK queries
	 * may required several connections executed in a loop).
	 */
	class HistoricalRetriever implements Runnable {
		HistoricalObserver<T> observer;
		SeriesSubscription nodeIO;
		Query customQuery;

		Thread dispatcher;
		Object dispatchLock = new Object();

		List<String> results = new ArrayList<String>();

		@SuppressWarnings("unchecked")
		public HistoricalRetriever(final HistoricalObserver<T> obs, final SeriesSubscription io, final Query query) {
			this.observer = obs;
			this.nodeIO = io;
			this.customQuery = query;

			dispatcher = new Thread() {
				@Override
				public void run() {
					try {
						synchronized(dispatchLock) {
							dispatchLock.wait();

							observer.onNext((T)new HistoricalResult() {
								@Override
								public SeriesSubscription getSubscription() {
									return nodeIO;
								}
								@Override
								public List<String> getResult() {
									return results;
								}
							});
						}
					}catch(final Exception e) {
						e.printStackTrace();
					}
				}
			};

			dispatcher.start();
		}

		/**
		 * Main dispatch point which routes to one of three methods:
		 * <pre>
		 * 	1. executeCustom()
		 *  2. executeForTicks()
		 *  3. executeForMinutes()
		 * </pre>
		 */
		@Override
		public void run() {
			String url = prepareURL(nodeIO);

			if(customQuery != null) {
			    url = url.concat(customQuery.getCustomQuery());
			    executeCustom(url);
			}else{
			    switch(nodeIO.getTimeFrames()[0].getPeriod().getPeriodType()) {
	                case TICK: {
	                    executeForTicks(url); break;
	                }
	                default: {
	                    executeForMinutes(url); break;
	                }
	            }
			}
		}

		/**
		 * Opens a new connection on behalf of the looped connection method handling
		 * pages of TICK requests.
		 *
		 * @param urlString	the new url to use (with updated parameters for current
		 * 					loop iteration).
		 * @return	the new connection.
		 */
		private BufferedReader nextConnection(final String urlString) {
			BufferedReader br = null;
			try {
				final URL url = new URL(urlString);
			    final URLConnection huc = url.openConnection();
			    huc.setUseCaches(false);
			    br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
			}catch(final Exception e) {
				e.printStackTrace();
			}

			return br;
		}

		/**
		 * Executes TICKs query.
		 *
		 * @param url
		 */
		public void executeForTicks(final String url) {
			BufferedReader br = null;
			try {
				final DateTime endDate = nodeIO.getTimeFrames()[0].getEndDate();
				final DateTime now = PeriodType.DAY.resolutionInstant(endDate == null ? new DateTime() : endDate);
				DateTime queryDate = nodeIO.getTimeFrames()[0].getStartDate();

				while(PeriodType.DAY.resolutionInstant(queryDate).isBefore(now)) {
					String line;
					final String urlStr = url + ("start="+nextDateStr(queryDate)+"&order=asc");
					System.out.println("url = " + urlStr);
					br = nextConnection(urlStr);
				    while((line = br.readLine()) != null) {
				        //System.out.println(line + "\n");
				        if(line.trim().length() > 0) {
				            results.add(line);
				        }
				    }
				    if (null != br) {
				       br.close();
				    }
				    queryDate = queryDate.plusDays(1);
				}
			}
			catch (final IOException ioe) { ioe.printStackTrace(); }
			finally {
				try {
					br.close();
				}catch(final Exception e) { e.printStackTrace(); }
			}

			try {
				synchronized(dispatchLock) {
				    dispatchLock.notify();
				}
			}catch(final Exception e) { e.printStackTrace(); }
		}

		/**
		 * Executes custom query passed in from the client.
		 *
		 * @param url
		 */
		public void executeCustom(final String url) {
            BufferedReader br = null;
            try {
                final String urlStr = url;
                System.out.println("custom url = " + urlStr);

                String line = null;
                br = nextConnection(urlStr);
                while((line = br.readLine()) != null) {
                    //System.out.println(line + "\n");
                    if(line.trim().length() > 0) {
                        results.add(line);
                    }
                }
                if (null != br) {
                   br.close();
                }
            }
            catch (final IOException ioe) { ioe.printStackTrace(); }
            finally {
                try {
                    br.close();
                }catch(final Exception e) { e.printStackTrace(); }
            }

            try {
                synchronized(dispatchLock) {
                    dispatchLock.notify();
                }
            }catch(final Exception e) { e.printStackTrace(); }
        }

		/**
		 * Executes minute query
		 *
		 * @param url
		 */
		public void executeForMinutes(final String url) {
			BufferedReader br = null;
			try {
				final DateTime queryDate = nodeIO.getTimeFrames()[0].getStartDate();
				final DateTime endDate = nodeIO.getTimeFrames()[0].getEndDate();
				String line;

				final String urlStr = url + ("start="+nextDateStr(queryDate) +
					(endDate == null ? "" : "&" + nextDateStr(endDate)) +
						"&order=asc");
				System.out.println("url = " + urlStr);

				br = nextConnection(urlStr);
			    while((line = br.readLine()) != null) {
			        //System.out.println(line + "\n");
			        if(line.trim().length() > 0) {
			            results.add(line);
			        }
			    }
			    if (null != br) {
			       br.close();
			    }
			}
			catch (final IOException ioe) { ioe.printStackTrace(); }
			finally {
				try {
					br.close();
				}catch(final Exception e) { e.printStackTrace(); }
			}

			try {
				synchronized(dispatchLock) {
					dispatchLock.notify();
				}
			}catch(final Exception e) { e.printStackTrace(); }
		}
	}

}
