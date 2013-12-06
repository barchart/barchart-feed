package com.barchart.feed.series.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

import com.barchart.feed.api.series.services.HistoricalObserver;
import com.barchart.feed.api.series.services.HistoricalResult;
import com.barchart.feed.api.series.services.Query;
import com.barchart.feed.api.series.temporal.PeriodType;


public class HistoricalService<T extends HistoricalResult> extends Observable<T> {
	private static final StringBuilder URL_PREFIX = new StringBuilder("http://ds01.ddfplus.com/historical/");
	
	private static final String TICK_URL_SUFFIX = 
		"queryticks.ashx?username=${UNAME}&password=${PWORD}&symbol=${SYMB}&";
	
	private static final String MINUTE_URL_SUFFIX = 
		"queryminutes.ashx?username=${UNAME}&password=${PWORD}&symbol=${SYMB}&";
	
	private String uname;
	private String pword;
	
	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	
	/**
	 * 
	 * @param username
	 * @param password
	 */
	public HistoricalService(String username, String password) {
		super(new Observable.OnSubscribeFunc<T>() {
			@Override public Subscription onSubscribe(Observer<? super T> t1) {
				return new Subscription() { @Override public void unsubscribe() {} };
			}
		});
		
		this.uname = username;
		this.pword = password;
	}
	
	/**
	 * Immediately starts a task to produce query results which will be 
	 * returned to the specified observer's {@link HistoricalObserver#onNext(HistoricalResult)}
	 * method.
	 * @param observer
	 * @param query
	 */
	public void subscribe(HistoricalObserver<T> observer, Query query) {
		executorService.submit(new HistoricalRetriever(observer, query));
	}
	
	private String prepareURL(Query query) {
		String baseUrl = query.getPeriod().getPeriodType() == PeriodType.TICK ? 
			TICK_URL_SUFFIX : MINUTE_URL_SUFFIX;
		
		baseUrl = baseUrl.replaceFirst("\\$\\{UNAME\\}", uname).replaceFirst("\\$\\{PWORD\\}", pword).
			replaceFirst("\\$\\{SYMB\\}", query.getSymbol());
		
		return URL_PREFIX.append(baseUrl).toString();
	}
	
	private String nextDateStr(DateTime current) {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
		return format.print(current);
	}
	
	class HistoricalRetriever implements Runnable {
		HistoricalObserver<T> observer;
		Query query;
		
		Thread dispatcher;
		Object dispatchLock = new Object();
		
		List<String> results = new ArrayList<String>();
		
		@SuppressWarnings("unchecked")
		public HistoricalRetriever(HistoricalObserver<T> obs, Query q) {
			this.observer = obs;
			this.query = q;
			
			dispatcher = new Thread() {
				public void run() {
					try {
						synchronized(dispatchLock) {
							dispatchLock.wait();
							
							observer.onNext((T)new HistoricalResult() {
								@Override
								public Query getQuery() {
									return query;
								}
								@Override
								public List<String> getResult() {
									return results;
								}
							});
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			};
		}
		
		public void run() {
			String url = prepareURL(query);
			
			switch(query.getPeriod().getPeriodType()) {
				case TICK: {
					executeForTicks(url); break;
				}
				default: {
					executeForMinutes(url); break;
				}
			}
		}
		
		private BufferedReader nextConnection(String urlString) {
			BufferedReader br = null;
			try {
				URL url = new URL(urlString);
			    final URLConnection huc = url.openConnection();
			    huc.setUseCaches(false);
			    br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return br;
		}
		
		
		public void executeForTicks(String url) {
			try {
				BufferedReader br = null;
				DateTime now = PeriodType.DAY.resolutionInstant(query.getEnd() == null ? new DateTime() : query.getEnd());
				DateTime queryDate = query.getStart();
				
				while(PeriodType.DAY.resolutionInstant(queryDate).isBefore(now)) {
					String line;
					String urlStr = url + ("start="+nextDateStr(queryDate)+"&order=asc");
					System.out.println("url = " + urlStr);
					br = nextConnection(urlStr);
				    while((line = br.readLine()) != null) {
				        System.out.println(line + "\n");
				        results.add(line);
				    }
				    if (null != br) {
				       br.close();
				    }
				    queryDate = queryDate.plusDays(1);
				}
			}
			catch (final IOException ioe) { ioe.printStackTrace(); }
			
			try {
				synchronized(dispatchLock) {
					dispatchLock.notify();
				}
			}catch(Exception e) { e.printStackTrace(); }
		}
		
		
		public void executeForMinutes(String url) {
			try {
				BufferedReader br = null;
				DateTime queryDate = query.getStart();
				
				String line;
				
				String urlStr = url + ("start="+nextDateStr(queryDate) +
					(query.getEnd() == null ? "" : "&" + nextDateStr(query.getEnd())) + 
						"&order=asc");
				System.out.println("url = " + urlStr);
				
				br = nextConnection(urlStr);
			    while((line = br.readLine()) != null) {
			        System.out.println(line + "\n");
			        results.add(line);
			    }
			    if (null != br) {
			       br.close();
			    }
			}
			catch (final IOException ioe) { ioe.printStackTrace(); }
			
			try {
				synchronized(dispatchLock) {
					dispatchLock.notify();
				}
			}catch(Exception e) { e.printStackTrace(); }
		}
	}
}
