package com.barchart.feed.series;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import rx.Observer;

import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.connection.Connection;
import com.barchart.feed.api.connection.Connection.Monitor;
import com.barchart.feed.api.connection.Connection.State;
import com.barchart.feed.api.consumer.ConsumerAgent;
import com.barchart.feed.api.consumer.MetadataService.Result;
import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.client.provider.BarchartMarketProvider;

public class TestDataSeriesClient {
	
	private static final String[] insts = {"ESZ13", "AAPL"};
	
	private static ConsumerAgent agent1;
	private static BarchartMarketProvider provider;
	
	
	public TestDataSeriesClient() throws InterruptedException {
		provider = new BarchartMarketProvider("dray", "dray");
		
		final CountDownLatch lock = new CountDownLatch(1);
		
		provider.bindConnectionStateListener(listener(lock));
		provider.startup();
		
		lock.await();
		
		agent1 = provider.register(marketObs(), Market.class);
		
		agent1.include(insts).subscribe(instObs());
		
		/////////////////
		//http://extras.ddfplus.com/instruments/?lookup
		//http://ds01.ddfplus.com/historical/queryticks.ashx?username=username&password=password&
//		historicalTest();
		
	}
	
	private static void historicalTest() {
		try {
			BufferedReader br = null;
			DateTime now = new DateTime().minusDays(1);
			DateTime next = new DateTime();
			
			while(true) {
				String line;
				String urlStr = "http://ds01.ddfplus.com/historical/queryticks.ashx?username=dray&password=dray&symbol=GOOG&";
				urlStr += ("start="+nextDateStr(now)+"&end="+nextDateStr(next));
				System.out.println("url = " + urlStr);
				br = nextConnection(urlStr);
			    while((line = br.readLine()) != null) {
			        System.out.println("Date: " + now);
			        System.out.println(line + "\n");
			        
			        try {Thread.sleep(2000);}catch(Exception ex) { ex.printStackTrace(); }
			        
			        break;
			    }
			    if (null != br) {
			       br.close();
			    }
			    now = now.minusDays(1);
			    next = next.minusDays(1);
		    }
		}
		catch (final IOException ioe) {}
	}
	
	private static String nextDateStr(DateTime current) {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
		return format.print(current);
	}
	
	private static BufferedReader nextConnection(String urlString) {
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
	
	private static Monitor listener(final CountDownLatch lock) { 
		return new Monitor() {

			@Override
			public void handle(State state, Connection connection) {
				
				System.out.println("New State = {} "+ state);
				
				if(state == State.CONNECTED) {
					lock.countDown();
				}
				
				System.out.println("latch counted down");
			}
		};
	}
	
	private static MarketObserver<Market> marketObs() {
		
		return new MarketObserver<Market>() {

			@Override
			public void onNext(final Market v) {
				final Book.Top top = v.book().top();
				System.out.println("UPDATE: {} "+ top.ask() + " - " + top.bid() + " - " + v.instrument().marketHours());
			}
		};
	}
	
	private static Observer<Result<Instrument>> instObs() {
		
		return new Observer<Result<Instrument>>() {

			@Override
			public void onCompleted() {
				System.out.println("Lookup and registration complete");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("Exception in lookup and registration \n{} "+ e);
			}

			@Override
			public void onNext(Result<Instrument> args) {
				System.out.println("New Instrument Lookup and Registration");
				
				for(List<Instrument> l : args.results().values()) {
					System.out.println("\t listing: " + l.size());
					for(Instrument i : l) {
						System.out.println("\t\t" + i + " - mh = " + i.marketHours());
					}
				}
			}
			
		};
	}
	
	public static void main(String[] args) {
		try {
			new TestDataSeriesClient();
			
//			agent1.exclude(insts).subscribe(instObs());
//			Thread.sleep(5 * 1000);
//			System.out.println("Shutting down");
//			provider.shutdown();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
