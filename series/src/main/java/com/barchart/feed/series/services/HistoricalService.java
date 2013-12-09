package com.barchart.feed.series.services;

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
import rx.Observer;
import rx.Subscription;

import com.barchart.feed.api.series.services.HistoricalObserver;
import com.barchart.feed.api.series.services.HistoricalResult;
import com.barchart.feed.api.series.services.NodeIODescriptor;
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
	
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
	
	
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
	 * Starts a task after a short delay to produce query results which will be 
	 * returned to the specified observer's {@link HistoricalObserver#onNext(HistoricalResult)}
	 * method.
	 * 
	 * @param observer
	 * @param nodeIO
	 */
	public void subscribe(HistoricalObserver<T> observer, NodeIODescriptor nodeIO) {
		scheduler.schedule(new HistoricalRetriever(observer, nodeIO, null), 2000, TimeUnit.MILLISECONDS);
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
    public void subscribe(HistoricalObserver<T> observer, NodeIODescriptor nodeIO, Query customQuery) {
        scheduler.schedule(new HistoricalRetriever(observer, nodeIO, customQuery), 2000, TimeUnit.MILLISECONDS);
    }
	
	private String prepareURL(NodeIODescriptor io) {
		String baseUrl = io.getTimeFrames()[0].getPeriod().getPeriodType() == PeriodType.TICK ? 
			TICK_URL_SUFFIX : MINUTE_URL_SUFFIX;
		
		baseUrl = baseUrl.replaceFirst("\\$\\{UNAME\\}", uname).replaceFirst("\\$\\{PWORD\\}", pword).
			replaceFirst("\\$\\{SYMB\\}", io.getSymbol());
		
		return URL_PREFIX.append(baseUrl).toString();
	}
	
	private String nextDateStr(DateTime current) {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
		return format.print(current);
	}
	
	class HistoricalRetriever implements Runnable {
		HistoricalObserver<T> observer;
		NodeIODescriptor nodeIO;
		Query customQuery;
		
		Thread dispatcher;
		Object dispatchLock = new Object();
		
		List<String> results = new ArrayList<String>();
		
		@SuppressWarnings("unchecked")
		public HistoricalRetriever(HistoricalObserver<T> obs, NodeIODescriptor io, Query query) {
			this.observer = obs;
			this.nodeIO = io;
			this.customQuery = query;
			
			dispatcher = new Thread() {
				public void run() {
					try {
						synchronized(dispatchLock) {
							dispatchLock.wait();
							
							observer.onNext((T)new HistoricalResult() {
								@Override
								public NodeIODescriptor getIODescriptor() {
									return nodeIO;
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
			
			dispatcher.start();
		}
		
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
			BufferedReader br = null;
			try {
				DateTime endDate = nodeIO.getTimeFrames()[0].getEndDate();
				DateTime now = PeriodType.DAY.resolutionInstant(endDate == null ? new DateTime() : endDate);
				DateTime queryDate = nodeIO.getTimeFrames()[0].getStartDate();
				
				while(PeriodType.DAY.resolutionInstant(queryDate).isBefore(now)) {
					String line;
					String urlStr = url + ("start="+nextDateStr(queryDate)+"&order=asc");
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
				}catch(Exception e) { e.printStackTrace(); }
			}
			
			try {
				synchronized(dispatchLock) {
				    dispatchLock.notify();
				}
			}catch(Exception e) { e.printStackTrace(); }
		}
		
		public void executeCustom(String url) {
            BufferedReader br = null;
            try {
                String urlStr = url;
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
                }catch(Exception e) { e.printStackTrace(); }
            }
            
            try {
                synchronized(dispatchLock) {
                    dispatchLock.notify();
                }
            }catch(Exception e) { e.printStackTrace(); }
        }
		
		
		public void executeForMinutes(String url) {
			BufferedReader br = null;
			try {
				DateTime queryDate = nodeIO.getTimeFrames()[0].getStartDate();
				DateTime endDate = nodeIO.getTimeFrames()[0].getEndDate();
				String line;
				
				String urlStr = url + ("start="+nextDateStr(queryDate) +
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
				}catch(Exception e) { e.printStackTrace(); }
			}
			
			try {
				synchronized(dispatchLock) {
					dispatchLock.notify();
				}
			}catch(Exception e) { e.printStackTrace(); }
		}
	}
}
